// SPDX-FileCopyrightText: 2014 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0
package it.infn.mw.tc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "iam-test-client.oauth2")
public record IamTestClientProperties(
// @formatter:off
  String clientId,
  String clientSecret,
  String defaultIssuer,
  String defaultScopes,
  String defaultResource,
  String redirectUri,
  String registrationId,
  boolean callUserinfo,
  boolean callIntrospection,
  boolean hideTokens
// @formatter:on
) {
}
