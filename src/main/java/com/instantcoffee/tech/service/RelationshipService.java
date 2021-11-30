package com.instantcoffee.tech.service;

import com.instantcoffee.tech.repo.RelationshipRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RelationshipService {

    RelationshipRepo relationshipRepo;
}
