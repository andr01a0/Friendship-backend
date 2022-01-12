package com.instantcoffee.tech.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Response {

  private String version;
  private int statusCode;
  private String phrase;

  @Override
  public String toString() {
    return getVersion() + " " +
        getStatusCode() + " " +
        getPhrase() + "\r\n";
  }

}
