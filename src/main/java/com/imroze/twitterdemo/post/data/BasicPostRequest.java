package com.imroze.twitterdemo.post.data;

import lombok.Data;

@Data
public class BasicPostRequest {
  private String text;
  private String location;
}
