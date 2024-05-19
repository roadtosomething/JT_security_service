package com.just_talk.security_service.security;

import com.just_talk.security_service.exception.AuthException;
import com.just_talk.security_service.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;

public class JWTHandler {

    private final String secret;

    public JWTHandler(String secret) {
        this.secret = secret;
    }

    private VerificationResult verify (String token){
        Claims claims = getClaimsFromToken(token);
        final Date expirationDate = claims.getExpiration();


        if (expirationDate.before(new Date())){
            throw new RuntimeException("Token expired");
        }

        return new VerificationResult(claims, token);
    }

    public Mono<VerificationResult> check (String accesstoken){
        return Mono.just(verify(accesstoken))
                .onErrorResume(e->Mono.error(new UnauthorizedException(e.getMessage())));
    }
    private Claims getClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }
    @AllArgsConstructor
    public static class VerificationResult {
        public Claims claims;
        public String token;
    }
}
