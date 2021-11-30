package com.instantcoffee.tech.entities;

import javax.persistence.*;

@Entity
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int friendId;
    String friendEmail;
    String userEmail;
    boolean pending;
    long friendIp;

    @ManyToOne
    @JoinColumn(name = "userId")
    User user;

}
