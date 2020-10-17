package com.imroze.twitterdemo.post;

import com.imroze.twitterdemo.post.data.BasicPostRequest;
import com.imroze.twitterdemo.post.data.Comment;
import com.imroze.twitterdemo.post.data.Like;
import com.imroze.twitterdemo.post.data.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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

  @PutMapping("/{username}/post/{postId}/like")
  public Mono<String> likePost(
      @PathVariable("username") String username,
      @PathVariable("postId") String postId,
      @RequestBody Like like) {
    return postService.likePost(username, postId, like);
  }

  @PutMapping("/{username}/post/{postId}/comment")
  public Mono<String> likePost(
      @PathVariable("username") String username,
      @PathVariable("postId") String postId,
      @RequestBody Comment comment) {
    return postService.commentPost(username, postId, comment);
  }

  @PutMapping("/{username}/post/{postId}/comment/{commentId}/like")
  public Mono<String> likeComment(
      @PathVariable("username") String username,
      @PathVariable("postId") String postId,
      @PathVariable("commentId") String commentId,
      @RequestBody Like like) {
    return postService.likeComment(username, postId, commentId, like);
  }

  @GetMapping("/{username}/post/{postId}")
  public Mono<Post> getPost(@PathVariable("postId") String postId) {
    return postService.getPost(postId);
  }
}
