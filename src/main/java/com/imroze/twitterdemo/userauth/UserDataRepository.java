package com.imroze.twitterdemo.userauth;

import com.imroze.twitterdemo.userauth.data.UserData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
interface UserDataRepository extends ReactiveMongoRepository<UserData, String> {

  Mono<UserData> findUserDataByEmail(@Param("email") String email);

  Mono<UserData> findUserDataByNumber(@Param("number") String number);
}
