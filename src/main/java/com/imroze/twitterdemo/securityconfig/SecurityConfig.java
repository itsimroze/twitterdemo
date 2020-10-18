package com.imroze.twitterdemo.securityconfig;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

  @Autowired private SecurityContextRepository securityContextRepository;

  @Autowired private AuthenticationManager authenticationManager;

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

    http.exceptionHandling()
        .authenticationEntryPoint(
            (swe, e) ->
                Mono.fromRunnable(
                    () -> {
                      swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    }))
        .accessDeniedHandler(
            (swe, e) ->
                Mono.fromRunnable(
                    () -> {
                      swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    }))
        .and()
        .csrf()
        .disable()
        .httpBasic()
        .disable()
        .formLogin()
        .disable()
        .authenticationManager(authenticationManager)
        .securityContextRepository(securityContextRepository)
        .authorizeExchange()
        .pathMatchers(HttpMethod.OPTIONS)
        .permitAll()
        .pathMatchers("/auth/**", "/demo/v1.0/stream/**")
        .permitAll()
        .anyExchange()
        .authenticated();

    http.cors();

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(ImmutableList.of("*"));
    configuration.setAllowedMethods(
        ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(
        ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
