package tech.sobhan.golestan.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.business.exceptions.CourseNotFoundException;
import tech.sobhan.golestan.business.exceptions.CourseSectionRegistrationNotEmpty;
import tech.sobhan.golestan.business.exceptions.InstructorNotFoundException;
import tech.sobhan.golestan.business.exceptions.StudentNotFoundException;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.repositories.CourseSectionRegistrationRepository;
import tech.sobhan.golestan.repositories.CourseSectionRepository;
import tech.sobhan.golestan.repositories.InstructorRepository;
import tech.sobhan.golestan.repositories.StudentRepository;

import java.util.List;
import java.util.Map;

import static tech.sobhan.golestan.utils.Util.createLog;

@Slf4j
@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;

    public InstructorService(InstructorRepository instructorRepository, StudentRepository studentRepository,
                                CourseSectionRepository courseSectionRepository,
                                CourseSectionRegistrationRepository courseSectionRegistrationRepository) {
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
        this.courseSectionRepository = courseSectionRepository;
        this.courseSectionRegistrationRepository = courseSectionRegistrationRepository;
    }

    public List<Instructor> list() {
        //        if(allInstructors.isEmpty()){
//            throw new InstructorNotFoundException();
//        }
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

    public Instructor read(Long id) {//todo create service package
        return instructorRepository.findById(id).orElseThrow(InstructorNotFoundException::new);
    }

    public void update(Rank rank, Long id) {
        instructorRepository.findById(id).map(instructor -> {
            instructor.setRank(rank);
            return instructorRepository.save(instructor);//todo sus for saveinstructor instead of instructor
        }).orElseGet(() -> {
            Instructor newInstructor = Instructor.builder().rank(rank).id(id).build();
            return instructorRepository.save(newInstructor);
        });
    }

    public void delete(Long id) {//todo check later
        Instructor instructor = read(id);
        List<CourseSection> courseSectionsOfInstructor = courseSectionRepository.findByInstructor(id);//todo delete course section
        courseSectionRepository.deleteAll(courseSectionRepository.findByInstructor(id));
        instructorRepository.delete(instructor);
        log.info("Instructor with id of " + id + "deleted successfully.");        //todo add log in all deletes and signups
    }

    public void giveMark(Map<String, String> json) {
        Student foundStudent = studentRepository.
                findById(Long.valueOf(json.get("student_id"))).orElseThrow(StudentNotFoundException::new);
        CourseSection foundCourseSection = courseSectionRepository
                .findById(Long.valueOf(json.get("course_section_id"))).orElseThrow(CourseNotFoundException::new);
        double score = Double.parseDouble(json.get("score"));
        CourseSectionRegistration newCourseSectionRegistration = courseSectionRegistrationRepository
                .findByCourseSectionAndStudent(foundCourseSection.getId(), foundStudent.getStudentId())
                .orElseThrow(CourseSectionRegistrationNotEmpty::new);
        newCourseSectionRegistration.setScore(score);
        courseSectionRegistrationRepository.findById(newCourseSectionRegistration.getId()).map(courseSectionRegistration -> {
            courseSectionRegistration = newCourseSectionRegistration;
            return courseSectionRegistrationRepository.save(courseSectionRegistration);
        });
    }
}
