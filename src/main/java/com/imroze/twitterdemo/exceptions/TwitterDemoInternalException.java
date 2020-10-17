package com.imroze.twitterdemo.exceptions;

import org.springframework.http.HttpStatus;

public class TwitterDemoInternalException extends TwitterDemoException{
  public TwitterDemoInternalException(Throwable throwable, String message) {
    super(throwable, message);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
