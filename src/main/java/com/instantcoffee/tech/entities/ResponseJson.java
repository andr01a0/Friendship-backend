package com.instantcoffee.tech.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseJson {

  @JsonProperty("response")
  private String response;

}