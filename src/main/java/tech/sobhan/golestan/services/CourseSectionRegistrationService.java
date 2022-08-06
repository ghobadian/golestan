package tech.sobhan.golestan.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.repositories.Repository;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.List;

@Service
@Slf4j
public class CourseSectionRegistrationService {//todo what the heck find Usage
    private final Repository repository;
    private final ErrorChecker errorChecker;

    public CourseSectionRegistrationService(Repository repository, ErrorChecker errorChecker) {
        this.repository = repository;
        this.errorChecker = errorChecker;
    }

    public CourseSectionRegistration create(CourseSectionRegistration csr) {
        errorChecker.checkCourseSectionRegistrationExists(csr.getCourseSection(), csr.getStudent());
        return repository.saveCourseSectionRegistration(csr);
    }

    public List<CourseSectionRegistration> list() {
        return repository.findAllCourseSectionRegistrations();
    }
}
