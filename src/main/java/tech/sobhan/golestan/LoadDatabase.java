package tech.sobhan.golestan;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.sobhan.golestan.enums.Degree;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.auth.User;
import tech.sobhan.golestan.services.*;

import java.util.Date;

@Configuration
public class LoadDatabase {
//    @Bean
    CommandLineRunner initDatabase(UserService userService, InstructorService instructorService,
                                   StudentService studentService, PasswordEncoder passwordEncoder,
                                   CourseService courseService, TermService termService,
                                   CourseSectionService courseSectionService,
                                   CourseSectionRegistrationService courseSectionRegistrationService){
        return args -> {//todo add more data
            loadTables(userService, instructorService, studentService, passwordEncoder,
                    courseService, termService, courseSectionService, courseSectionRegistrationService);
        };
    }

    private void loadTables(UserService userService, InstructorService instructorService, StudentService studentService, PasswordEncoder passwordEncoder, CourseService courseService, TermService termService, CourseSectionService courseSectionService, CourseSectionRegistrationService courseSectionRegistrationService) {
        Instructor instructor1 = Instructor.builder().rank(Rank.ASSISTANT).build();
        Instructor instructor2 = Instructor.builder().rank(Rank.ASSOCIATE).build();
        Instructor esna = Instructor.builder().rank(Rank.FULL).build();
        Instructor khadem = Instructor.builder().rank(Rank.FULL).build();
        Instructor kashani = Instructor.builder().rank(Rank.FULL).build();
        loadInstructors(instructorService, instructor1, instructor2, esna, khadem, kashani);

        Student ghobadian = Student.builder().degree(Degree.BS).startDate(new Date()).build();
        Student padash = Student.builder().degree(Degree.BS).startDate(new Date()).build();
        Student zahedi = Student.builder().degree(Degree.BS).startDate(new Date()).build();
        Student student2 = Student.builder().degree(Degree.MS).startDate(new Date()).build();
        Student student3 = Student.builder().degree(Degree.PHD).startDate(new Date()).build();
        loadStudents(studentService, ghobadian, padash, zahedi, student2, student3);

        loadUsers(userService, passwordEncoder, esna, khadem, kashani, ghobadian, padash, zahedi, student2, student3);

        Course physics = Course.builder().title("physics").units(3).build();
        Course literature = Course.builder().title("literature").units(4).build();
        Course ap = Course.builder().title("ap").units(3).build();
        Course math = Course.builder().title("math").units(2).build();
        Course diff = Course.builder().title("diff").units(1).build();
        loadCourses(courseService, physics, literature, ap, math, diff);

        Term term1 = Term.builder().title("4001").open(false).build();
        Term term2 = Term.builder().title("4002").open(true).build();
        loadTerms(termService, term1, term2);

        CourseSection mathCourseSection = CourseSection.builder().course(math).term(term2).instructor(kashani).build();
        CourseSection apCourseSection = CourseSection.builder().course(ap).term(term2).instructor(esna).build();
        CourseSection diffCourseSection = CourseSection.builder().course(diff).term(term2).instructor(khadem).build();
        loadCourseSections(courseSectionService, mathCourseSection, apCourseSection, diffCourseSection);

        loadCourseSectionRegistrations(courseSectionRegistrationService, ghobadian, padash, zahedi, mathCourseSection, apCourseSection);
    }

    private void loadCourseSectionRegistrations(CourseSectionRegistrationService courseSectionRegistrationService, Student ghobadian, Student padash, Student zahedi, CourseSection mathCourseSection, CourseSection apCourseSection) {
        CourseSectionRegistration courseSectionRegistration1 = CourseSectionRegistration.builder()
                .courseSection(mathCourseSection).student(ghobadian).build();
        CourseSectionRegistration courseSectionRegistration2 = CourseSectionRegistration.builder()
                .courseSection(apCourseSection).student(ghobadian).build();
        CourseSectionRegistration courseSectionRegistration3 = CourseSectionRegistration.builder()
                .courseSection(mathCourseSection).student(padash).build();
        CourseSectionRegistration courseSectionRegistration4 = CourseSectionRegistration.builder()
                .courseSection(mathCourseSection).student(zahedi).build();
        courseSectionRegistrationService.create(courseSectionRegistration1);
        courseSectionRegistrationService.create(courseSectionRegistration2);
        courseSectionRegistrationService.create(courseSectionRegistration3);
        courseSectionRegistrationService.create(courseSectionRegistration4);
    }

    private void loadCourseSections(CourseSectionService courseSectionService, CourseSection mathCourseSection, CourseSection apCourseSection, CourseSection diffCourseSection) {
        courseSectionService.create(mathCourseSection);
        courseSectionService.create(apCourseSection);
        courseSectionService.create(diffCourseSection);
    }

    private void loadTerms(TermService termService, Term term1, Term term2) {
        termService.create(term1);
        termService.create(term2);
    }

    private void loadCourses(CourseService courseService, Course physics, Course literature, Course ap, Course math, Course diff) {
        courseService.create(physics);
        courseService.create(literature);
        courseService.create(ap);
        courseService.create(math);
        courseService.create(diff);
    }

    private void loadStudents(StudentService studentService, Student ghobadian, Student padash, Student zahedi, Student student2, Student student3) {
        studentService.create(ghobadian);
        studentService.create(padash);
        studentService.create(zahedi);
        studentService.create(student2);
        studentService.create(student3);
    }

    private void loadUsers(UserService userService, PasswordEncoder passwordEncoder, Instructor esna, Instructor khadem, Instructor kashani, Student ghobadian, Student padash, Student zahedi, Student student2, Student student3) {
        User user1 = User.builder().username("admin").password(passwordEncoder.encode("admin")).name("admin").phone("1234")
                .nationalId("1234").admin(true).active(true).build();
        User gobadianUser = User.builder().username("msghobadian").password(passwordEncoder.encode("123456789")).name("MohammadSadegh Ghobadian").phone("09031098319")
                .nationalId("123516879").student(ghobadian).active(true).build();
        User padashUser = User.builder().username("price").password(passwordEncoder.encode("1315469798")).name("Ali Padash").phone("46549787978")
                .nationalId("3146465987").student(padash).active(true).build();
        User zahediUser = User.builder().username("zahedi").password(passwordEncoder.encode("fasdjfhklhk")).name("Saeed Zahedi").phone("4649879877")
                .nationalId("1364797879").student(zahedi).active(true).build();
        User user3 = User.builder().username("john_doe").password(passwordEncoder.encode("123456789")).name("John Doe").phone("13897954654")
                .nationalId("1316464564").student(student2).active(true).build();
        User user4 = User.builder().username("no_name").password(passwordEncoder.encode("665489asfsaf")).name("Ali Bagheri").phone("54647981fsfasf")
                .nationalId("4689987987").student(student3).active(true).build();
        User user5 = User.builder().username("mesna").password(passwordEncoder.encode("twelve")).name("Mahdi Esnaashari").phone("453987863125")
                .nationalId("123165464987").instructor(esna).active(true).build();
        User user6 = User.builder().username("khadem").password(passwordEncoder.encode("khadempass")).name("Ali Khadem").phone("12345874546")
                .nationalId("465498798798").instructor(khadem).active(true).build();
        User user7 = User.builder().username("zkash").password(passwordEncoder.encode("faskdjjfhk")).name("Zahra Kashani").phone("621332178")
                .nationalId("132132464979").instructor(kashani).active(true).build();
        userService.create(user1);
        userService.create(gobadianUser);
        userService.create(padashUser);
        userService.create(zahediUser);
        userService.create(user3);
        userService.create(user4);
        userService.create(user5);
        userService.create(user6);
        userService.create(user7);
    }

    private void loadInstructors(InstructorService instructorService, Instructor instructor1, Instructor instructor2, Instructor esna, Instructor khadem, Instructor kashani) {
        instructorService.create(instructor1);
        instructorService.create(instructor2);
        instructorService.create(esna);
        instructorService.create(khadem);
        instructorService.create(kashani);
    }
}
