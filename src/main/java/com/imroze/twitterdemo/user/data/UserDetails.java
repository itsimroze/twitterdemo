package com.imroze.twitterdemo.user.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetails {
  private String username;
  private String email;
  private String number;
  private String name;
  private String recoveryEmail;
  private String profilePictureUrl;
}
