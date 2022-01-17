package com.instantcoffee.tech.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instantcoffee.tech.entities.*;
import com.instantcoffee.tech.repo.FriendlyServerRepo;
import com.instantcoffee.tech.repo.FriendshipRepo;
import com.instantcoffee.tech.repo.UserRepo;
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
public class FriendshipService {

    @Autowired
    FriendshipRepo friendshipRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    FriendlyServerRepo friendlyServerRepo;

    private void addFriend(Request request) {
        User user = userRepo.findByUsername(request.getSourceEmail()).orElse(null);
        Friendship friendship = friendshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getDestinationEmail(), request.getDestinationHost()
        ).orElse(null);

        if(friendship == null) {
            Friendship newFriendship = new Friendship();
            newFriendship.setFriendEmail(request.getDestinationEmail());
            newFriendship.setFriendHost(request.getDestinationHost());
            newFriendship.setUser(user);
            newFriendship.setStatus("Pending");
            friendshipRepo.save(newFriendship);
        }
    }

    private void acceptFriend(Request request) {
        Friendship friendship = friendshipRepo.findByUserAndFriendEmailAndFriendHost(
            userRepo.findByUsername(request.getSourceEmail()).orElse(null),
            request.getDestinationEmail(), request.getDestinationHost()
        ).orElse(null);

        if(friendship != null && friendship.getStatus().equals("Pending")) {
            friendship.setStatus("Friends");
            friendshipRepo.save(friendship);
        }
    }

    private void denyFriend(Request request) {
        Friendship friendship = friendshipRepo.findByUserAndFriendEmailAndFriendHost(
            userRepo.findByUsername(request.getSourceEmail()).orElse(null),
            request.getDestinationEmail(), request.getDestinationHost()
        ).orElse(null);

        if(friendship != null && friendship.getStatus().equals("Pending"))
            friendshipRepo.delete(friendship);
    }

    private void removeFriend(Request request) {
        Friendship friendship = friendshipRepo.findByUserAndFriendEmailAndFriendHost(
            userRepo.findByUsername(request.getSourceEmail()).orElse(null),
            request.getDestinationEmail(), request.getDestinationHost()
        ).orElse(null);

        if(friendship != null && friendship.getStatus().equals("Friends"))
            friendshipRepo.delete(friendship);
    }

    private void blockFriend(Request request) {
        Friendship friendship = friendshipRepo.findByUserAndFriendEmailAndFriendHost(
            userRepo.findByUsername(request.getSourceEmail()).orElse(null),
            request.getDestinationEmail(), request.getDestinationHost()
        ).orElse(null);

        if(friendship != null) {
            friendship.setStatus("Blocked");
            friendshipRepo.save(friendship);
        }
    }

    private Response executeRequest(Request request) {
        switch (request.getMethod()) {
            case "Add":
                addFriend(request);
                break;
            case "Accept":
                acceptFriend(request);
                break;
            case "Deny":
                denyFriend(request);
                break;
            case "Remove":
                removeFriend(request);
                break;
            case "Block":
                blockFriend(request);
                break;
            default:
                return new Response(request.getVersion(), 500, "Method not allowed.");
        }
        return new Response(request.getVersion(), 200, "Success");
    }

    public Response process(Request request, User user, String origin) throws IOException {
        // check if request came from a server
        if(origin.equals(request.getSourceHost().split(":")[0])) {
            // check if username is valid in the server
            User friend = userRepo.findByUsername(request.getDestinationEmail()).orElse(null);
            if(friend == null)
                return new Response(request.getVersion(), 501, "Username is not a valid user");

            // swap source with destination user
            String swap = request.getDestinationEmail();
            request.setDestinationEmail(request.getSourceEmail());
            request.setSourceEmail(swap);
            swap = request.getDestinationHost();
            request.setDestinationHost(request.getSourceHost());
            request.setSourceHost(swap);
        } else {
            // check if the user has access to process the request
            if(!user.getUsername().equals(request.getSourceEmail()))
                return new Response(request.getVersion(), 530, "Access denied");

            // get the external IP of this server
            URL whatIsMyIp = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                whatIsMyIp.openStream()));
            String myIp = in.readLine();
            //String myIp = "127.0.0.1";

            // check if it needs to send to another server
            if(!request.getDestinationHost().equals(myIp.concat(":").concat(System.getenv("PORT")))) {
                WebClient.Builder webClientBuilder = WebClient.builder()
                    .baseUrl("http://" + request.getDestinationHost() + "/friendship")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

                friendlyServerRepo.findByHost(request.getDestinationHost()).ifPresent(
                    friendlyServer -> webClientBuilder.defaultHeader(
                        HttpHeaders.AUTHORIZATION, "Bearer " + friendlyServer.getJwtToken()
                    )
                );
                WebClient webClient = webClientBuilder.build();

                String responseString = webClient.post()
                    .body(Mono.just(request.toJson()), String.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
                ObjectMapper mapper = new ObjectMapper();
                ResponseJson responseJson = mapper.readValue(responseString, ResponseJson.class);
                Response response = new Response(responseJson.getResponse());
                if (response.getStatusCode() == 200)
                    executeRequest(request);
                return response;
            }
        }
        return executeRequest(request);
    }

}
