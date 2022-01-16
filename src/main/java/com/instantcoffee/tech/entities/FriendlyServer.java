package com.instantcoffee.tech.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "friendly_servers")
public class FriendlyServer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "friendly_server_id")
  private long id;

  @Column(name = "jwt_token")
  private String jwtToken;

}
