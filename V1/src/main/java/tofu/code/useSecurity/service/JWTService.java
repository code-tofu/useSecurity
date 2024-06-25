package tofu.code.useSecurity.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import tofu.code.useSecurity.entity.UserDetailsImpl;

@Service
@Slf4j
public class JWTService {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration.ms}")
    private int expireTimeMs;


    public Optional<Claims> validateAndExtractToken(HttpServletRequest request){
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if(null == authHeader || !authHeader.startsWith(BEARER_PREFIX)){
            log.info("Jwt Token Does Not Exist");
            return Optional.empty();
        }
        String jwtToken = authHeader.substring(7);

        try {
            Claims jwtClaims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(jwtToken).getBody();

            if (null == jwtClaims.getSubject()) {
                log.info("Jwt Token Subject is null");
                return Optional.empty(); //validate subject
            }
            if (jwtClaims.getExpiration().before(new Date())) {
                log.info("Jwt Token is expired");
                return Optional.empty(); //validate expiry
            }
            return Optional.of(jwtClaims);
        } catch (Exception exception){
            log.info("JWT Parse Exception: {}", exception.getMessage());
        /*
        UnsupportedJwtException – if the claimsJws argument does not represent an Claims JWS
        MalformedJwtException – if the claimsJws string is not a valid JWS
        SignatureException – if the claimsJws JWS signature validation fails
        ExpiredJwtException – if the specified JWT is a Claims JWT and the Claims has an expiration time before the time this method is invoked.
        IllegalArgumentException – if the claimsJws string is null or empty or only whitespace
        */
        }
        return Optional.empty();
    }

    private Key getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetailsImpl user) {
        log.info("Generating Token for UserId:{}",user.getUserId());
        Claims claims = new DefaultClaims();
        claims.setSubject(user.getUsername());
        claims.putIfAbsent("role",user.getAuthorities().get(0).toString());
        return Jwts
                .builder()
                .setClaims(claims) //overrides setSubject, since setSubject is a convenience method
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs)) //slightly off?
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}
