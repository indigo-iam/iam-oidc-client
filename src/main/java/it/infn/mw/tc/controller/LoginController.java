// SPDX-FileCopyrightText: 2014 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0
package it.infn.mw.tc.controller;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.infn.mw.tc.config.IamTestClientProperties;
import it.infn.mw.tc.model.LoginForm;
import it.infn.mw.tc.oauth.DiscoveryService;
import it.infn.mw.tc.oauth.DynamicClientRegistrationRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

  private final IamTestClientProperties properties;
  private final DiscoveryService discoveryService;
  private final DynamicClientRegistrationRepository registrationRepository;

  public LoginController(IamTestClientProperties properties, DiscoveryService discoveryService,
      DynamicClientRegistrationRepository registrationRepository) {
    this.properties = properties;
    this.discoveryService = discoveryService;
    this.registrationRepository = registrationRepository;
  }

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("form",
        new LoginForm(properties.defaultIssuer(), properties.defaultScopes()));
    return "index";
  }

  @PostMapping("/login/start")
  public String startLogin(@ModelAttribute LoginForm form, HttpSession session) {
    ClientRegistration registration = discoveryService.discover(form.issuer(), form.scopes());

    registrationRepository.save(registration);

    session.setAttribute("issuer", form.issuer());
    session.setAttribute("scopes", form.scopes());
    session.setAttribute("resource", properties.defaultResource());

    return "redirect:/oauth2/authorization/" + properties.registrationId();
  }
}
