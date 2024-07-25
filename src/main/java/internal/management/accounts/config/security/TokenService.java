package internal.management.accounts.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.JWTCreator.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import internal.management.accounts.domain.model.UserAuthenticated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(UserAuthenticated user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Builder tokenBuilder  = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getUsername())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(generateExpiration());
            tokenBuilder.withClaim("userId", (String) user.getProperties().getUuid().toString());
            tokenBuilder.withClaim("roleId", (String) user.getProperties().getUserCode().getAccountInfix());
            return tokenBuilder.sign(algorithm);
        } catch (JWTCreationException ex) {
            throw new RuntimeException("Error while generating token",ex);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException ex) {
            return "";
        }
    }

    public String getUserId(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token);
            return jwt.getClaim("userId").asString();
        } catch (JWTVerificationException ex) {
            return "";
        }
    }

    public String getRole(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token);
            String roleId = jwt.getClaim("roleId").asString();
            roleId = (roleId == "") ? "0" : roleId;
            return roleId;
        } catch (JWTVerificationException ex) {
            return "";
        }
    }

    private Instant generateExpiration(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}