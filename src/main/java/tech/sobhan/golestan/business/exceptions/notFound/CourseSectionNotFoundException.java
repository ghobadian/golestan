package tech.sobhan.golestan.business.exceptions.notFound;

import tech.sobhan.golestan.models.CourseSection;

public class CourseSectionNotFoundException extends RuntimeException {
    public CourseSectionNotFoundException(){
        super("ERROR404 Course section not found.");
    }
}