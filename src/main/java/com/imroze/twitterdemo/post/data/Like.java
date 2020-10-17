package com.imroze.twitterdemo.post.data;

import java.util.Date;
import lombok.Data;

@Data
public class Like {
  private String username;
  private ReactType reactType;
  private Date createdAt;
}
