package com.ibji.app.security;

import com.ibji.app.entity.Membro;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@Getter
public class SecurityUser implements UserDetails {

    private final Membro membro;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + membro.getNivelAcesso().name())
        );
    }

    @Override
    public String getPassword() {
        // Hash BCrypt da senha "admin123"
        return "$2b$12$fOXXVOKbuHqDXFgDqtqU9uzhxf6dT8jWlKBP79cbVAQnndA7j3avu";
    }

    @Override
    public String getUsername() {
        return membro.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return membro.getStatus().name().equals("ATIVO");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return membro.getStatus().name().equals("ATIVO");
    }
}
