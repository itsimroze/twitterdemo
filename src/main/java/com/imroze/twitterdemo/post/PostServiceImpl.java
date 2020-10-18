package com.imroze.twitterdemo.post;

import com.imroze.twitterdemo.auth.UserDataRepository;
import com.imroze.twitterdemo.exceptions.TwitterDemoClientException;
import com.imroze.twitterdemo.exceptions.TwitterDemoNotFoundException;
import com.imroze.twitterdemo.post.data.BasicPostRequest;
import com.imroze.twitterdemo.post.data.Comment;
import com.imroze.twitterdemo.post.data.Like;
import com.imroze.twitterdemo.post.data.Post;
import com.imroze.twitterdemo.stream.PostCappedRepository;
import com.imroze.twitterdemo.stream.data.PostCapped;
import com.imroze.twitterdemo.utility.CommonUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostServiceImpl implements PostService {

  @Autowired private PostRepository postRepository;

  @Autowired private PostCappedRepository postCappedRepository;

  @Autowired private UserDataRepository userDataRepository;

  @Override
  public Mono<Post> createPost(String username, BasicPostRequest basicPostRequest) {

    if (basicPostRequest.getText().length() > 140) {
      return Mono.error(
          () ->
              new TwitterDemoClientException(
                  new IllegalStateException(), "Post is out of text limit."));
    }

    return postRepository
        .save(
            Post.builder()
                .username(username)
                .createdAt(new Date())
                .location(basicPostRequest.getLocation())
                .postId(CommonUtils.generateUUID())
                .text(basicPostRequest.getText())
                .comments(new ArrayList<>())
                .likes(new ArrayList<>())
                .build())
        .flatMap(
            post ->
                postCappedRepository
                    .save(
                        PostCapped.builder()
                            .username(post.getUsername())
                            .createdAt(post.getCreatedAt())
                            .location(post.getLocation())
                            .postId(post.getPostId())
                            .text(basicPostRequest.getText())
                            .comments(new ArrayList<>())
                            .likes(new ArrayList<>())
                            .build())
                    .map(postCapped -> post));
  }

  @Override
  public Mono<String> likePost(String username, String postId, Like like) {

    like.setCreatedAt(new Date());
    like.setUsername(username);

    return postRepository
        .findById(postId)
        .switchIfEmpty(
            Mono.error(
                new TwitterDemoNotFoundException(
                    new RuntimeException(), "Post might have been deleted")))
        .flatMap(
            post -> {
              post.getLikes().add(like);
              return postRepository.save(post).map(post1 -> like.getReactType().name());
            });
  }

  @Override
  public Mono<String> commentPost(String username, String postId, Comment comment) {

    comment.setCommentId(CommonUtils.generateUUID());
    comment.setCreatedDate(new Date());
    comment.setLikes(new ArrayList<>());
    comment.setUsername(username);

    return postRepository
        .findById(postId)
        .switchIfEmpty(
            Mono.error(
                new TwitterDemoNotFoundException(
                    new RuntimeException(), "Post might have been deleted")))
        .flatMap(
            post -> {
              post.getComments().add(comment);
              return postRepository.save(post).map(post1 -> comment.getText());
            });
  }

  @Override
  public Mono<String> likeComment(String username, String postId, String commentId, Like like) {
    like.setCreatedAt(new Date());
    like.setUsername(username);

    return postRepository
        .findById(postId)
        .switchIfEmpty(
            Mono.error(
                new TwitterDemoNotFoundException(
                    new RuntimeException(), "Post might have been deleted")))
        .flatMap(
            post ->
                Flux.fromIterable(post.getComments())
                    .filter(comment -> comment.getCommentId().equals(commentId))
                    .take(1)
                    .next()
                    .flatMap(
                        comment -> {
                          comment.getLikes().add(like);
                          return postRepository.save(post).map(post1 -> like.getReactType().name());
                        }));
  }

  @Override
  public Mono<Post> getPost(String postId) {
    return postRepository
        .findById(postId)
        .switchIfEmpty(
            Mono.error(
                new TwitterDemoNotFoundException(new RuntimeException(), "Post not found!")));
  }

  @Override
  public Mono<HashMap<String, Object>> getPostsForFollowingUser(
      String username, String followingUser, Long page) {
    return userDataRepository
        .findById(followingUser)
        .flatMap(
            userData ->
                postRepository
                    .countByUsername(followingUser)
                    .flatMap(
                        aLong ->
                            postRepository
                                .findByUsername(followingUser)
                                .sort(Comparator.comparing(Post::getCreatedAt).reversed())
                                .skip((page - 1) * 20)
                                .take(20)
                                .collectList()
                                .map(
                                    posts -> {
                                      HashMap<String, Object> response = new HashMap<>();
                                      response.put(
                                          "totalPage", (aLong % 20) == 0 ? aLong / 20 : aLong/20 + 1);
                                      response.put("data", posts);
                                      response.put("currentPage", page);
                                      return response;
                                    })));
  }
}
