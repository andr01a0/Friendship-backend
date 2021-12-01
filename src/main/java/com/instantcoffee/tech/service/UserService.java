package com.instantcoffee.tech.service;

import com.instantcoffee.tech.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    UserRepo userRepo;

}
