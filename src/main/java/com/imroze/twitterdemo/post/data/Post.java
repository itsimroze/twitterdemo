package com.imroze.twitterdemo.post.data;

import java.util.ArrayList;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Builder
@Document
public class Post {
  @Id
  private String postId;
  @Indexed
  private String username;
  private String text;
  private String location;
  private Date createdAt;
  private List<Like> likes = new ArrayList<>();
  private List<Comment> comments = new ArrayList<>();
}
