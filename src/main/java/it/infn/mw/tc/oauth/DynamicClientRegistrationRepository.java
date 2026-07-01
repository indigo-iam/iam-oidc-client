// SPDX-FileCopyrightText: 2014 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0
package it.infn.mw.tc.oauth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

public class DynamicClientRegistrationRepository implements ClientRegistrationRepository {

  private final Map<String, ClientRegistration> registrations = new ConcurrentHashMap<>();

  @Override
  public ClientRegistration findByRegistrationId(String registrationId) {
    return registrations.get(registrationId);
  }

  public void save(ClientRegistration clientRegistration) {
    registrations.put(clientRegistration.getRegistrationId(), clientRegistration);
  }
}
