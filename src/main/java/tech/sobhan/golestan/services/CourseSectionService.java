package tech.sobhan.golestan.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.business.exceptions.notFound.CourseNotFoundException;
import tech.sobhan.golestan.business.exceptions.notFound.InstructorNotFoundException;
import tech.sobhan.golestan.business.exceptions.notFound.TermNotFoundException;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.dto.CourseSectionDTO;
import tech.sobhan.golestan.models.dto.StudentDTO;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;

import java.util.List;
import java.util.stream.Collectors;

import static tech.sobhan.golestan.security.PaginationErrorChecker.checkPaginationErrors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseSectionService {
    private final Repo repo;

    public List<CourseSection> list(Long termId, String instructorName,
                       String courseName, Integer pageNumber, Integer maxInEachPage) {
        Term term = repo.findTerm(termId);
        List<CourseSection> filteredList = filterList(term, instructorName, courseName);
        checkPaginationErrors(filteredList.size(), pageNumber, maxInEachPage);
        return pagination(filteredList, pageNumber, maxInEachPage);
    }

    public List<StudentDTO> listCourseSectionStudents(List<CourseSectionRegistration> courseSectionRegistrations) {
        return courseSectionRegistrations.stream()
                .map(csr -> getStudentDetails(csr, repo))
                .collect(Collectors.toList());
    }

    private StudentDTO getStudentDetails(CourseSectionRegistration csr, Repo repo) {
        Student student = csr.getStudent();
        User user = repo.findUserByStudent(student.getId());
        return StudentDTO.builder().id(student.getId()).name(user.getName())
                .number(user.getPhone()).score(csr.getScore()).build();
    }

    @Value("${defaultMaxInEachPage}")
    private static Integer defaultMaxInEachPage;
    private List<CourseSection> pagination(List<CourseSection> filteredList, Integer pageNumber, Integer maxInEachPage) {//todo clean it
        if(pageNumber == null) {
            if(maxInEachPage == null) {
                return filteredList;

            }else{
                return filteredList.subList(0, maxInEachPage> filteredList.size() ? filteredList.size() : maxInEachPage);
            }
        }else{
            if(maxInEachPage == null) {
                return filteredList.subList((pageNumber - 1)  * defaultMaxInEachPage, (pageNumber)  * defaultMaxInEachPage);

            }else{
                return filteredList.subList((pageNumber - 1)  * maxInEachPage, (pageNumber)  * maxInEachPage);
            }
        }
    }

    private List<CourseSection> filterList(Term term, String instructorName, String courseName) {//todo clean it
        if(instructorName!=null && courseName == null) {
            return repo.findCourseSectionByInstructorName(instructorName);
        }else if(instructorName==null && courseName!= null) {
            return repo.findCourseSectionByCourseName(courseName);
        }else if(instructorName!=null && courseName!= null) {
            return repo.findCourseSectionByInstructorNameAndCourseName(instructorName, courseName);
        }else{
            return list(term);
        }
    }

    private List<CourseSection> list(Term term) {
        return repo.findCourseSectionByTerm(term);
    }

    public CourseSection create(CourseSection courseSection) {
        log.info("CourseSection " + courseSection + "created");
        return repo.saveCourseSection(courseSection);
    }

    public CourseSectionDTO read(Long id) {
        CourseSection courseSection = repo.findCourseSection(id);
        int numberOfStudents = repo.findNumberOfStudentsInCourseSection(courseSection);
        return CourseSectionDTO.builder().courseSection(courseSection).numberOfStudents(numberOfStudents).build();
    }

    public CourseSection update(Long termId, Long courseId, Long instructorId, Long courseSectionId) {
        CourseSection courseSection = repo.findCourseSection(courseSectionId);
        updateTerm(termId, courseSection);
        updateCourse(courseId, courseSection);
        updateInstructor(instructorId, courseSection);
        return repo.saveCourseSection(courseSection);
    }

    private void updateInstructor(Long instructorId, CourseSection courseSection) {
        try{
            Instructor instructor = repo.findInstructor(instructorId);
            courseSection.setInstructor(instructor);
        }catch (InstructorNotFoundException ignored) {
        }
    }

    private void updateCourse(Long courseId, CourseSection courseSection) {
        try{
            Course course = repo.findCourse(courseId);
            courseSection.setCourse(course);
        }catch (CourseNotFoundException ignored) {
        }
    }

    private void updateTerm(Long termId, CourseSection courseSection) {
        try{
            Term term = repo.findTerm(termId);
            courseSection.setTerm(term);
        }catch (TermNotFoundException ignored) {
        }
    }

    public void delete(CourseSection courseSection) {
        log.info("CourseSection " + courseSection + "deleted");
        repo.deleteCourseSection(courseSection);
    }
}
