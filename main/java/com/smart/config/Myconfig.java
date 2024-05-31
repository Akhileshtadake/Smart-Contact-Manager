package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

import java.text.Normalizer.Form;

@Configuration
@EnableWebSecurity
public class Myconfig {

    @Bean
    UserDetailsService getUserDetailsService() {
    	return new UserDetailsServiceImpl();
    }
    
    @Bean
    static PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    
    
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
    	
    	DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    	
    	daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
    	daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    	
    	return daoAuthenticationProvider;
    }
    
    
    
    
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/user/**")
                        .authenticated()
                        .requestMatchers("/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(form -> form.loginPage("/signin"));
            return http.build();
            
            
                 
    }
    
    

    
    
    
    
}

