package com.instantmessage.app;

import java.io.Serializable;

public class Message implements Serializable {
  private static final long serialVersionUID = 1L;
  private String username;
  private String message;

  public Message(String username, String message) {
    this.username = username;
    this.message = message;
  }

  public String getUsername() {
    return this.username;
  }

  public String getMessage() {
    return this.message;
  }
}
