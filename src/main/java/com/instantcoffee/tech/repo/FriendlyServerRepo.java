package com.instantcoffee.tech.repo;

import com.instantcoffee.tech.entities.FriendlyServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendlyServerRepo extends JpaRepository<FriendlyServer, Long> {
}
