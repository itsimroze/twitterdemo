package com.imroze.twitterdemo.post;

import com.imroze.twitterdemo.post.data.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, String> {

}
