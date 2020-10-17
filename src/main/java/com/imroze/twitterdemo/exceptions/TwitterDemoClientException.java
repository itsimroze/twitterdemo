package com.imroze.twitterdemo.exceptions;

import org.springframework.http.HttpStatus;

public class TwitterDemoClientException extends TwitterDemoException {

  public TwitterDemoClientException(Throwable throwable, String message) {
    super(throwable, message);
  }

  public TwitterDemoClientException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }
}
