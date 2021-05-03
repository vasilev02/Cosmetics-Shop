package com.example.shop.config;

import com.example.shop.security.DemoUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DemoUserDetailsService demoUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(DemoUserDetailsService demoUserDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.demoUserDetailsService = demoUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                userDetailsService(demoUserDetailsService).
                passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests().
                antMatchers("/css/**", "/img/**").permitAll().
                antMatchers("/", "/info", "/users/login", "/users/register").permitAll().
                antMatchers("/orders/**").hasAuthority("ROLE_ADMIN").
                antMatchers("/role/**").hasAuthority("ROLE_ROOT").
                antMatchers("/statistics").hasAuthority("ROLE_ROOT").
                antMatchers("/products/api").hasAuthority("ROLE_USER").
                antMatchers("/products/add").hasAuthority("ROLE_ADMIN").
                antMatchers("/shop/product/edit/{id}").hasAuthority("ROLE_ADMIN").
                antMatchers("/shop/product/delete/{id}").hasAuthority("ROLE_ADMIN").
                antMatchers("/shop/product/delete/comment/{id}").hasAuthority("ROLE_ADMIN").
                anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/users/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .failureForwardUrl("/users/login-error")
                .and().
                logout().
                logoutUrl("/logout").
                logoutSuccessUrl("/").
                invalidateHttpSession(true).
                deleteCookies("JSESSIONID");
    }

}