package com.instantcoffee.tech.entities;
import com.instantcoffee.tech.authentication.User;

import javax.persistence.*;

@Entity
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int relationshipId;
    String type;
    String friendEmail;
    long friendIp;

    @ManyToOne
    @JoinColumn(name = "userId")
    User user;

}