package com.instantcoffee.tech.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

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
    System.out.println(Arrays.toString(protocolData));
  }

}
