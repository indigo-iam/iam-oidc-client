// SPDX-FileCopyrightText: 2014 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0
package it.infn.mw.tc.model;

public record TokenView(
// @formatter:off
  String issuer,
  String scopes,
  String accessToken,
  String decodedAccessToken,
  String idToken,
  String decodedIdToken,
  String refreshToken,
  String decodedRefreshToken,
  String userInfo,
  String introspection,
  Boolean hideTokens
// @formatter:on
) {
}
