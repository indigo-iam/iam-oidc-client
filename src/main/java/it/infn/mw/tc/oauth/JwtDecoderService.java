// SPDX-FileCopyrightText: 2014 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0
package it.infn.mw.tc.oauth;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JwtDecoderService {

  private final ObjectMapper objectMapper = new ObjectMapper();

  public String decode(String token) {
    if (token == null || token.isBlank()) {
      return "";
    }

    String[] parts = token.split("\\.");

    if (parts.length < 2) {
      return "Token is not a JWT";
    }

    try {
      Map<String, Object> header = decodePart(parts[0]);
      Map<String, Object> claims = decodePart(parts[1]);

      return objectMapper.writerWithDefaultPrettyPrinter()
        .writeValueAsString(Map.of("header", header, "claims", claims));
    } catch (Exception ex) {
      return "Unable to decode JWT: " + ex.getMessage();
    }
  }

  private Map<String, Object> decodePart(String value) throws Exception {
    byte[] decoded = Base64.getUrlDecoder().decode(value);
    String json = new String(decoded, StandardCharsets.UTF_8);
    return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
  }
}
