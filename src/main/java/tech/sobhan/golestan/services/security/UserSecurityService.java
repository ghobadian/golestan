package tech.sobhan.golestan.services.security;

import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.security.ErrorChecker;
import tech.sobhan.golestan.services.UserService;

import java.util.Map;

@Service
public class UserSecurityService {
    private final ErrorChecker errorChecker;
    private final UserService service;

    public UserSecurityService(ErrorChecker errorChecker, UserService service) {
        this.errorChecker = errorChecker;
        this.service = service;
    }

    public String list(String username, String password) {
        errorChecker.checkIsUser(username, password);
        return service.list().toString();
    }

    public User create(String username, String password, String name, String phone, String nationalId) {
        errorChecker.checkPhoneNumber(phone);
        errorChecker.checkNationalId(nationalId);
        errorChecker.checkUserExists(username, phone, nationalId);
        return service.create(username, password, name, phone, nationalId);
    }

    public String read(Long id, String username, String password) {
        errorChecker.checkIsUser(username, password);
        return service.read(id).toString();
    }

    public String update(String name, String newUsername, String newPassword, String phone,
                         String username, String password) {
        errorChecker.checkIsUser(username, password);
        return service.update(name, newUsername, newPassword, phone, username).toString();
    }

    public void delete(Long id, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        service.delete(id);
    }

    public String modifyRole(Long id, Map<String, String> requestedBody, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        return service.modifyRole(id, requestedBody);
    }
}
