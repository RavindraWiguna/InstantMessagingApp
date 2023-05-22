package com.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.serialization.Login;
import com.serialization.Person;

public class Main {
  private static Socket serverSocket;
  private static String username;
  public static void main(String[] args) {
    try {
        // Connect to the server
        serverSocket = new Socket("localhost", 1235);
        System.out.println("Connected to server: " + serverSocket.getInetAddress());
        // login
        // input username
        // receive status
        Login login = new Login();
        // inputstream from keyboard in variable username
        Scanner scanner = new Scanner(System.in);
        username = scanner.nextLine();
        scanner.close();

        login.setUsername(username);
        login.setStatusCode(0);
        sendLogin(login);
        // if status is 200, then show menu
        // if status is 400, then ask for another username
        

        // always give code status to read or write
        // then show menu
        // > listen chat
        // general or private, give a sign
        // > write chat
        // write then send to anyone who online or general 
        // > see online user

        // Person person = new Person("John-Doe-Client", 35);
        // sendMessage(person);
        
        // // receive the message from the server
        // ObjectInputStream ois = new ObjectInputStream(serverSocket.getInputStream());
        // Person personFromServer = (Person) ois.readObject();
        // System.out.println("Received: " + personFromServer.getFullName() + " " + personFromServer.getAge());

        // ois.close();
        serverSocket.close();
    } catch (IOException e) {
        e.printStackTrace();
    } 
  }

  public static void sendMessage(Person message) {
      try {
          ObjectOutputStream ous = new ObjectOutputStream(serverSocket.getOutputStream());
          ous.writeObject(message);
          ous.flush();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  public static void sendLogin(Login l) {
    try {
        ObjectOutputStream ous = new ObjectOutputStream(serverSocket.getOutputStream());
        ous.writeObject(l);
        ous.flush();
    } catch (IOException e) {
        e.printStackTrace();
    }
  }

  // declare class message
  // public static class Message implements Serializable {
  //     private String from;
  //     private String to;
  //     private String content;
  //     private Date timestamp;
  //
  //     public Message(String from, String to, String content) {
  //         this.from = from;
  //         this.to = to;
  //         this.content = content;
  //         this.timestamp = new Date();
  //     }
  //
  //     public String getFrom() {
  //         return from;
  //     }
  //
  //     public String getTo() {
  //         return to;
  //     }
  //
  //     public String getContent() {
  //         return content;
  //     }
  //
  //     public Date getTimestamp() {
  //         return timestamp;
  //     }
  // }
}