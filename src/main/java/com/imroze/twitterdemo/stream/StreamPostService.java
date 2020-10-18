package com.imroze.twitterdemo.stream;

import com.imroze.twitterdemo.stream.data.PostCapped;
import reactor.core.publisher.Flux;

public interface StreamPostService {

  Flux<PostCapped> streamPosts(String username);
}
