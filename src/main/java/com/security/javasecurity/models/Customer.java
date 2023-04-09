package com.security.javasecurity.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.security.javasecurity.views.Views;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
public class Customer implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Admin.class)
    private int id;

    @Column(unique = true)
//    @Pattern(regexp=".+@.+\\.[a-z]+", message="Invalid email address!")
    @NotNull
    @JsonView({Views.Admin.class,Views.User.class})
    private String email;

    @JsonView({Views.Admin.class,Views.User.class})
    @NotNull
    private String password;

    //default register USER role
    @JsonView(Views.Admin.class)
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Role> roles = Arrays.asList(Role.USER);

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        this.roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.name())));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
