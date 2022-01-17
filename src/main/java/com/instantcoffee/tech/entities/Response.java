package com.instantcoffee.tech.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
@RequiredArgsConstructor
public class Response {

  private String version;
  private int statusCode;
  private String phrase;

  public Response(String version, int statusCode, String phrase) {
    this.version = version;
    this.statusCode = statusCode;
    this.phrase = phrase;
  }

  public Response(String friendshipProtocol) {
    String[] protocolData = friendshipProtocol.split("\\s+");
    this.version = protocolData[0];
    this.statusCode = Integer.parseInt(protocolData[1]);
    this.phrase = String.join(" ", Arrays.copyOfRange(protocolData, 2, protocolData.length));
  }

  @Override
  public String toString() {
    return getVersion() + " " +
        getStatusCode() + " " +
        getPhrase();
  }

  public String toJson() {
    return "{\"response\": \""+toString()+"\\r\\n\"}";
  }

}
