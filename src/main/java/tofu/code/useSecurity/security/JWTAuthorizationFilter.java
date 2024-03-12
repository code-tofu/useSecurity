package tofu.code.useSecurity.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class JWTAuthorizationFilter implements OncePerRequestFilter {

    @Autowired
    UserDetailsServiceImpl userDetailsSvc;

    @Autowired
    JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Optional<Claims> jwtClaims = jwtService.validateAndExtractToken(request);
        if (jwtClaims.isEmpty()) {
            filterChain.doFilter(request, response);
        }

        Claims claims = jwtClaims.get();
        UserDetails userDetails = userDetailsSvc.loadUserByUsername(claims.getSubject());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null,userDetails.getAuthorities() );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    }
}
