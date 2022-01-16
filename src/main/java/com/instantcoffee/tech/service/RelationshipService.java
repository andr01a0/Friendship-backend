package com.instantcoffee.tech.service;

import com.instantcoffee.tech.entities.Relationship;
import com.instantcoffee.tech.entities.Request;
import com.instantcoffee.tech.entities.Response;
import com.instantcoffee.tech.entities.User;
import com.instantcoffee.tech.repo.RelationshipRepo;
import com.instantcoffee.tech.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Service
@AllArgsConstructor
public class RelationshipService {

    @Autowired
    RelationshipRepo relationshipRepo;

    @Autowired
    UserRepo userRepo;

    private void addFriend(Request request, User user) {
        Relationship relationship = relationshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getDestinationEmail(), request.getDestinationHost()
        ).orElse(null);
        Relationship reverseRelationship = relationshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getSourceEmail(), request.getSourceHost()
        ).orElse(null);

        if(relationship == null && reverseRelationship == null) {
            Relationship newRelationship = new Relationship();
            newRelationship.setFriendEmail(request.getSourceEmail());
            newRelationship.setFriendHost(request.getSourceHost());
            newRelationship.setUser(user);
            newRelationship.setType("Pending");
            relationshipRepo.save(newRelationship);
        }
    }

    private void acceptFriend(Request request, User user) {
        Relationship relationship = relationshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getDestinationEmail(), request.getDestinationHost()
        ).orElse(null);
        Relationship reverseRelationship = relationshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getSourceEmail(), request.getSourceHost()
        ).orElse(null);

        if(relationship != null && relationship.getType().equals("Pending")) {
            relationship.setType("Friend");
            relationshipRepo.save(relationship);
        } else if(reverseRelationship != null && reverseRelationship.getType().equals("Pending")) {
            reverseRelationship.setType("Friend");
            relationshipRepo.save(reverseRelationship);
        }
    }

    private void denyFriend(Request request, User user) {
        Relationship relationship = relationshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getDestinationEmail(), request.getDestinationHost()
        ).orElse(null);
        Relationship reverseRelationship = relationshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getSourceEmail(), request.getSourceHost()
        ).orElse(null);

        if(relationship != null && relationship.getType().equals("Pending"))
            relationshipRepo.delete(relationship);
        else if(reverseRelationship != null && reverseRelationship.getType().equals("Pending"))
            relationshipRepo.delete(reverseRelationship);
    }

    private void removeFriend(Request request, User user) {
        Relationship relationship = relationshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getDestinationEmail(), request.getDestinationHost()
        ).orElse(null);
        Relationship reverseRelationship = relationshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getSourceEmail(), request.getSourceHost()
        ).orElse(null);

        if(relationship != null)
            relationshipRepo.delete(relationship);
        else if(reverseRelationship != null)
            relationshipRepo.delete(reverseRelationship);
    }

    private void blockFriend(Request request, User user) {
        Relationship relationship = relationshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getDestinationEmail(), request.getDestinationHost()
        ).orElse(null);
        Relationship reverseRelationship = relationshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getSourceEmail(), request.getSourceHost()
        ).orElse(null);

        if(relationship != null) {
            relationship.setType("Blocked");
            relationshipRepo.save(relationship);
        } else if(reverseRelationship != null) {
            reverseRelationship.setType("Blocked");
            relationshipRepo.save(reverseRelationship);
        }
    }

    public Response process(Request request, User user) throws IOException {
        // get the external IP of this server
        URL whatIsMyIp = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
            whatIsMyIp.openStream()));

        // add the port to the IP
        String myIp = in.readLine() + ":" + System.getenv("PORT");
        // String myIp = "localhost" + ":" + System.getenv("PORT");

        // check if the user has access to process the request
        if(!user.getUsername().equals(request.getSourceEmail()))
            return new Response(request.getVersion(), 530, "Access denied");

        if(request.getDestinationHost().equals(myIp)) {
            // check if username is valid in the server
            User friend = userRepo.findByUsername(request.getDestinationEmail()).orElse(null);
            if(friend == null)
                return new Response(request.getVersion(), 501, "Username is not a valid user");

            switch (request.getMethod()) {
                case "Add":
                    addFriend(request, user);
                    break;
                case "Accept":
                    acceptFriend(request, user);
                    break;
                case "Deny":
                    denyFriend(request, user);
                    break;
                case "Remove":
                    removeFriend(request, user);
                    break;
                case "Block":
                    blockFriend(request, user);
                    break;
                default:
                    return new Response(request.getVersion(), 500, "Method not allowed.");
            }
        } else {
            WebClient webClient = WebClient.builder()
                .baseUrl("http://"+request.getDestinationHost()+"/friendship")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

            String response = webClient.post()
                .body(Mono.just(request.toJson()), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            System.out.println(response);
        }
        return new Response(request.getVersion(), 200, "Success");
    }

}
