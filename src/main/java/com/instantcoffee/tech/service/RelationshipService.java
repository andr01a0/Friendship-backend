package com.instantcoffee.tech.service;

import com.instantcoffee.tech.entities.Request;
import com.instantcoffee.tech.entities.Response;
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

    public void addFriend() {

    }

    public void acceptFriend() {

    }

    public void denyFriend() {

    }

    public void removeFriend() {

    }

    public void blockFriend() {

    }

    public Response process(Request request) throws IOException {
        // get the external IP of this server
        URL whatIsMyIp = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
            whatIsMyIp.openStream()));

        // add the port to the IP
        String myIp = in.readLine() + ":" + System.getenv("PORT");

        if(request.getDestinationHost().equals(myIp)) {
            switch (request.getMethod()) {
                case "Add":
                    addFriend();
                    break;
                case "Accept":
                    acceptFriend();
                    break;
                case "Deny":
                    denyFriend();
                    break;
                case "Remove":
                    removeFriend();
                    break;
                case "Block":
                    blockFriend();
                    break;
            }
        } else {
            WebClient webClient = WebClient.builder()
                .baseUrl("http://"+request.getDestinationHost())
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
