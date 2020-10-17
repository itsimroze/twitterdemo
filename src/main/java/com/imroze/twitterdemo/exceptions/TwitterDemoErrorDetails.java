package com.imroze.twitterdemo.exceptions;

import java.time.Instant;
import java.util.Date;
import lombok.Data;

@Data
public class TwitterDemoErrorDetails {
  private Date date;
  private String message;
  private String details;

  public TwitterDemoErrorDetails(TwitterDemoException exception) {
    this.date = Date.from(Instant.now());
    this.message = exception.getMessage();
    this.details = exception.getDetails();
  }
}