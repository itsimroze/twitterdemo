package com.imroze.twitterdemo.user;

import com.imroze.twitterdemo.user.data.UserDetails;
import reactor.core.publisher.Mono;

public interface UserService {

  Mono<UserDetails> getUserDetails(String username);
}
