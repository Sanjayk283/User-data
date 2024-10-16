package com.UserRegistration.config;

import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.UserRegistration.config.InvalidTokenException;
import com.UserRegistration.modal.UserInfo;
import com.UserRegistration.Repo.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 8 * 60 * 60;

    String secret = "Your32ByteLongSecretKeyForHS256!!";  // Ensure it is 32+ characters

    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    
    @Autowired
    private UserRepository userLoginRepo;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails, HttpServletRequest request) {
        Claims claims = this.getAllClaimsFromToken(token);
        final String username = getUsernameFromToken(token);
        String tokenUserAgent = claims.get("userAgent", String.class);
        String tokenClientIp = claims.get("clientIp", String.class);

        String currentUserAgent = request.getHeader("User-Agent");
        String currentClientIp = request.getRemoteAddr();
        Integer tokenVersion = claims.get("tokenVersion", Integer.class);

        if (!username.equals(userDetails.getUsername())) {
            throw new InvalidTokenException("Invalid username in token.");
        }


        if (isTokenExpired(token)) {
            throw new InvalidTokenException("Token has expired.");
        }

        if (!tokenUserAgent.equals(currentUserAgent)) {
            throw new InvalidTokenException("Token version is invalid.");
        }
        if (!tokenClientIp.equals(currentClientIp)) {
            throw new InvalidTokenException("Token version is invalid.");
        }

        return true;

    }

    public String generateToken(UserInfo user, HttpServletRequest request) {
        Map<String, Object> claims = new HashMap<>();
        String userAgent = request.getHeader("User-Agent");
        String clientIp = request.getRemoteAddr();
        claims.put("userAgent", userAgent);
        claims.put("clientIp", clientIp);
        claims.put("payload", convertUserToMap(user));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUserName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(key)
                .compact();
    }

    private Map<String, Object> convertUserToMap(UserInfo user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userName", user.getUserName());
        userMap.put("id", user.getId());
        return userMap;
    }
    
}