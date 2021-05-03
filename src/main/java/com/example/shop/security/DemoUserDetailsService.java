package com.example.shop.security;

import com.example.shop.model.entity.User;
import com.example.shop.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DemoUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DemoUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User with name " + username + " was not found."));

        return mapToUserDetails(user);
    }

    private UserDetails mapToUserDetails(User user) {

        List<SimpleGrantedAuthority> authorities = user.
                getRoles().
                stream().
                map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRole())).
                collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
