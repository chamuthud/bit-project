package com.residential.construction_management.security.jwt;

import com.residential.construction_management.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret:DefaultSecretKeyMustBeLongerThan256BitsDefaultSecretKeyMustBeLongerThan256Bits}") // Load from properties or use default (change it!)
    private String jwtSecretString;

    @Value("${app.jwtExpirationMs:86400000}") // Default to 24 hours
    private int jwtExpirationMs;

    private SecretKey key() {
        // Decode the base64 secret string into bytes
        byte[] decodedKey = java.util.Base64.getDecoder().decode(jwtSecretString);
        // Ensure the key size is appropriate for HS512 (64 bytes / 512 bits recommended)
        return Keys.hmacShaKeyFor(decodedKey);
    }


    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS512) // Use the SecretKey object
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build() // Use the SecretKey object
                .parseClaimsJws(token).getBody().getSubject();
    }


    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken); // Use the SecretKey object
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (io.jsonwebtoken.security.SecurityException e) { // Specific exception for signature validation failure
            logger.error("Invalid JWT signature: {}", e.getMessage());
        }


        return false;
    }
}
