package tofu.code.useSecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import tofu.code.useSecurity.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired JWTAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProviderDAO() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService((UserDetailsService) userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean //TODO CONFIGURE ANT MATCHERS
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) 
                .authenticationProvider(authenticationProviderDAO())
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                // .authorizeHttpRequests(authorize -> authorize
                //     .requestMatchers( "/api/signin").permitAll()
                //     .requestMatchers( "/api/visitor/signup").permitAll()
                //     .requestMatchers( "/test/public").permitAll() //TODO: REMOVE ME
                //     .requestMatchers( "/test/signup").permitAll() //TODO: REMOVE ME
                //     .requestMatchers( "/test/user").authenticated() //TODO: REMOVE ME
                //     .requestMatchers("/test/admin").hasAnyRole(String.valueOf(ADMIN)) //TODO: REMOVE ME
                //     .requestMatchers("/test/visitor").hasAnyRole(String.valueOf(VISITOR)) //TODO: REMOVE ME
                //     .requestMatchers("/test/staff").hasAnyRole(String.valueOf(STAFF)) //TODO: REMOVE ME
                //     .requestMatchers( "/").permitAll() //TODO: TO CHANGE TO AUTHENTICATED
                //     .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) ;

        return http.build();
    }


}
