package com.imroze.twitterdemo.userauth.data.request;

import com.imroze.twitterdemo.userauth.data.RegistrationType;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class UserRegistrationData {
  @NonNull
  private String userName;
  @NonNull
  private String email;
  @NonNull
  private String name;
  @NonNull
    private String password;
  @NonNull
  private String number;
  private String recoveryEmail;
  private String profilePictureUrl;

  @NonNull
  private RegistrationType registrationType;
}
