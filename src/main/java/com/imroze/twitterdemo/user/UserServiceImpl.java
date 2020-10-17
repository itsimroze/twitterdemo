package com.imroze.twitterdemo.user;

import com.imroze.twitterdemo.user.data.UserDetails;
import com.imroze.twitterdemo.userauth.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}
