// SPDX-FileCopyrightText: 2014 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0
package it.infn.mw.tc.oauth;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class IntrospectionService {

  private final RestClient restClient = RestClient.create();
  private final ObjectMapper objectMapper = new ObjectMapper();

  public String introspect(OAuth2AuthorizedClient authorizedClient) {
    Object endpoint = authorizedClient.getClientRegistration()
      .getProviderDetails()
      .getConfigurationMetadata()
      .get("introspection_endpoint");

    if (endpoint == null || endpoint.toString().isBlank()) {
      return "No introspection_endpoint found in discovery document";
    }

    var form = new LinkedMultiValueMap<String, String>();
    form.add("token", authorizedClient.getAccessToken().getTokenValue());
    form.add("token_type_hint", "access_token");

    var registration = authorizedClient.getClientRegistration();

    boolean hasSecret =
        registration.getClientSecret() != null && !registration.getClientSecret().isBlank();

    try {
      Map<String, Object> response = restClient.post()
        .uri(endpoint.toString())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .headers(headers -> {
          if (hasSecret) {
            headers.setBasicAuth(registration.getClientId(), registration.getClientSecret());
          }
        })
        .body(form)
        .retrieve()
        .body(new ParameterizedTypeReference<Map<String, Object>>() {});

      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
    } catch (Exception ex) {
      return "Introspection call failed: " + ex.getMessage();
    }
  }
}
