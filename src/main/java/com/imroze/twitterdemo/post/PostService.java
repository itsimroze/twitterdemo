package com.imroze.twitterdemo.post;

import com.imroze.twitterdemo.post.data.BasicPostRequest;
import com.imroze.twitterdemo.post.data.Post;
import reactor.core.publisher.Mono;

public interface PostService {

  Mono<Post> createPost(String username, BasicPostRequest basicPostRequest);
}
