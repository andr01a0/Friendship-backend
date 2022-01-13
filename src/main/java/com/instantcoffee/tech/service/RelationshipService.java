package com.instantcoffee.tech.service;

import com.instantcoffee.tech.entities.Request;
import com.instantcoffee.tech.entities.Response;
import com.instantcoffee.tech.repo.RelationshipRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RelationshipService {

    @Autowired
    RelationshipRepo relationshipRepo;

    public Response process(Request request) {
        return new Response(request.getVersion(), 200, "Response Message");
    }

}
