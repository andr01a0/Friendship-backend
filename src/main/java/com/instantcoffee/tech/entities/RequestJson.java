package com.instantcoffee.tech.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestJson {

  @JsonProperty("request")
  private String request;

}
