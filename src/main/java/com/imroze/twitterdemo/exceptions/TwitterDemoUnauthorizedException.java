package com.imroze.twitterdemo.exceptions;

import org.springframework.http.HttpStatus;

public class TwitterDemoUnauthorizedException extends TwitterDemoException {
  private String message;

  public TwitterDemoUnauthorizedException() {
    message = "Access is not Denied.";
  }

  public TwitterDemoUnauthorizedException(String message) {
    this.message = message;
  }

  public TwitterDemoUnauthorizedException(Throwable throwable, String message) {
    this.message = message;

    throwable.printStackTrace();
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.UNAUTHORIZED;
  }
}
