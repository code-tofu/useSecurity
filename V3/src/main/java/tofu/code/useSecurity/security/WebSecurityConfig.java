package tofu.code.useSecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import tofu.code.useSecurity.service.UserDetailsServiceImpl;
import static tofu.code.useSecurity.enums.Authority.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    JWTAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProviderDAO() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProviderDAO())
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                    //public
                    .requestMatchers("/").permitAll()
                    .requestMatchers("/except").permitAll()
                    .requestMatchers("/signin").permitAll()
                    //authorities implmentation
                    .requestMatchers("/v1/signup/newUser/**").permitAll()
                    .requestMatchers("/v1/signin").permitAll()
                    //role implementation
                    .requestMatchers("/v2/signup/newUser/**").permitAll()
                    //protected
                    .requestMatchers("/protected").hasAnyAuthority(String.valueOf(PROTECTED_AUTHORITY))
                    .requestMatchers("/write").hasAnyAuthority(String.valueOf(WRITE_AUTHORITY))
                    .requestMatchers("/delete").hasAnyAuthority(String.valueOf(DELETE_AUTHORITY))


//                    .requestMatchers("/signup/newUser/**").permitAll()

                    .anyRequest().authenticated())
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedHandler(new CustomAccessDeniedHandler()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }


}
