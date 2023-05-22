package com.serialization;

import java.io.Serializable;

public class Login implements Serializable {
  private String username;
  private int status_code;

  public Login(){}

  public String getUsername(){
    return this.username;
  }

  public void setUsername(String username){
    this.username = username;
  }

  public int getStatusCode(){
    return this.status_code;
  }

  public void setStatusCode(int status_code){
    this.status_code = status_code;
  }
}
