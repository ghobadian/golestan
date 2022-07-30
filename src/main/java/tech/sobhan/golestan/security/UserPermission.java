package tech.sobhan.golestan.security;

import lombok.Getter;

public enum UserPermission {
    MODIFY_ROLE("modifyRole"),
    CREATE_INSTRUCTOR("createInstructor"),
    UPDATE_INSTRUCTOR("updateInstructor"),
    DELETE_INSTRUCTOR("deleteInstructor"),
    CREATE_TERM("createTerm"),
    UPDATE_TERM("updateTerm"),
    DELETE_TERM("deleteTerm"),
    CREATE_COURSE("createCourse"),
    UPDATE_COURSE("updateCourse"),
    DELETE_COURSE("deleteCourse"),
    LIST_STUDENTS_OF_COURSE_SECTION("listStudentsOfCourseSection"),
    CREATE_COURSE_SECTION("createCourseSection"),
    UPDATE_COURSE_SECTION("updateCourseSection"),
    DELETE_COURSE_SECTION("deleteCourseSection"),
    REGISTER_COURSE_SECTION("registerCourseSection"),
    GIVE_MARKS("giveMarks"),
    SEE_SCORES_TERM("seeScoresTerm"),
    SEE_SUMMERY("seeSummery");

    @Getter
    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

}
