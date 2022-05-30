package com.denik.vy.configs;

import com.denik.vy.models.Role;
import com.denik.vy.models.User;
import com.denik.vy.repositories.RoleRepository;
import com.denik.vy.repositories.UserRepository;
import com.denik.vy.services.RoleService;
import com.denik.vy.services.UserSecurityService;
import com.denik.vy.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserSecurityService userSecurityService;
    final SuccessUserHandler successUserHandler;

    public WebSecurityConfig( UserSecurityService userSecurityService, SuccessUserHandler successUserHandler) {

        this.userSecurityService = userSecurityService;

        this.successUserHandler = successUserHandler;
    }

    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable();
        httpSecurity.authorizeRequests()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(successUserHandler).permitAll()
                .and()
                .logout().permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userSecurityService);
        return provider;
    }
    @Bean
    String adminInit(UserRepository userRepository, RoleRepository roleRepository){
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        User admin = new User(
                "admin",
                passwordEncoder().encode("12345"),
                "hhaid@bk.ru",
                Arrays.asList(roleAdmin, roleUser));
        User user = new User(
                "user",
                passwordEncoder().encode("12345"),
                "user@bk.ru",
                Arrays.asList(roleUser));
        roleRepository.save(roleAdmin);
        roleRepository.save(roleUser);
        userRepository.save(admin);
        userRepository.save(user);
        return null;
    }
}
