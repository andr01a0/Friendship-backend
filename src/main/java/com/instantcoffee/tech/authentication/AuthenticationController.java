package com.instantcoffee.tech.authentication;

import com.instantcoffee.tech.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
@AllArgsConstructor
public class AuthenticationController {

    private final JwtUserDetailsService jwtUserDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        User user = this.jwtUserDetailsService.signUp(username, password);

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(new SignUpResponse(user.getId(), user.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        String token = this.jwtUserDetailsService.login(username, password);
        System.out.println(token);

        if (token == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/check-token")
    public ResponseEntity<?> checkToken(@RequestBody CheckTokenRequest checkTokenRequest) {
        boolean isValid = this.jwtUserDetailsService.checkToken(checkTokenRequest.getToken());

        if (isValid) {
            return ResponseEntity.ok(new CheckTokenResponse("valid"));
        }

        return ResponseEntity.status(401).body(new CheckTokenResponse("invalid"));
    }
}
