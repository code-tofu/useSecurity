package tofu.code.useSecurity.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tofu.code.useSecurity.service.JWTService;
import tofu.code.useSecurity.service.UserDetailsServiceImpl;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    UserDetailsServiceImpl userDetailsSvc;

    @Autowired
    JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Optional<Claims> jwtClaims = jwtService.validateAndExtractToken(request);
        if (jwtClaims.isEmpty()) {
            log.info("JWT Claims is Empty");
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = jwtClaims.get(); //CHECK OTHER CLAIMS IF THEY ARE ADDED
        UserDetails userDetails = userDetailsSvc.loadUserByUsername(claims.getSubject());
        if (null == userDetails) {
            log.info("User Details is Null");
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null,userDetails.getAuthorities() );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

}
