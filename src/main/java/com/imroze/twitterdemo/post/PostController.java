package com.imroze.twitterdemo.post;

import com.imroze.twitterdemo.post.data.BasicPostRequest;
import com.imroze.twitterdemo.post.data.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/demo/v1.0/user")
public class PostController {

  @Autowired private PostService postService;

  @PutMapping("/{username}/post")
  @PreAuthorize("hasRole('USER')")
  public Mono<Post> createPost(
      @PathVariable("username") String username,
      @NonNull @RequestBody BasicPostRequest basicPostRequest) {
    return postService.createPost(username, basicPostRequest);
  }
}
