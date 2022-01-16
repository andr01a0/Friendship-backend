package com.instantcoffee.tech.service;

import com.instantcoffee.tech.entities.*;
import com.instantcoffee.tech.repo.FriendlyServerRepo;
import com.instantcoffee.tech.repo.FriendshipRepo;
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
public class FriendshipService {

    @Autowired
    FriendshipRepo friendshipRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    FriendlyServerRepo friendlyServerRepo;

    private void addFriend(Request request, User user) {
        Friendship friendship = friendshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getDestinationEmail(), request.getDestinationHost()
        ).orElse(null);
        Friendship reverseFriendship = friendshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getSourceEmail(), request.getSourceHost()
        ).orElse(null);

        if(friendship == null && reverseFriendship == null) {
            Friendship newFriendship = new Friendship();
            newFriendship.setFriendEmail(request.getSourceEmail());
            newFriendship.setFriendHost(request.getSourceHost());
            newFriendship.setUser(user);
            newFriendship.setType("Pending");
            friendshipRepo.save(newFriendship);
        }
    }

    private void acceptFriend(Request request, User user) {
        Friendship friendship = friendshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getDestinationEmail(), request.getDestinationHost()
        ).orElse(null);
        Friendship reverseFriendship = friendshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getSourceEmail(), request.getSourceHost()
        ).orElse(null);

        if(friendship != null && friendship.getType().equals("Pending")) {
            friendship.setType("Friends");
            friendshipRepo.save(friendship);
        } else if(reverseFriendship != null && reverseFriendship.getType().equals("Pending")) {
            reverseFriendship.setType("Friends");
            friendshipRepo.save(reverseFriendship);
        }
    }

    private void denyFriend(Request request, User user) {
        Friendship friendship = friendshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getDestinationEmail(), request.getDestinationHost()
        ).orElse(null);
        Friendship reverseFriendship = friendshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getSourceEmail(), request.getSourceHost()
        ).orElse(null);

        if(friendship != null && friendship.getType().equals("Pending"))
            friendshipRepo.delete(friendship);
        else if(reverseFriendship != null && reverseFriendship.getType().equals("Pending"))
            friendshipRepo.delete(reverseFriendship);
    }

    private void removeFriend(Request request, User user) {
        Friendship friendship = friendshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getDestinationEmail(), request.getDestinationHost()
        ).orElse(null);
        Friendship reverseFriendship = friendshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getSourceEmail(), request.getSourceHost()
        ).orElse(null);

        if(friendship != null && friendship.getType().equals("Friends"))
            friendshipRepo.delete(friendship);
        else if(reverseFriendship != null && reverseFriendship.getType().equals("Friends"))
            friendshipRepo.delete(reverseFriendship);
    }

    private void blockFriend(Request request, User user) {
        Friendship friendship = friendshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getDestinationEmail(), request.getDestinationHost()
        ).orElse(null);
        Friendship reverseFriendship = friendshipRepo.findByUserAndFriendEmailAndFriendHost(
            user, request.getSourceEmail(), request.getSourceHost()
        ).orElse(null);

        if(friendship != null) {
            friendship.setType("Blocked");
            friendshipRepo.save(friendship);
        } else if(reverseFriendship != null) {
            reverseFriendship.setType("Blocked");
            friendshipRepo.save(reverseFriendship);
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
            WebClient.Builder webClientBuilder = WebClient.builder()
                .baseUrl("http://" + request.getDestinationHost() + "/friendship")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            friendlyServerRepo.findByHost(request.getDestinationHost()).ifPresent(
                friendlyServer -> webClientBuilder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + friendlyServer.getJwtToken())
            );
            WebClient webClient = webClientBuilder.build();

            String response = webClient.post()
                .body(Mono.just(request.toJson()), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        }
        return new Response(request.getVersion(), 200, "Success");
    }

}
