package com.instantcoffee.tech.repo;

import com.instantcoffee.tech.entities.BlockedUser;
import org.springframework.data.jpa.repository.JpaRepository;



public interface BlockedRepo extends JpaRepository<BlockedUser,Integer> {
}
