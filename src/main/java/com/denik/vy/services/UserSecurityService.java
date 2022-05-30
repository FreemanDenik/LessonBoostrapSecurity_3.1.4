package com.denik.vy.services;

import com.denik.vy.models.Role;
import com.denik.vy.models.User;
import com.denik.vy.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("not find user: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities(user.getRoles()));
    }

    private List<? extends GrantedAuthority> grantedAuthorities(List<Role> roles) {
        return roles.stream().map(t -> new SimpleGrantedAuthority(t.getName())).collect(Collectors.toList());
    }
}
