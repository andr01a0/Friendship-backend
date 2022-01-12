package com.instantcoffee.tech.authentication;

import com.instantcoffee.tech.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  public boolean existsByUsername(String username);

  public User findByUsername(String username);
}
