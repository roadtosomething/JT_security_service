package com.just_talk.security_service.security;

import com.just_talk.security_service.entity.UserEntity;
import com.just_talk.security_service.exception.AuthException;
import com.just_talk.security_service.repository.UserRepository;
import com.just_talk.security_service.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expirationInSeconds;
    @Value("${jwt.issuer}")
    private String issuer;

    private TokenDetail generateToken (UserEntity user){
        Map<String, Object> claims = new HashMap<>() {{
            put("role", user.getRole());
            put("username", user.getUsername());

        }};
        return generateToken(claims,user.getId().toString());
    }

    private TokenDetail generateToken(Map<String, Object> claims, String subject){
        Long expirationTimeInMillis = expirationInSeconds * 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);

        return generateToken(expirationDate, claims,subject);
    }
    private TokenDetail generateToken (Date expirationDate, Map<String, Object> claims, String subject) {
        Date createdDate = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();
        return TokenDetail.builder()
                .token(token)
                .issuedAt(createdDate)
                .expiresAt(expirationDate)
                .build();
    }
    public Mono<TokenDetail> authenticate (String username, String password){
        return userService.getUserbyUsername(username)
                .flatMap(user ->{

                    if (!user.isEnabled()){
                        return Mono.error(new AuthException("Account disabled", "JTSECURITY_DISABLED_USER"));
                    }

                    if (!passwordEncoder.matches(password, user.getPassword())){
                        return Mono.error(new AuthException("Invalid password", "JTSECURITY_INVALID_PASSWORD"));
                    }

                    return Mono.just(generateToken(user).toBuilder()
                                    .userId(user.getId())
                            .build());
                })
                .switchIfEmpty(Mono.error(new AuthException("Auth information is not pass", "JTSECURITY_AUTH_EXCEPTION")));
    }
}
