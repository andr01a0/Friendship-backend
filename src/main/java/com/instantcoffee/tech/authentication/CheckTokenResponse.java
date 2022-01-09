package com.instantcoffee.tech.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CheckTokenResponse {
    private String tokenStatus;
}
