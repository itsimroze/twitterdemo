package com.imroze.twitterdemo.user;

import com.imroze.twitterdemo.auth.data.FollowStatus;
import com.imroze.twitterdemo.exceptions.TwitterDemoNotFoundException;
import com.imroze.twitterdemo.user.data.SearchData;
import com.imroze.twitterdemo.user.data.SearchRequest;
import com.imroze.twitterdemo.user.data.UserDetails;
import com.imroze.twitterdemo.auth.UserDataRepository;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
  @Autowired private UserDataRepository userDataRepository;

  @Override
  public Mono<UserDetails> getUserDetails(String username) {
    return userDataRepository
        .findById(username)
        .map(
            userData ->
                UserDetails.builder()
                    .email(userData.getEmail())
                    .name(userData.getName())
                    .number(userData.getNumber())
                    .profilePictureUrl(userData.getProfilePictureUrl())
                    .recoveryEmail(userData.getRecoveryEmail())
                    .username(userData.getUserName())
                    .build());
  }

  @Override
  public Flux<SearchData> searchUser(SearchRequest searchRequest) {

    switch (searchRequest.getSearchType()) {
      case NAME:
        return userDataRepository
            .findAll()
            .filter(userData -> userData.getName().startsWith(searchRequest.getKeyword()))
            .switchIfEmpty(
                Flux.error(
                    () ->
                        new TwitterDemoNotFoundException(new RuntimeException(), "No user found!")))
            .map(
                userData ->
                    SearchData.builder()
                        .name(userData.getName())
                        .username(userData.getUserName())
                        .profilePictureUrl(userData.getProfilePictureUrl())
                        .build());
      case USERNAME:
        return userDataRepository
            .findAll()
            .filter(userData -> userData.getUserName().startsWith(searchRequest.getKeyword()))
            .switchIfEmpty(
                Flux.error(
                    () ->
                        new TwitterDemoNotFoundException(new RuntimeException(), "No user found!")))
            .map(
                userData ->
                    SearchData.builder()
                        .name(userData.getName())
                        .username(userData.getUserName())
                        .profilePictureUrl(userData.getProfilePictureUrl())
                        .build());

      default:
        return Flux.error(
            () -> new TwitterDemoNotFoundException(new RuntimeException(), "No user found!"));
    }
  }

  @Override
  public Mono<String> followUser(String username, String followName) {
    return userDataRepository
        .findById(followName)
        .switchIfEmpty(
            Mono.error(
                () -> new TwitterDemoNotFoundException(new RuntimeException(), "User not found!")))
        .flatMap(
            toBeFollowedUser -> {
              if (toBeFollowedUser.getFollowers().get(username) == null) {
                toBeFollowedUser.getFollowers().put(username, FollowStatus.FOLLOWER);
              } else if (toBeFollowedUser
                  .getFollowers()
                  .get(username)
                  .equals(FollowStatus.FOLLOWING)) {
                toBeFollowedUser.getFollowers().put(username, FollowStatus.BOTH);
              } else if (toBeFollowedUser
                      .getFollowers()
                      .get(username)
                      .equals(FollowStatus.FOLLOWER)
                  || toBeFollowedUser.getFollowers().get(username).equals(FollowStatus.BOTH)) {
                return Mono.just("You already follow " + followName);
              }
              return userDataRepository
                  .findById(username)
                  .flatMap(
                      userData -> {
                        userData
                            .getFollowers()
                            .put(
                                followName,
                                toBeFollowedUser
                                        .getFollowers()
                                        .get(username)
                                        .equals(FollowStatus.FOLLOWER)
                                    ? FollowStatus.FOLLOWING
                                    : FollowStatus.BOTH);
                        return userDataRepository
                            .saveAll(Arrays.asList(userData, toBeFollowedUser))
                            .collectList()
                            .map(userData1 -> userData.getFollowers().get(followName).name());
                      });
            });
  }
}
