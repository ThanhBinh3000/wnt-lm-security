package vn.com.gsoft.security.util.system;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {
    @Value("${jwt.token.validity}")
    private long validity;
    @Value("${jwt.refreshtoken.validity}")
    private long rtValidity;
    @Value("${jwt.secret}")
    private String secret;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we will need the secret key
    public Claims getAllClaimsFromToken(String token) {
        String encodedString = Base64.getEncoder().encodeToString(secret.getBytes());
        return Jwts.parser().setSigningKey(encodedString).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(String data) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "token");
        return doGenerateToken(claims, data, this.validity);
    }

    public String generateRefreshToken(String data) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refreshtoken");
        return doGenerateToken(claims, data, this.rtValidity);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, long validity) {
        String encodedString = Base64.getEncoder().encodeToString(secret.getBytes());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(SignatureAlgorithm.HS512, encodedString)
                .compact();
    }
}
