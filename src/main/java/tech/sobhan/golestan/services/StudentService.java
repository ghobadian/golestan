package tech.sobhan.golestan.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.dto.*;
import tech.sobhan.golestan.models.users.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {
    private final Repo repo;

    public CourseSectionRegistration signUpSection(Student student, CourseSection courseSection) {
        CourseSectionRegistration csr = CourseSectionRegistration.builder()
                .student(student).courseSection(courseSection).build();
        return repo.saveCourseSectionRegistration(csr);
    }

    public CourseSectionRegistration signUpSection(Long courseSectionId, String token) {
        Long studentId = repo.findStudentByUsername(repo.findUsernameByToken(token)).getId();
        return signUpSection(repo.findStudent(studentId), repo.findCourseSection(courseSectionId));
    }

    public EntityModel<StudentAverageDTO> seeScoresInSpecifiedTerm(Long termId, String token) {
        String username = repo.findUsernameByToken(token);
        Student student = repo.findStudentByUsername(username);
        Term term = repo.findTerm(termId);
        List<CourseSectionRegistration> csrs = repo.findCSRsByStudentAndTerm(student, term);
        List<CourseSectionDTO> courseSections = csrs.stream().map(this::getCourseSectionDetails).collect(Collectors.toList());
        return EntityModel.of(StudentAverageDTO.builder().average(findAverage(csrs)).courseSections(courseSections).build());
    }

    public CourseSectionDTO getCourseSectionDetails(CourseSectionRegistration courseSectionRegistration) {
        CourseSection courseSection = courseSectionRegistration.getCourseSection();
        Course course = courseSection.getCourse();
        return CourseSectionDTO.builder().id(courseSection.getId()).courseName(course.getTitle())
                .courseUnits(course.getUnits()).instructor(getInstructorDTO(courseSection)).build();
    }

    private InstructorDTO getInstructorDTO(CourseSection courseSection) {
        Long instructorId = courseSection.getInstructor().getId();
        String instructorName = "";
        if(repo.userExistsByInstructor(instructorId)) {
            instructorName = repo.findUserByInstructor(instructorId).getName();
        }
        return InstructorDTO.builder().name(instructorName)
                .rank(courseSection.getInstructor().getRank()).build();
    }

    private double findAverage(List<CourseSectionRegistration> courseSectionRegistrations) {
        double sum = 0;
        for (CourseSectionRegistration courseSectionRegistration : courseSectionRegistrations) {
            sum += courseSectionRegistration.getScore();
        }
        return sum / courseSectionRegistrations.size();
    }


    public SummeryDTO seeSummery(String token) {
        Student student = repo.findStudentByToken(token);
        AtomicReference<Double> totalSum = new AtomicReference<>((double) 0);
        List<Term> terms = repo.findAllTerms();
        List<TermDTO> allTermDetails = new ArrayList<>();
        terms.forEach(term -> {
            Double averageInSpecifiedTerm = findAverageByTerm(term, student);
            TermDTO termDetails = TermDTO.builder().termId(term.getId()).termTitle(term.getTitle())
                    .studentAverage(averageInSpecifiedTerm).build();
            totalSum.updateAndGet(v -> (v + averageInSpecifiedTerm));
            allTermDetails.add(termDetails);
        });
        return SummeryDTO.builder().termDetails(allTermDetails).totalAverage(calcTotalAverage(totalSum.get(), terms)).build();
    }

    private double calcTotalAverage(double totalSum, List<Term> terms) {
        int numberOfTerms = terms.size();
        return totalSum / numberOfTerms;
    }

    private double findAverageByTerm(Term term, Student studentId) {
        List<CourseSectionRegistration> courseSectionRegistrations =
                repo.findCSRsByStudentAndTerm(studentId, term);
        return courseSectionRegistrations.isEmpty() ? 0 : findAverage(courseSectionRegistrations);
    }

    public void delete(Long studentId) {
        Student student = repo.findStudent(studentId);
        List<CourseSectionRegistration> csrs = repo.findCourseSectionRegistrationByStudent(student);
        csrs.forEach(csr -> csr.setStudent(null));
        repo.deleteStudent(student);
        log.info("Student with id " + studentId + " created");
    }
}