package com.instantcoffee.tech.service;

import com.instantcoffee.tech.entities.Friendship;
import com.instantcoffee.tech.entities.User;
import com.instantcoffee.tech.repo.FriendshipRepo;
import com.instantcoffee.tech.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

  @Autowired
  UserRepo userRepo;

  @Autowired
  FriendshipRepo friendshipRepo;

  public List<Friendship> findAllByUserAndStatus(User user, String status) {
    return friendshipRepo.findAllByUserOrTargetAndStatus(user, user.getUsername(), status);
  }

}
