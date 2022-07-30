package tech.sobhan.golestan.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.auth.User;
import tech.sobhan.golestan.business.exceptions.CourseSectionNotFoundException;
import tech.sobhan.golestan.business.exceptions.StudentNotFoundException;
import tech.sobhan.golestan.business.exceptions.UserNotFoundException;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.repositories.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static tech.sobhan.golestan.security.config.PasswordConfiguration.getPasswordEncoder;
import static tech.sobhan.golestan.utils.Util.createLog;

@Service
@Slf4j
public class StudentService {//todo clean this class
    private final StudentRepository studentRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;
    private final TermRepository termRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, CourseSectionRepository courseSectionRepository,
                          CourseSectionRegistrationRepository courseSectionRegistrationRepository,
                          TermRepository termRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.courseSectionRepository = courseSectionRepository;
        this.courseSectionRegistrationRepository = courseSectionRegistrationRepository;
        this.termRepository = termRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = getPasswordEncoder();
    }
    
    public Student create(Student student){
        if (studentExists(list(), student)) return null;
        createLog(Student.class, student.getStudentId());
        return studentRepository.save(student);
    }

    private boolean studentExists(List<Student> allStudents, Student student) {
        for (Student s : allStudents) {
            if(student.equals(s)){
                System.out.println("ERROR403 duplicate Students");
                return true;
            }
        }
        return false;
    }

    public List<Student> list() {
        //        if(allStudents.isEmpty()){
//            throw new StudentNotFoundException();
//        }
        return studentRepository.findAll();
    }

    public void signUpSection(Long course_section_id, String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        log.warn(user.getPassword()+ "|"+  passwordEncoder.encode(password));
        log.warn("boolean "  + passwordEncoder.matches(passwordEncoder.encode(password), user.getPassword()));//todo هش های یه رمز یکسان با هم فرق دارن
        if(!passwordEncoder.matches(passwordEncoder.encode(password), user.getPassword())){
            log.error("invalid credentials StudentService/signUpSection");
            return;
        }
        CourseSection foundCourseSection = courseSectionRepository.findById(course_section_id)
                .orElseThrow(CourseSectionNotFoundException::new);
//        Student foundStudent = studentRepository.findBy(student_id).orElseThrow(StudentNotFoundException::new);
        Student foundStudent = user.getStudent();
        CourseSectionRegistration newCourseSectionRegistration = CourseSectionRegistration.builder()
                .student(foundStudent).courseSection(foundCourseSection).build();
        courseSectionRegistrationRepository.save(newCourseSectionRegistration);
    }

    public String seeScoresInSpecifiedTerm(Long term_id, String username, String password) throws JSONException {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Long studentId = user.getStudent().getStudentId();
        List<CourseSectionRegistration> courseSectionRegistrations =
                findCourseSectionRegistrationsOfSpecifiedStudentAndTerm(studentId, term_id);

        double avg = findAverage(courseSectionRegistrations);
        JSONArray output = new JSONArray();
        output.put(new JSONObject("{average:"+avg+"}"));
        for (CourseSectionRegistration courseSectionRegistration : courseSectionRegistrations) {
            CourseSection courseSection = courseSectionRegistration.getCourseSection();
            Course course = courseSection.getCourse();
            JSONObject courseSectionDetails = new JSONObject();
            courseSectionDetails.put("course_section_id", courseSection.getId());
            courseSectionDetails.put("course_name", course.getTitle());
            courseSectionDetails.put("course_units", course.getUnits());
            courseSectionDetails.put("instructor", courseSection.getInstructor());
            courseSectionDetails.put("score", courseSectionRegistration.getScore());
            output.put(courseSectionDetails);
        }
        return output.toString();
    }


    private List<CourseSectionRegistration> findCourseSectionRegistrationsOfSpecifiedStudentAndTerm(Long student_id, Long term_id) {
        List<CourseSectionRegistration> courseSectionRegistrations = courseSectionRegistrationRepository
                .findByStudent(student_id);
        List<CourseSection> courseSections = courseSectionRepository.findByTerm(term_id);
        Set<CourseSectionRegistration> output = new HashSet<>();
        filterCourseSectionRegistrationsOfSpecifiedTerm(courseSectionRegistrations, courseSections, output);
        return output.stream().toList();
    }

    private void filterCourseSectionRegistrationsOfSpecifiedTerm(List<CourseSectionRegistration> courseSectionRegistrations, List<CourseSection> courseSections, Set<CourseSectionRegistration> output) {
        for (CourseSectionRegistration courseSectionRegistration : courseSectionRegistrations) {
            for (CourseSection courseSection : courseSections) {
                if(courseSection.equals(courseSectionRegistration.getCourseSection())){
                    output.add(courseSectionRegistration);
                }
            }
        }
    }

    private double findAverage(List<CourseSectionRegistration> courseSectionRegistrations) {
        double sum = 0;
        for (CourseSectionRegistration courseSectionRegistration : courseSectionRegistrations) {
            sum += courseSectionRegistration.getScore();
        }
        return sum / courseSectionRegistrations.size();
    }

    public String seeSummery(String username, String password) throws JSONException {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
//        if(checkAuthenticationOfUser(user.getPassword(), password)) return new Optional<JSONArray>;
        Student student = Optional.ofNullable(user.getStudent()).orElseThrow(StudentNotFoundException::new);
        double totalSum = 0;
        List<Term> terms = termRepository.findAll();
        JSONArray output = new JSONArray();
        for (Term term : terms) {
            JSONObject termDetails = new JSONObject();
            termDetails.put("term_id", term.getId());
            termDetails.put("term_title", term.getTitle());
            termDetails.put("term", term.getTitle());
            double averageInSpecifiedTerm = averageInSpecifiedTerm(term.getId(), student.getStudentId());
            termDetails.put("average", averageInSpecifiedTerm);
            output.put(termDetails);
            totalSum += averageInSpecifiedTerm;
        }
        double totalAverage = totalSum / terms.size();
        output.put(new JSONObject("{totalAverage:"+totalAverage+"}"));
        return output.toString();
    }

    private double averageInSpecifiedTerm(Long termId, Long studentId) {
        List<CourseSectionRegistration> courseSectionRegistrations =
                findCourseSectionRegistrationsOfSpecifiedStudentAndTerm(studentId, termId);
        return courseSectionRegistrations.isEmpty() ? 0 : findAverage(courseSectionRegistrations);
    }


}
