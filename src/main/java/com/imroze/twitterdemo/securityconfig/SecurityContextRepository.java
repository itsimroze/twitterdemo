package com.imroze.twitterdemo.securityconfig;

import com.imroze.twitterdemo.exceptions.TwitterDemoUnauthorizedException;
import com.imroze.twitterdemo.utility.JWTUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private JWTUtil jwtUtil;

  @Override
  public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
    throw new TwitterDemoUnauthorizedException("Un-Authorized!");
  }

  @Override
  public Mono<SecurityContext> load(ServerWebExchange swe) {

    ServerHttpRequest request = swe.getRequest();
    String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    PathPattern pathPattern = new PathPatternParser().parse("/demo/v1.0/user");

    PathPattern.PathRemainingMatchInfo pathRemainingMatchInfo =
        pathPattern.matchStartOfPath(request.getPath().pathWithinApplication());

    if (authHeader != null) {

      if (pathRemainingMatchInfo != null
          && pathRemainingMatchInfo.getPathRemaining() != null
          && !pathRemainingMatchInfo.getPathRemaining().value().isEmpty()) {

        String userName = pathRemainingMatchInfo.getPathRemaining().subPath(1, 2).value();

        if (!isAuthenticated(authHeader, userName)) {
          swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
          return Mono.error(
              () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Session!"));
        }
        else {
          Authentication auth = new UsernamePasswordAuthenticationToken(authHeader, authHeader);
          return this.authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
        }
      }
      else {
        return Mono.error(TwitterDemoUnauthorizedException::new);
      }

    } else {
      return Mono.error(
          () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please provide Token!"));
    }
  }

  private boolean isAuthenticated(String authToken, String pathUserName) {

    String userName = null;

    try {
      userName = jwtUtil.getUserNameFromToken(authToken);
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (userName.equals(pathUserName)) {
      return true;
    } else {
      return false;
    }
  }
}
