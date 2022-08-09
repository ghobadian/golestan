package tech.sobhan.golestan.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.repositories.Repository;
import tech.sobhan.golestan.security.ErrorChecker;
import tech.sobhan.golestan.services.UserService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserSecurityService {
    private final ErrorChecker errorChecker;
    private final UserService service;
    private final Repository repository;

    public String list(String token) {
        errorChecker.checkIsUser(token);
        return service.list().toString();
    }

    public User create(String username, String password, String name, String phone, String nationalId) {
        errorChecker.checkPhoneNumber(phone);
        errorChecker.checkNationalId(nationalId);
        errorChecker.checkUserExists(username, phone, nationalId);
        return service.create(username, password, name, phone, nationalId);
    }

    public String read(Long id, String token) {
        errorChecker.checkIsUser(token);
        return service.read(id).toString();
    }

    public String update(String name, String newUsername, String newPassword, String phone, String token) {
        errorChecker.checkIsUser(token);
        String username = repository.findTokenByToken(token).getUsername();
        return service.update(name, newUsername, newPassword, phone, username).toString();
    }

    public void delete(Long id, String token) {
        errorChecker.checkIsAdmin(token);
        service.delete(id);
    }

    public String modifyRole(Long id, Map<String, String> requestedBody, String token) {
        errorChecker.checkIsAdmin(token);
        return service.modifyRole(id, requestedBody);
    }

    public String login(String username, String password) {//todo check security
        errorChecker.checkIsUser(username, password);
        errorChecker.checkTokenExistsByUsername(username);
        return service.saveAndSendToken(username);
    }

    public String logout(String token) {
        errorChecker.checkIsUser(token);
        service.logout(repository.findTokenByToken(token));
        return null;//todo
    }
}
