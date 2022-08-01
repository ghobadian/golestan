package tech.sobhan.golestan.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.repositories.RepositoryHandler;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.createLog;

@Service
@Slf4j
public class CourseSectionRegistrationService {
    private final RepositoryHandler repositoryHandler;

    public CourseSectionRegistrationService(RepositoryHandler repositoryHandler) {
        this.repositoryHandler = repositoryHandler;
    }

    public CourseSectionRegistration create(CourseSectionRegistration courseSectionRegistration) {
        if(courseSectionRegistrationExists(list(), courseSectionRegistration)) return null;
        createLog(CourseSectionRegistration.class, courseSectionRegistration.getId());
        return repositoryHandler.saveCourseSectionRegistration(courseSectionRegistration);
    }

    private boolean courseSectionRegistrationExists(List<CourseSectionRegistration> allCourseSectionRegistrations,
                                                    CourseSectionRegistration courseSectionRegistration) {
        for (CourseSectionRegistration csr : allCourseSectionRegistrations) {
            if(courseSectionRegistration.equals(csr)){
                System.out.println("ERROR403 duplicate CourseSectionRegistrations");
                return true;
            }
        }
        return false;
    }

    public List<CourseSectionRegistration> list() {
        return repositoryHandler.findAllCourseSectionRegistrations();
    }
}
