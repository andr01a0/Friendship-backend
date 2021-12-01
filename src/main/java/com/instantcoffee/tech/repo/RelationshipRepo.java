package com.instantcoffee.tech.repo;

import com.instantcoffee.tech.entities.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RelationshipRepo extends JpaRepository<Relationship,Integer> {
}
