package com.imroze.twitterdemo.stream.data;

import com.imroze.twitterdemo.post.data.Comment;
import com.imroze.twitterdemo.post.data.Like;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class PostCapped {
  @Id
  private String postId;
  private String username;
  private String text;
  private String location;
  private Date createdAt;
  private List<Like> likes = new ArrayList<>();
  private List<Comment> comments = new ArrayList<>();
}