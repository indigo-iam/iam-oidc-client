// SPDX-FileCopyrightText: 2014 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0
package it.infn.mw.tc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import it.infn.mw.tc.config.IamTestClientProperties;

@SpringBootApplication
@EnableConfigurationProperties(IamTestClientProperties.class)
public class IamTestClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(IamTestClientApplication.class, args);
    }
}