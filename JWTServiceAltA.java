package tofu.code.useSecurity.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;

import tofu.code.useSecurity.entity.UserDetailsImpl;

public class JWTServiceAltA {
        @Value("${jwt.secret.key}")
    private String secretKey;

    public String generateAccessToken(UserDetailsImpl user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withClaim("role", user.getRole().toString())
                    .withExpiresAt(genAccessExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Error while generating token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            DecodedJWT jwt= JWT.require(algorithm)
                    .build()
                    .verify(token);
            return jwt.getSubject();

        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Error while validating token", exception);
        }
    }

    private Instant genAccessExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
