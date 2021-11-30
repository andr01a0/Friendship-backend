package com.instantcoffee.tech.repo;

import com.instantcoffee.tech.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
}
