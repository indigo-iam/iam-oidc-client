// SPDX-FileCopyrightText: 2014 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0
package it.infn.mw.tc.oauth;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import it.infn.mw.tc.config.IamTestClientProperties;

@Service
public class DiscoveryService {

  private final RestClient restClient = RestClient.create();
  private final IamTestClientProperties properties;

  public DiscoveryService(IamTestClientProperties properties) {
    this.properties = properties;
  }

  public ClientRegistration discover(String issuer, String scopes) {
    String normalizedIssuer =
        issuer.endsWith("/") ? issuer.substring(0, issuer.length() - 1) : issuer;

    String discoveryUrl = normalizedIssuer + "/.well-known/openid-configuration";

    Map<String, Object> metadata = restClient.get()
      .uri(discoveryUrl)
      .retrieve()
      .body(new ParameterizedTypeReference<Map<String, Object>>() {});

    if (metadata == null) {
      throw new IllegalStateException("OIDC discovery document is empty");
    }

    Set<String> scopeSet = new LinkedHashSet<>(Arrays.asList(scopes.trim().split("\\s+")));

    boolean hasSecret = properties.clientSecret() != null && !properties.clientSecret().isBlank();

    return ClientRegistration.withRegistrationId(properties.registrationId())
      .clientId(properties.clientId())
      .clientSecret(hasSecret ? properties.clientSecret() : null)
      .clientName("Dynamic OIDC Provider")
      .clientAuthenticationMethod(hasSecret ? ClientAuthenticationMethod.CLIENT_SECRET_BASIC
          : ClientAuthenticationMethod.NONE)
      .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
      .redirectUri(properties.redirectUri())
      .scope(scopeSet)
      .authorizationUri(required(metadata, "authorization_endpoint"))
      .tokenUri(required(metadata, "token_endpoint"))
      .jwkSetUri(required(metadata, "jwks_uri"))
      .issuerUri(required(metadata, "issuer"))
      .userInfoUri((String) metadata.get("userinfo_endpoint"))
      .userNameAttributeName("sub")
      .providerConfigurationMetadata(metadata)
      .build();
  }

  private static String required(Map<String, Object> metadata, String key) {
    Object value = metadata.get(key);
    if (value == null || value.toString().isBlank()) {
      throw new IllegalStateException("Missing required OIDC metadata: " + key);
    }
    return value.toString();
  }
}
