// SPDX-FileCopyrightText: 2014 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0
package it.infn.mw.tc.oauth;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import it.infn.mw.tc.config.IamTestClientProperties;
import it.infn.mw.tc.model.TokenView;

@Service
public class TokenService {

  private final OAuth2AuthorizedClientService authorizedClientService;
  private final JwtDecoderService jwtDecoderService;
  private final UserInfoService userInfoService;
  private final IntrospectionService introspectionService;
  private final IamTestClientProperties properties;

  public TokenService(OAuth2AuthorizedClientService authorizedClientService,
      JwtDecoderService jwtDecoderService, UserInfoService userInfoService,
      IntrospectionService introspectionService, IamTestClientProperties properties) {
    this.authorizedClientService = authorizedClientService;
    this.jwtDecoderService = jwtDecoderService;
    this.userInfoService = userInfoService;
    this.introspectionService = introspectionService;
    this.properties = properties;
  }

  public Optional<TokenView> currentTokenView() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (!(authentication instanceof OAuth2AuthenticationToken oauthToken)) {
      throw new IllegalStateException("Current authentication is not OAuth2");
    }

    var authorizedClient = authorizedClientService
      .loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());

    if (authorizedClient == null) {
      return Optional.empty();
    }

    String accessToken = authorizedClient.getAccessToken() != null
        ? authorizedClient.getAccessToken().getTokenValue()
        : null;

    String refreshToken = authorizedClient.getRefreshToken() != null
        ? authorizedClient.getRefreshToken().getTokenValue()
        : null;

    String idToken = null;

    if (oauthToken.getPrincipal() instanceof OidcUser oidcUser && oidcUser.getIdToken() != null) {
      idToken = oidcUser.getIdToken().getTokenValue();
    }

    String userInfo = properties.callUserinfo() ? userInfoService.callUserInfo(authorizedClient)
        : "UserInfo disabled";

    String introspection =
        properties.callIntrospection() ? introspectionService.introspect(authorizedClient)
            : "Introspection disabled";

    return Optional.of(
        new TokenView(authorizedClient.getClientRegistration().getProviderDetails().getIssuerUri(),
            authorizedClient.getClientRegistration().getScopes().toString(), accessToken,
            jwtDecoderService.decode(accessToken), idToken, jwtDecoderService.decode(idToken),
            refreshToken, jwtDecoderService.decode(refreshToken), userInfo, introspection,
            properties.hideTokens()));
  }
}
