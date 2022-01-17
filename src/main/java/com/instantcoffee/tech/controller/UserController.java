package com.instantcoffee.tech.controller;

import com.instantcoffee.tech.entities.Friendship;
import com.instantcoffee.tech.entities.User;
import com.instantcoffee.tech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  UserService userService;

  @GetMapping("/friendship")
  public List<Friendship> findAllByUserAndStatus(@RequestParam String status, @AuthenticationPrincipal User user) {
    return userService.findAllByUserAndStatus(user, status);
  }

}
