package com.instantcoffee.tech.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int userId;
    String email;
    String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    List<Relationship> relationships;


}
