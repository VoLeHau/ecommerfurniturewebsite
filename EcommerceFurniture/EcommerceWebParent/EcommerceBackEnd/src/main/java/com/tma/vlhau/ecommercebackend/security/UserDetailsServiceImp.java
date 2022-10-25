package com.tma.vlhau.ecommercebackend.security;

import com.tma.vlhau.ecommercebackend.user.repository.UserRepository;
import com.tma.vlhau.ecommercecommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if (user != null){
            return new UserDetailsImp(user);
        }

        throw new UsernameNotFoundException("Could not find user with email: " + email);
    }
}
