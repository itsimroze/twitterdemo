package com.imroze.twitterdemo.stream;

import com.imroze.twitterdemo.stream.data.PostCapped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/demo/v1.0/stream")
public class StreamPostController {

  @Autowired StreamPostService streamPostService;

  @GetMapping(value = "/user/{username}/wall/post", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Flux<PostCapped> streamPosts(@PathVariable("username") String username) {
    return streamPostService.streamPosts(username);
  }
}
