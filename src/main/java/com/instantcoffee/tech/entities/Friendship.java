package com.instantcoffee.tech.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int relationshipId;
    String type;
    String friendEmail;
    String friendHost;

    @ManyToOne
    @JoinColumn(name = "userId")
    User user;

}
