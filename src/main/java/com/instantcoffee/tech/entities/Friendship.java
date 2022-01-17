package com.instantcoffee.tech.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "friendships")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendship_id")
    private long id;

    @Column(name = "status")
    private String status;

    @Column(name = "friend_email")
    private String friendEmail;

    @Column(name = "friend_host")
    private String friendHost;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

}
