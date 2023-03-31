package com.lib.security;


import com.lib.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /// Amacimiz : Encoder., Provider, AuthTokenFilter gibi yapilari olusturmak...

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().
                authorizeRequests().antMatchers(HttpMethod.OPTIONS,"/**").permitAll().and().
                authorizeRequests().
                antMatchers("/login",
                        "/signin",
                        "/register",
                        "/index.html",
                        "/",
                        "/files/download/**",
                        "/files/display/**",
                        "/user/resetpassword/**",
                        "/publishers/visitors/**",
                        "/authors/visitors/**",
                        "/categories/visitors/**",
                        "/books/visitors/**",
                        "/actuator/info",
                        "/actuator/health").permitAll().
                anyRequest().authenticated();

       http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Encoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    // provider
    @Bean
    public DaoAuthenticationProvider authProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    // AuthenticationMenager

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception{
        return http.
                getSharedObject(AuthenticationManagerBuilder.class).
                authenticationProvider(authProvider()).
                build();
    }


    // AuthTokenFilter
    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }



}
