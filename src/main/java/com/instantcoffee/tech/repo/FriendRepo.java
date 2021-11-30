package com.instantcoffee.tech.repo;

import com.instantcoffee.tech.entities.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface FriendRepo extends JpaRepository<Friend,Integer> {
}
