// SPDX-FileCopyrightText: 2014 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0
package it.infn.mw.tc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.infn.mw.tc.oauth.TokenService;

@Controller
public class OAuthController {

  private final TokenService tokenService;

  public OAuthController(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @GetMapping("/result")
  public String result(Model model, RedirectAttributes redirectAttributes) {

    return tokenService.currentTokenView().map(tokenView -> {
      model.addAttribute("tokenView", tokenView);
      return "result";
    }).orElseGet(() -> {
      redirectAttributes.addFlashAttribute("message",
          "Your login session has expired. Please authenticate again.");
      return "redirect:/";
    });
  }
}
