package com.instantcoffee.tech.service;

import com.instantcoffee.tech.entities.Relationship;
import com.instantcoffee.tech.entities.Request;
import com.instantcoffee.tech.entities.Response;
import com.instantcoffee.tech.entities.User;
import com.instantcoffee.tech.repo.RelationshipRepo;
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

    private void addFriend(Request request, User user) {
        Relationship relCheck = relationshipRepo.findByUserAndFriendEmailAndFriendHost(user, request.getSourceEmail(), request.getSourceHost())
            .orElse(null);
        if(relCheck == null) {
            Relationship relationship = new Relationship();
            relationship.setFriendEmail(request.getSourceEmail());
            relationship.setFriendHost(request.getSourceHost());
            relationship.setUser(user);
            relationship.setType("Pending");
            relationshipRepo.save(relationship);
        }
    }

    private void acceptFriend(Request request, User user) {

    }

    private void denyFriend(Request request, User user) {

    }

    private void removeFriend(Request request, User user) {

    }

    private void blockFriend(Request request, User user) {

    }

    public Response process(Request request, User user) throws IOException {
        // get the external IP of this server
        URL whatIsMyIp = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
            whatIsMyIp.openStream()));

        // add the port to the IP
        String myIp = in.readLine() + ":" + System.getenv("PORT");

        if(request.getDestinationHost().equals(myIp)) {
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
