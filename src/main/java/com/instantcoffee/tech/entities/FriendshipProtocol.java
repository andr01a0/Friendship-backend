package com.instantcoffee.tech.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendshipProtocol {

  private String method;
  private String sourceEmail;
  private String sourceHost;
  private String destinationEmail;
  private String destinationHost;
  private String version;

  public FriendshipProtocol(String friendshipProtocol) {
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
    return this.method + " " +
        this.sourceEmail + " " +
        this.sourceHost + " " +
        this.destinationEmail + " " +
        this.destinationHost + " " +
        this.version + "\r\n";
  }

}
