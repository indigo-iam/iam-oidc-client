// SPDX-FileCopyrightText: 2014 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0
package it.infn.mw.tc.oauth;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserInfoService {

  private final RestClient restClient = RestClient.create();
  private final ObjectMapper objectMapper = new ObjectMapper();

  public String callUserInfo(OAuth2AuthorizedClient authorizedClient) {
    String userInfoUri = authorizedClient.getClientRegistration()
      .getProviderDetails()
      .getUserInfoEndpoint()
      .getUri();

    if (userInfoUri == null || userInfoUri.isBlank()) {
      return "No userinfo_endpoint found in discovery document";
    }

    try {
      Map<String, Object> response = restClient.get()
        .uri(userInfoUri)
        .headers(
            headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
        .retrieve()
        .body(new ParameterizedTypeReference<Map<String, Object>>() {});

      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
    } catch (Exception ex) {
      return "UserInfo call failed: " + ex.getMessage();
    }
  }
}
