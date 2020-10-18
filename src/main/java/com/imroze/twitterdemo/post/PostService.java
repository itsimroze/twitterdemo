package com.imroze.twitterdemo.post;

import com.imroze.twitterdemo.post.data.BasicPostRequest;
import com.imroze.twitterdemo.post.data.Comment;
import com.imroze.twitterdemo.post.data.Like;
import com.imroze.twitterdemo.post.data.Post;
import java.util.HashMap;
import reactor.core.publisher.Mono;

public interface PostService {

  Mono<Post> createPost(String username, BasicPostRequest basicPostRequest);

  Mono<String> likePost(String username, String postId, Like like);

  Mono<String> commentPost(String username, String postId, Comment comment);

  Mono<String> likeComment(String username, String postId, String commentId, Like like);

  Mono<Post> getPost(String postId);

  Mono<HashMap<String, Object>> getPostsForFollowingUser(String username, String followingUser, Long page);
}
