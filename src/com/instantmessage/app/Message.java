package com.instantmessage.app;

import java.io.Serializable;

public class Message implements Serializable {
  private String senderName;
  private String message;
  private boolean isBroadcast;
  private String receiverName;

  public Message(String senderName, String receiverName, String message){
    this.senderName = senderName;
    this.message = message;
    this.receiverName = receiverName;
    this.isBroadcast=false;
    if(receiverName.startsWith("$")){
      this.isBroadcast = true;
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

  public void printMessage(){
    if(isBroadcast){
      System.out.printf("[All][%s]:%s\n", this.senderName, this.message);
    }else{
      System.out.printf("[Private][%s]:%s\n", this.senderName, this.message);
    }

  }
}
