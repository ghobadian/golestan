package tech.sobhan.golestan.auth;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Embeddable;
import java.util.Collection;
import java.util.Set;

@Embeddable
public class GrantedAuthorities {
    private final Set<? extends GrantedAuthority> grantedAuthorities;

    public GrantedAuthorities(Set<? extends GrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }


    public Collection getAuthorities() {
        return grantedAuthorities;
    }

    public GrantedAuthorities() {
        grantedAuthorities = null;
    }
}
