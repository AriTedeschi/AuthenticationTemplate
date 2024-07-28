package internal.management.accounts.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.JWTCreator.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import internal.management.accounts.application.outbound.response.LoginResponse;
import internal.management.accounts.config.exception.TokenExpiredException;
import internal.management.accounts.domain.model.UserAuthenticated;
import internal.management.accounts.domain.model.UserEntity;
import internal.management.accounts.domain.repository.UserRepository;
import internal.management.accounts.domain.service.outbound.factory.UserLookupFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;
    private final UserRepository userRepository;

    public TokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse generateToken(UserAuthenticated user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Builder tokenBuilder  = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getUsername())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(generateExpiration());
            UserEntity userEntity = user.getProperties();
            tokenBuilder.withClaim("userId", (String) userEntity.getUuid().toString());
            tokenBuilder.withClaim("roleId", (String) userEntity.getUserCode().getAccountInfix());
            tokenBuilder.withClaim("version", userEntity.getTokenVersion());
            String token = tokenBuilder.sign(algorithm);
            String warning = userEntity.isEffectivePassword() ? null : "The provided password is temporary, please change password!";
            return new LoginResponse(token,warning);
        } catch (JWTCreationException ex) {
            throw new RuntimeException("Error while generating token",ex);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT jwt =  JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token);
            String userId = jwt.getClaim("userId").asString();
            int tokenVersion = jwt.getClaim("version").asInt();

            UserEntity user = UserLookupFactory.getBy(userId, userRepository);

            if (user.getTokenVersion() != tokenVersion)
                throw new TokenExpiredException("Expired token, please login again");

            if (user.getTokenVersion() != tokenVersion)
                throw new TokenExpiredException("Expired password, please change password again at /password");

            return jwt.getSubject();
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