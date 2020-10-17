package com.imroze.twitterdemo.user;

import com.imroze.twitterdemo.user.data.SearchData;
import com.imroze.twitterdemo.user.data.SearchRequest;
import com.imroze.twitterdemo.user.data.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("/demo/v1.0/user")
public class UserController {

  @Autowired private UserService userService;

  @GetMapping("/{username}")
  @PreAuthorize("hasRole('USER')")
  public Mono<UserDetails> getUserDetails(@PathVariable("username") String username) {
    return userService.getUserDetails(username);
  }

  @PostMapping("/{username}/search")
  public Flux<SearchData> getUserNames(@RequestBody SearchRequest searchRequest) {
    return userService.searchUser(searchRequest);
  }

  @PutMapping("/{username}/follow/{followUserName}")
  public Mono<String> followUser(
      @PathVariable("username") String username,
      @PathVariable("followUserName") String followName) {
    return userService.followUser(username, followName);
  }
}
