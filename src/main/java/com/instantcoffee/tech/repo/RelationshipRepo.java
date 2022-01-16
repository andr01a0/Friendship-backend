package com.instantcoffee.tech.repo;

import com.instantcoffee.tech.entities.Relationship;
import com.instantcoffee.tech.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RelationshipRepo extends JpaRepository<Relationship,Integer> {

  Optional<Relationship> findByUserAndFriendEmailAndFriendHost(User user, String friendEmail, String friendHost);

}
