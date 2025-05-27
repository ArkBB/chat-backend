package com.example.chatserver.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final String secretKey;
    private final long tokenExpirationTime;
    private  Key SECRET_KEY;

    public JwtTokenProvider(@Value("${jwt.secretKey}")String secretKey, @Value("${jwt.expiration}")long tokenExpirationTime) {
        this.secretKey = secretKey;
        this.tokenExpirationTime = tokenExpirationTime;
        SetSecretKey(secretKey);
    }

    public String createToken(String email, String role){
        Claims claims = Jwts.claims().setSubject(email);
        // 주체 식별을 위해 이메일 이용
        claims.put("role",role);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenExpirationTime*60*1000L))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();

        return token;
    }

    private void SetSecretKey(String secretKey) {
        this.SECRET_KEY = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS512.getJcaName());
    }
}
