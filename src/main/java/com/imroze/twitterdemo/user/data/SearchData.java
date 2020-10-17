package com.imroze.twitterdemo.user.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchData {
  private String username;
  private String name;
  private String profilePictureUrl;
}
