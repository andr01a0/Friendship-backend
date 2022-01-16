package com.instantcoffee.tech.repo;

import com.instantcoffee.tech.entities.FriendlyServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendlyServerRepo extends JpaRepository<FriendlyServer, Long> {

  Optional<FriendlyServer> findByHost(String host);

}
