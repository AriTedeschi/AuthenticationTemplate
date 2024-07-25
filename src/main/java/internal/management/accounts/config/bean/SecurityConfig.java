package internal.management.accounts.config.bean;

import internal.management.accounts.config.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] WHITELIST_SWAGGER = {"/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html","/h2-console/**","/favicon.ico"};
    private static final String[] WHITELIST_REGISTER_AUTH = {"/users/register","/login"};
    private static final String[] BLACKLIST_ENDPOINTS = {"/users","/roles","/roles/*"};
    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->{
                    auth.requestMatchers(HttpMethod.GET,BLACKLIST_ENDPOINTS).authenticated();
                    auth.requestMatchers(HttpMethod.POST,BLACKLIST_ENDPOINTS).authenticated();
                    auth.requestMatchers(HttpMethod.POST,WHITELIST_REGISTER_AUTH).permitAll();
                    auth.requestMatchers(WHITELIST_SWAGGER).permitAll();
                    auth.requestMatchers("/error").permitAll();
                    auth.anyRequest().authenticated();
                })
                .headers(headers -> headers.frameOptions().sameOrigin())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
