package com.tma.vlhau.ecommercebackend.security;


import com.tma.vlhau.ecommercecommon.entity.Role;
import com.tma.vlhau.ecommercecommon.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class UserDetailsImp implements UserDetails {

    private static final long serialVersionUID = 2L;

    private User user;

    public UserDetailsImp(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        for (Role role : roles){
            authorityList.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorityList;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
        return user.isEnabled();
    }

    public String getFullName(){
        return "<span class=\"fas fa-user fa-1x icon-silver\"></span>" + this.user.getLastName() + " " + this.user.getFirstName();
    }

    public void setFirstName(String firstName){
        this.user.setFirstName(firstName);
    }

    public void setLastName(String lastName){
        this.user.setLastName(lastName);
    }

    public boolean hasRole(String roleName) {
        return this.user.hasRole(roleName);
    }

}
