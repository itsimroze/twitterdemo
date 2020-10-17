package com.imroze.twitterdemo.userauth.data.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
  private String token;
}