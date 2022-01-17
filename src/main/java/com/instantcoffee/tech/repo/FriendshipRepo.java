package com.instantcoffee.tech.repo;

import com.instantcoffee.tech.entities.Friendship;
import com.instantcoffee.tech.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FriendshipRepo extends JpaRepository<Friendship, Long> {

  Optional<Friendship> findByUserAndFriendEmailAndFriendHost(User user, String friendEmail, String friendHost);

  List<Friendship> findAllByUserAndStatus(User user, String status);

}
