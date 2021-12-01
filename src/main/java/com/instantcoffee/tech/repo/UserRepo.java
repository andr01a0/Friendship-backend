package com.instantcoffee.tech.repo;

import com.instantcoffee.tech.authentication.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepo extends JpaRepository<User,Integer> {
}
