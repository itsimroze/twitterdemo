package com.imroze.twitterdemo.auth;

import com.imroze.twitterdemo.auth.data.request.LoginRequest;
import com.imroze.twitterdemo.auth.data.request.UserRegistrationData;
import com.imroze.twitterdemo.auth.data.response.LoginResponse;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth/v1.0/user")
public class AuthController {

  @Autowired
  AuthService authService;

  @PutMapping("/register")
  public Mono<LoginResponse> registerUser(@RequestBody UserRegistrationData userRegistrationData) {
    return authService.registerUser(userRegistrationData);
  }

  @PostMapping("/login")
  public Mono<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    return authService.login(loginRequest);
  }

  @PostMapping("/logout")
  public Mono<String> logoutUser(@RequestBody HashMap<String, String> username) {
    return authService.logoutUser(username);
  }
}
