package com.imroze.twitterdemo.post;

import com.imroze.twitterdemo.exceptions.TwitterDemoClientException;
import com.imroze.twitterdemo.post.data.BasicPostRequest;
import com.imroze.twitterdemo.post.data.Post;
import com.imroze.twitterdemo.utility.CommonUtils;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PostServiceImpl implements PostService {

  @Autowired private PostRepository postRepository;

  @Override
  public Mono<Post> createPost(String username, BasicPostRequest basicPostRequest) {

    if (basicPostRequest.getText().length() > 140) {
      return Mono.error(
          () ->
              new TwitterDemoClientException(
                  new IllegalStateException(), "Post is out of text limit."));
    }

    return postRepository.save(
        Post.builder()
            .username(username)
            .createdAt(new Date())
            .location(basicPostRequest.getLocation())
            .postId(CommonUtils.generateUUID())
            .text(basicPostRequest.getText())
            .build());
  }
}
