package com.imroze.twitterdemo.stream;

import com.imroze.twitterdemo.auth.UserDataRepository;
import com.imroze.twitterdemo.post.PostRepository;
import com.imroze.twitterdemo.stream.data.PostCapped;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class StreamPostServiceImpl implements StreamPostService {

  @Autowired private PostCappedRepository postCappedRepository;

  @Autowired private UserDataRepository userDataRepository;

  @Autowired private ReactiveMongoTemplate reactiveMongoTemplate;

  @Autowired private PostRepository postRepository;

  @PostConstruct
  private void initializeCappedCollection() {
    reactiveMongoTemplate
        .collectionExists(PostCapped.class)
        .filter(aBoolean -> !aBoolean)
        .map(
            aBoolean ->
                reactiveMongoTemplate.createCollection(
                    PostCapped.class,
                    CollectionOptions.empty().capped().maxDocuments(100).size(5000)))
        .subscribe();

    postRepository
        .findAll()
        .take(50)
        .flatMap(
            post ->
                postCappedRepository.save(
                    PostCapped.builder()
                        .username(post.getUsername())
                        .createdAt(post.getCreatedAt())
                        .location(post.getLocation())
                        .postId(post.getPostId())
                        .text(post.getText())
                        .comments(post.getComments())
                        .likes(post.getLikes())
                        .build()))
        .subscribe();
  }

  @Override
  public Flux<PostCapped> streamPosts(String username) {
    return userDataRepository
        .findById(username)
        .flux()
        .flatMap(
            userData ->
                postCappedRepository
                    .findPostBy()
                    .filter(post -> userData.getFollowers().containsKey(post.getUsername())));
  }
}
