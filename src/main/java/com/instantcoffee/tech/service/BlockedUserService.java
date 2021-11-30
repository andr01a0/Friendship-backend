package com.instantcoffee.tech.service;

import com.instantcoffee.tech.repo.BlockedRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BlockedUserService {

    BlockedRepo blockedRepo;
}
