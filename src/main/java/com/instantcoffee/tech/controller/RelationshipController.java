package com.instantcoffee.tech.controller;

import com.instantcoffee.tech.entities.FriendshipProtocol;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friendship")
public class RelationshipController {

  @PostMapping
  public ResponseEntity<String> relationshipProtocol(@RequestBody FriendshipProtocol friendshipProtocol) {
    return ResponseEntity.ok("");
  }

}
