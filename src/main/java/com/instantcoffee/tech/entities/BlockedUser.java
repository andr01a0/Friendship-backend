package com.instantcoffee.tech.entities;

import javax.persistence.*;

@Entity
public class BlockedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int blockedId;
    String blockedEmail;
    String userEmail;
    long blockedIp;

    @ManyToOne
    @JoinColumn(name = "userId")
    User user;
}
