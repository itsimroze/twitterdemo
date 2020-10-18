package com.imroze.twitterdemo.stream;

import com.imroze.twitterdemo.stream.data.PostCapped;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PostCappedRepository extends ReactiveMongoRepository<PostCapped, String> {

  @Tailable
  Flux<PostCapped> findPostBy();
}
