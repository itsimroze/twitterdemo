package com.imroze.twitterdemo.securityconfig;

import com.imroze.twitterdemo.exceptions.TwitterDemoUnauthorizedException;
import com.imroze.twitterdemo.auth.UserDataRepository;
import com.imroze.twitterdemo.auth.data.Role;
import com.imroze.twitterdemo.auth.data.SessionStatus;
import com.imroze.twitterdemo.utility.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

  @Autowired private JWTUtil jwtUtil;

  @Autowired private UserDataRepository userDataRepository;

  @Override
  @SuppressWarnings("unchecked")
  public Mono<Authentication> authenticate(Authentication authentication) {
    String authToken = authentication.getCredentials().toString();

    String userName;
    String role;
    String email;
    String number;

    try {

      try {
        userName = jwtUtil.getUserNameFromToken(authToken);
      } catch (Exception e) {
        userName = null;
      }

      try {
        role = jwtUtil.getRole(authToken);
      } catch (Exception e) {
        role = null;
      }

      try {
        email = jwtUtil.getEmail(authToken);
      } catch (Exception e) {
        email = null;
      }

      try {
        number = jwtUtil.getNumber(authToken);
      } catch (Exception e) {
        number = null;
      }

      if (number != null
          && role != null
          && userName != null
          && email != null
          && (role.equals(Role.ROLE_USER.name()))
          && jwtUtil.validateToken(authToken)) {

        List<Role> roles = new ArrayList<>();
        roles.add(Role.valueOf(role));
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(
                jwtUtil.getUserNameFromToken(authToken),
                null,
                roles.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.name()))
                    .collect(Collectors.toList()));

        return Mono.just(auth);

      } else {
        return Mono.error(
            () ->
                new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Access Denied!",
                    new TwitterDemoUnauthorizedException()));
      }
    } catch (ExpiredJwtException e) {

      return userDataRepository
          .findUserDataByEmail(e.getClaims().get("email").toString())
          .filter(userData -> userData.getSessionStatus().equals(SessionStatus.ACTIVE))
          .switchIfEmpty(
              Mono.error(
                  () ->
                      new ResponseStatusException(
                          HttpStatus.UNAUTHORIZED,
                          "Your session has been expired!",
                          new TwitterDemoUnauthorizedException())))
          .flatMap(
              userData -> {
                userData.setSessionStatus(SessionStatus.INACTIVE);
                return userDataRepository.save(userData);
              })
          .flatMap(
              userData ->
                  Mono.error(
                      () ->
                          new ResponseStatusException(
                              HttpStatus.UNAUTHORIZED,
                              "Your session has been expired!",
                              new TwitterDemoUnauthorizedException())));

    } catch (MalformedJwtException | SignatureException e) {
      return Mono.error(
          () ->
              new ResponseStatusException(
                  HttpStatus.UNAUTHORIZED,
                  "Invalid Token",
                  new TwitterDemoUnauthorizedException()));
    }
  }
}
