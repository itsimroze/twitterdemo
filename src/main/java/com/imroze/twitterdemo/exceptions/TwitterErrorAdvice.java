package com.imroze.twitterdemo.exceptions;

import com.auth0.jwt.exceptions.JWTDecodeException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.nio.file.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class TwitterErrorAdvice {

  public TwitterErrorAdvice() {
    log.info("Initialized error advice.");
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({TwitterDemoNotFoundException.class, NotFoundException.class})
  public final ResponseEntity<TwitterDemoErrorDetails> handle(TwitterDemoNotFoundException e) {
    return new ResponseEntity<>(new TwitterDemoErrorDetails(e), e.getHttpStatus());
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({
    TwitterDemoUnauthorizedException.class,
    AccessDeniedException.class,
    JWTDecodeException.class,
    JwtException.class,
    ExpiredJwtException.class
  })
  public final ResponseEntity<TwitterDemoErrorDetails> handle(TwitterDemoUnauthorizedException e) {
    return new ResponseEntity<>(new TwitterDemoErrorDetails(e), e.getHttpStatus());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({
    TwitterDemoClientException.class,
    IllegalArgumentException.class,
    IllegalStateException.class
  })
  public final ResponseEntity<TwitterDemoErrorDetails> handle(TwitterDemoClientException e) {
    return new ResponseEntity<>(new TwitterDemoErrorDetails(e), e.getHttpStatus());
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({
    Exception.class,
    Throwable.class,
    TwitterDemoInternalException.class,
    TwitterDemoException.class,
    NullPointerException.class,
    RuntimeException.class
  })
  public final ResponseEntity<TwitterDemoErrorDetails> handle(Throwable ex) {

    return new ResponseEntity<>(
        new TwitterDemoErrorDetails(new TwitterDemoClientException(ex, ex.getMessage())),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
