package tech.sobhan.golestan.security;


import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static tech.sobhan.golestan.security.UserPermission.*;

@AllArgsConstructor
public enum Role {
    STUDENT(Sets.newHashSet()),
    INSTRUCTOR(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(CREATE_INSTRUCTOR,UPDATE_INSTRUCTOR, DELETE_INSTRUCTOR, CREATE_TERM, UPDATE_TERM, DELETE_TERM,
            CREATE_COURSE, UPDATE_COURSE, DELETE_COURSE, UPDATE_COURSE_SECTION, DELETE_COURSE_SECTION));

    @Getter private final Set<UserPermission> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
