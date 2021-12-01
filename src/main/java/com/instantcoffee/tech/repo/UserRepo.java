package com.instantcoffee.tech.repo;

import com.instantcoffee.tech.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepo extends JpaRepository<User,Integer> {
}
