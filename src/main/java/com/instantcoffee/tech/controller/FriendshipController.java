package com.instantcoffee.tech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instantcoffee.tech.entities.ProtocolBody;
import com.instantcoffee.tech.entities.Request;
import com.instantcoffee.tech.entities.Response;
import com.instantcoffee.tech.entities.User;
import com.instantcoffee.tech.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/friendship")
public class FriendshipController {

  @Autowired
  FriendshipService friendshipService;

  @PostMapping
  public ResponseEntity<String> friendshipProtocol(@AuthenticationPrincipal User user,
                                                   @RequestBody String requestJson, HttpServletRequest clientInfo) throws IOException {
    System.out.println(clientInfo.getLocalAddr());
    ObjectMapper mapper = new ObjectMapper();
    ProtocolBody protocolBody = mapper.readValue(requestJson, ProtocolBody.class);
    Request request = new Request(protocolBody.getRequest());
    Response response = friendshipService.process(request, user);
    return ResponseEntity.ok(response.toJson());
  }

}
