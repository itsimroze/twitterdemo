package com.imroze.twitterdemo.exceptions;

import org.springframework.http.HttpStatus;

public class TwitterDemoNotFoundException extends TwitterDemoException {
  public TwitterDemoNotFoundException(Throwable throwable, String message) {
    super(throwable, message);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
