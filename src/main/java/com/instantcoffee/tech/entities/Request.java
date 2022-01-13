package com.instantcoffee.tech.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {

  private String method;
  private String sourceEmail;
  private String sourceHost;
  private String destinationEmail;
  private String destinationHost;
  private String version;

  public Request(String friendshipProtocol) {
    String[] protocolData = friendshipProtocol.split("\\s+");
    this.method = protocolData[0];
    this.sourceEmail = protocolData[1];
    this.sourceHost = protocolData[2];
    this.destinationEmail = protocolData[3];
    this.destinationHost = protocolData[4];
    this.version = protocolData[5];
  }

  @Override
  public String toString() {
    return getMethod() + " " +
        getSourceEmail() + " " +
        getSourceHost() + " " +
        getDestinationEmail() + " " +
        getDestinationHost() + " " +
        getVersion();
  }

  public String toJson() {
    return "{\"request\": \""+toString()+"\\r\\n\"}";
  }

}
