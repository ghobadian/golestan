package tech.sobhan.golestan.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final CustomUserDAO customUserDAO;

    @Autowired
    public CustomUserDetailsService(@Qualifier("fake") CustomUserDAO customUserDAO) {
        this.customUserDAO = customUserDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customUserDAO.selectCustomUserDetailsByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username "+username+"Not found"));
    }

}