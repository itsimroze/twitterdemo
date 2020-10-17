package com.imroze.twitterdemo.auth.data;

import java.util.HashMap;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class UserData {
  @Id
  private String userName;
  @Indexed
  private String email;
  @Indexed
  private String number;
  private String name;
  private String password;
  private String recoveryEmail;
  private String profilePictureUrl;
  private HashMap<String, FollowStatus> followers = new HashMap<>();
  private SessionStatus sessionStatus;
}