package tech.sobhan.golestan.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.security.ErrorChecker;

@Service
@RequiredArgsConstructor
public class UserSecurityService {
    private final ErrorChecker errorChecker;

    public void list(String token) {
        errorChecker.checkIsUser(token);
    }

    public void create(String username, String phone, String nationalId) {
        errorChecker.checkPhoneNumber(phone);
        errorChecker.checkNationalId(nationalId);
        errorChecker.checkUserExists(username, phone, nationalId);
    }

    public void read(String token) {
        errorChecker.checkIsUser(token);
    }

    public void update(String token) {
        errorChecker.checkIsUser(token);
    }

    public void delete(String token) {
        errorChecker.checkIsAdmin(token);
    }

    public void modifyRole(String token) {
        errorChecker.checkIsAdmin(token);
    }

    public void login(String username, String password) {
        errorChecker.checkIsUser(username, password);
        errorChecker.checkTokenExistsByUsername(username);
    }

    public void logout(String token) {
        errorChecker.checkIsUser(token);
    }
}
