package com.instantmessage.app;

import java.io.Serializable;

public class Message implements Serializable {
  private String senderName;
  private String message;
  private boolean isBroadcast;
  private boolean isCheckOnline;
  private String receiverName;

  public Message(String senderName, String receiverName, String message){
    this.senderName = senderName;
    this.message = message;
    this.receiverName = receiverName;
    this.isBroadcast=false;
    this.isCheckOnline=false;
    if(receiverName.equals("$broadcast")){
      this.isBroadcast = true;
    }
    if(receiverName.equals("$user")){
      this.isCheckOnline=true;
    }
  }

  public String getSenderName() {
    return senderName;
  }

  public String getMessage() {
    return message;
  }

  public boolean isBroadcast() {
    return isBroadcast;
  }

  public String getReceiverName() {
    return receiverName;
  }

  public boolean isCheckOnline() {
    return isCheckOnline;
  }

  public void printMessage(){
    if(isCheckOnline){
      // sender is the list name
      System.out.printf("[%s] is online.\n", this.senderName);
      return;
    }

    if(isBroadcast){
      System.out.printf("[All][%s]:%s\n", this.senderName, this.message);
    }else{
      System.out.printf("[Private][%s]:%s\n", this.senderName, this.message);
    }

  }
}
