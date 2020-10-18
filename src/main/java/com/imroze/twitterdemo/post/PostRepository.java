package com.imroze.twitterdemo.post;

import com.imroze.twitterdemo.post.data.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, String> {
  Flux<Post> findByUsername(@Param("username") String username);

  Mono<Long> countByUsername(@Param("username") String username);
}
