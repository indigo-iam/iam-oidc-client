// SPDX-FileCopyrightText: 2014 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0
package it.infn.mw.tc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.web.SecurityFilterChain;

import it.infn.mw.tc.oauth.DynamicClientRegistrationRepository;

@Configuration
public class SecurityConfig {

  @Bean
  DynamicClientRegistrationRepository clientRegistrationRepository() {
    return new DynamicClientRegistrationRepository();
  }

  @Bean
  OAuth2AuthorizedClientService authorizedClientService(
      DynamicClientRegistrationRepository clientRegistrationRepository) {
    return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http,
      DynamicClientRegistrationRepository clientRegistrationRepository) throws Exception {

    var authorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
        clientRegistrationRepository, "/oauth2/authorization");

    authorizationRequestResolver
      .setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());

    http
      .authorizeHttpRequests(auth -> auth.requestMatchers("/", "/login/start", "/error", "/css/**")
        .permitAll()
        .anyRequest()
        .authenticated())
      .oauth2Login(oauth -> oauth
        .authorizationEndpoint(
            endpoint -> endpoint.authorizationRequestResolver(authorizationRequestResolver))
        .defaultSuccessUrl("/result", true))
      .logout(logout -> logout.logoutSuccessUrl("/")
        .invalidateHttpSession(true)
        .clearAuthentication(true))
      .csrf(Customizer.withDefaults());

    return http.build();
  }

}
