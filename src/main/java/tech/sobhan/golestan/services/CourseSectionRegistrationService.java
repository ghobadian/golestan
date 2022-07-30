package tech.sobhan.golestan.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.business.exceptions.CourseSectionRegistrationNotFoundException;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.repositories.CourseSectionRegistrationRepository;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.createLog;

@Service
@Slf4j
public class CourseSectionRegistrationService {
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;

    public CourseSectionRegistrationService(CourseSectionRegistrationRepository courseSectionRegistrationRepository) {
        this.courseSectionRegistrationRepository = courseSectionRegistrationRepository;
    }

    public CourseSectionRegistration create(CourseSectionRegistration courseSectionRegistration) {
        if(courseSectionRegistrationExists(list(), courseSectionRegistration)) return null;
        createLog(CourseSectionRegistration.class, courseSectionRegistration.getId());
        return courseSectionRegistrationRepository.save(courseSectionRegistration);
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
        List<CourseSectionRegistration> allCourseSectionRegistrations = courseSectionRegistrationRepository.findAll();
//        if(allCourseSectionRegistrations.isEmpty()){
//            throw new CourseSectionRegistrationNotFoundException();
//        }
        return allCourseSectionRegistrations;
    }
}
