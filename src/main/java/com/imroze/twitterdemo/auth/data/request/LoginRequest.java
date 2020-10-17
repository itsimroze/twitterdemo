package com.imroze.twitterdemo.auth.data.request;

import com.imroze.twitterdemo.auth.data.LoginType;
import lombok.Data;

@Data
public class LoginRequest {
  private String user;
  private LoginType loginType;
  private String password;
}
