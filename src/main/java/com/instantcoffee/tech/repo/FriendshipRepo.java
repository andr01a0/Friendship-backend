package com.instantcoffee.tech.repo;

import com.instantcoffee.tech.entities.Friendship;
import com.instantcoffee.tech.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface FriendshipRepo extends JpaRepository<Friendship,Integer> {

  Optional<Friendship> findByUserAndFriendEmailAndFriendHost(User user, String friendEmail, String friendHost);

}
