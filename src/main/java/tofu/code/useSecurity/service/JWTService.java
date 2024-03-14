package tofu.code.useSecurity.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;


public class JWTService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration.ms}")
    private int expireTimeMs;

    /**
     *
     UnsupportedJwtException – if the claimsJws argument does not represent an Claims JWS
     MalformedJwtException – if the claimsJws string is not a valid JWS
     SignatureException – if the claimsJws JWS signature validation fails
     ExpiredJwtException – if the specified JWT is a Claims JWT and the Claims has an expiration time before the time this method is invoked.
     IllegalArgumentException – if the claimsJws string is null or empty or only whitespace
     * @param request
     * @return Optional<Claims>
     */
    public Optional<Claims> validateAndExtractToken(HttpServletRequest request){
        final String authHeader = request.getHeader("Authorization");
        if(null == authHeader || !authHeader.startsWith("Bearer ")){
            return Optional.empty();
        }
        String jwtToken = authHeader.substring(7);

        Claims jwtClaims = Jwts.parserBuilder()
                        .setSigningKey(getSecretKey())  //TODO: add more parser requirements here
                        .build()
                        .parseClaimsJws(jwtToken).getBody();
        if(null == jwtClaims.getSubject()) return Optional.empty();
        if(jwtClaims.getExpiration().before(new Date())) return Optional.empty();
        return Optional.of(jwtClaims);
    }

    private Key getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String subject, Map<String, Object> customClaims) {
        return Jwts
                .builder()
                .setClaims(customClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}
