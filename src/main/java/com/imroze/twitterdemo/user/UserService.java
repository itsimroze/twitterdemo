package com.imroze.twitterdemo.user;

import com.imroze.twitterdemo.user.data.SearchData;
import com.imroze.twitterdemo.user.data.SearchRequest;
import com.imroze.twitterdemo.user.data.UserDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

  Mono<UserDetails> getUserDetails(String username);

  Flux<SearchData> searchUser(SearchRequest keyWord);
}
