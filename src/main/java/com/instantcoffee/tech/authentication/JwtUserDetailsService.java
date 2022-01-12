package com.instantcoffee.tech.authentication;

import com.instantcoffee.tech.entities.User;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;
  private final JwtTokenUtil jwtTokenUtil;
  private final PasswordEncoder passwordEncoder;

  @Override
  public User loadUserByUsername(String username) {
    return this.userRepository.findByUsername(username);
  }

  public User signUp(String username, String password) {
    boolean isUsernameTaken = this.userRepository.existsByUsername(username);

    if (isUsernameTaken) {
      return null;
    }

    User user = new User();
    user.setUsername(username);
    user.setPassword(this.passwordEncoder.encode(password));

    return this.userRepository.save(user);
  }

  public String login(String username, String password) {
    User user = this.loadUserByUsername(username);

    if (user == null) {
      return null;
    }

    boolean didPasswordMatch = this.passwordEncoder.matches(password, user.getPassword());

    if (!didPasswordMatch) {
      return null;
    }

    return jwtTokenUtil.generateToken(user);
  }

  public boolean checkToken(String token) {
    String username;

    try {
      username = this.jwtTokenUtil.getUsernameFromToken(token);
    } catch (SignatureException exception) {
      return false;
    }

    UserDetails userDetails = this.loadUserByUsername(username);

    return userDetails != null && this.jwtTokenUtil.validateToken(token, userDetails);
  }
}
