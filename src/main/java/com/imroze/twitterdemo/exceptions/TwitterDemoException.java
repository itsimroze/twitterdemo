package com.imroze.twitterdemo.exceptions;

import org.springframework.http.HttpStatus;

public class TwitterDemoException extends RuntimeException {

  private String message = "Wrong username/password.";

  public TwitterDemoException() {

  }

  public TwitterDemoException(String message) {
    this.message = message;
  }

  public TwitterDemoException(Throwable throwable, String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public HttpStatus getHttpStatus() {
    return HttpStatus.UNPROCESSABLE_ENTITY;
  }

  public String getDetails() {
    return "This is a generic error description";
  }
}