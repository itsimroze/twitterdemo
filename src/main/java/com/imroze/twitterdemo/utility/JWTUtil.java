package com.imroze.twitterdemo.utility;

import com.imroze.twitterdemo.userauth.data.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil implements Serializable {

  private static final String secret =
      "ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave512bitsKeySize";
  public static final Long expirationTime = 2L; // 86400L; // in second

  public String generateToken(Role role, String userName, String email, String number) {

    Map<String, Object> claims = new HashMap<>();
    claims.put("role", role);
    claims.put("email", email);
    claims.put("number", number);

    return doGenerateToken(claims, userName);
  }

  public Boolean validateToken(String token) {
    return !isTokenExpired(token);
  }

  public String getUserNameFromToken(String token) {
    return getAllClaimsFromToken(token).getSubject();
  }

  public String getRole(String token) {
    return getAllClaimsFromToken(token).get("role").toString();
  }

  public String getEmail(String token) {
    return getAllClaimsFromToken(token).get("email").toString();
  }

  public String getNumber(String token) {
    return getAllClaimsFromToken(token).get("number").toString();
  }

  public Date getExpirationDateFromToken(String token) {
    return getAllClaimsFromToken(token).getExpiration();
  }

  private String doGenerateToken(Map<String, Object> claims, String userName) {

    final Date createdDate = new Date();
    final Date expirationDate = new Date(createdDate.getTime() + expirationTime * 1000);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userName)
        .setIssuedAt(createdDate)
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.getBytes()))
        .compact();
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser()
        .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
        .parseClaimsJws(token)
        .getBody();
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }
}
