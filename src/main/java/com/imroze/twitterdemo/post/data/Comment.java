package com.imroze.twitterdemo.post.data;

import java.util.Date;
import lombok.Data;
import java.util.List;

@Data
public class Comment {
  private String username;
  private String text;
  private List<Like> likes;
  private Date createdDate;
}
