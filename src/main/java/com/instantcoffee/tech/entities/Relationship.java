package com.instantcoffee.tech.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
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
