package com.instantcoffee.tech.controller;

import com.instantcoffee.tech.entities.Request;
import com.instantcoffee.tech.entities.Response;
import com.instantcoffee.tech.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friendship")
public class FriendshipController {

  @Autowired
  RelationshipService relationshipService;

  @PostMapping
  public ResponseEntity<String> friendshipProtocol(@RequestBody String protocolBody) {
    Request request = new Request(protocolBody);
    Response response = relationshipService.process(request);
    return ResponseEntity.ok(response.toString());
  }

}
