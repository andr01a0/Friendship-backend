package com.instantcoffee.tech.service;

import com.instantcoffee.tech.repo.FriendRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FriendService {

    FriendRepo friendRepo;
}
