package com.imroze.twitterdemo.userauth.data.request;

import com.imroze.twitterdemo.userauth.data.LoginType;
import lombok.Data;

@Data
public class LoginRequest {
  private String user;
  private LoginType loginType;
  private String password;
}
