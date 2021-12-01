package com.instantcoffee.tech.entities;

import com.instantcoffee.tech.authentication.User;

import javax.persistence.*;

@Entity
@Table(name = "relationships")
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int relationshipId;
    String type;
    String friendEmail;
    long friendIp;

    @ManyToOne
    @JoinColumn(name = "id")
    User user;

}
