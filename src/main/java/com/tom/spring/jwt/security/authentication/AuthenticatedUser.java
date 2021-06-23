package com.tom.spring.jwt.security.authentication;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AuthenticatedUser extends User {

    private Long id;


    public AuthenticatedUser(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities){
        super(username, password, authorities);
        this.id = id;
    }

    public AuthenticatedUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public static AuthenticatedUser of(com.tom.spring.jwt.security.entity.User user){

        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());

        return new AuthenticatedUser(user.getId(),user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
