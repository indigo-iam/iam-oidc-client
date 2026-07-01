// SPDX-FileCopyrightText: 2014 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0
package it.infn.mw.tc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.infn.mw.tc.model.TokenView;
import it.infn.mw.tc.oauth.TokenService;

@Controller
public class OAuthController {

    private final TokenService tokenService;

    public OAuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/result")
    public String result(Model model) {
        TokenView tokenView = tokenService.currentTokenView();
        model.addAttribute("tokenView", tokenView);
        return "result";
    }
}