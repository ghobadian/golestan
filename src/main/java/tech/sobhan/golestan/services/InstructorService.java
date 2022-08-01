package tech.sobhan.golestan.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.business.exceptions.*;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.repositories.*;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.List;
import java.util.Map;

import static tech.sobhan.golestan.utils.Util.createLog;

@Slf4j
@Service
public class InstructorService {

    private final UserRepository userRepository;
    private final ErrorChecker errorChecker;

    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;

    public InstructorService(UserRepository userRepository, ErrorChecker errorChecker, InstructorRepository instructorRepository,
                             StudentRepository studentRepository, CourseSectionRepository courseSectionRepository,
                             CourseSectionRegistrationRepository courseSectionRegistrationRepository) {
        this.userRepository = userRepository;
        this.errorChecker = errorChecker;
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
        this.courseSectionRepository = courseSectionRepository;
        this.courseSectionRegistrationRepository = courseSectionRegistrationRepository;
    }

    public String list(String username, String password) {
        errorChecker.checkIsUser(username, password);
        //        if(allInstructors.isEmpty()){
//            throw new InstructorNotFoundException();
//        }
        return list().toString();
    }

    private List<Instructor> list() {
        return instructorRepository.findAll();
    }

    public Instructor create(Instructor instructor){
        if (instructorExists(list(), instructor)) return null;
        createLog(Instructor.class, instructor.getId());
        return instructorRepository.save(instructor);
    }

    private boolean instructorExists(List<Instructor> allInstructors, Instructor instructor) {
        for (Instructor i : allInstructors) {
            if(instructor.equals(i)){
                System.out.println("ERROR403 duplicate Instructors");
                return true;
            }
        }
        return false;
    }

    public String read(Long id, String username, String password) {//todo create service package
        errorChecker.checkIsUser(username, password);
        return read(id).toString();
    }

    private Instructor read(Long id) {
        return instructorRepository.findById(id).orElseThrow(InstructorNotFoundException::new);
    }

    public String update(Rank rank, Long id, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        instructorRepository.findById(id).map(instructor -> {
            instructor.setRank(rank);
            return instructorRepository.save(instructor);//todo sus for saveinstructor instead of instructor
        }).orElseGet(() -> {
            Instructor newInstructor = Instructor.builder().rank(rank).id(id).build();
            return instructorRepository.save(newInstructor);
        });
        return "OK";
    }

    public String delete(Long id, String username, String password) {//todo check later
        errorChecker.checkIsAdmin(username, password);
        Instructor instructor = read(id);
        List<CourseSection> courseSectionsOfInstructor = courseSectionRepository.findByInstructor(id);//todo delete course section
        courseSectionRepository.deleteAll(courseSectionRepository.findByInstructor(id));
        instructorRepository.delete(instructor);
        return "OK";
//        log.info("Instructor with id of " + id + "deleted successfully.");        //todo add log in all deletes and signups
    }

    public String giveMark(Map<String, String> json, String username, String password) {
        CourseSection courseSection = courseSectionRepository
                .findById(Long.valueOf(json.get("course_section_id"))).orElseThrow(CourseNotFoundException::new);
        String foundError = foundError(username, password, courseSection);
        if (foundError != null) return foundError;
        Student student = studentRepository
                .findById(Long.valueOf(json.get("student_id"))).orElseThrow(StudentNotFoundException::new);

        double score = Double.parseDouble(json.get("score"));
        CourseSectionRegistration newCourseSectionRegistration = courseSectionRegistrationRepository
                .findByCourseSectionAndStudent(courseSection.getId(), student.getStudentId())
                .orElseThrow(CourseSectionRegistrationNotEmptyException::new);
        newCourseSectionRegistration.setScore(score);
        courseSectionRegistrationRepository.findById(newCourseSectionRegistration.getId()).map(courseSectionRegistration -> {
            courseSectionRegistration = newCourseSectionRegistration;
            return courseSectionRegistrationRepository.save(courseSectionRegistration);
        });
        return "OK";
    }

    private String foundError(String username, String password, CourseSection courseSection) {
        if(!errorChecker.isUser(username, password)) throw new UnauthorisedException();
        if(!ErrorChecker.isInstructor(username)) return "ERROR 403";
        if(!ErrorChecker.isInstructorOfCourseSection(username, courseSection)) return "ERROR 403";
        return null;
    }

}
