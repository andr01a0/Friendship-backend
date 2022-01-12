package com.instantcoffee.tech.service;

import com.instantcoffee.tech.entities.FriendshipProtocol;
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

    public Response process(FriendshipProtocol friendshipProtocol) {
        return new Response(friendshipProtocol.getVersion(), 200, "Response Message");
    }

}
