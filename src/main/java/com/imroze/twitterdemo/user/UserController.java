package com.imroze.twitterdemo.user;

import com.imroze.twitterdemo.user.data.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/demo/v1.0/user")
public class UserController {

  @Autowired private UserService userService;

  @GetMapping("/{username}")
  @PreAuthorize("hasRole('USER')")
  public Mono<UserDetails> getUserDetails(@PathVariable("username") String username) {
    return userService.getUserDetails(username);
  }


}
