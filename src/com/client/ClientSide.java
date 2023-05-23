package com.client;

import com.instantmessage.app.Message;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientSide {
  // private classes for the clien
  private Socket socket;
  private BufferedReader buffReader;
  private BufferedWriter buffWriter;
  private String name;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;

  public ClientSide(Socket socket, String name) {
    try {
      // Constructors of all the private classes
      this.socket = socket;
      this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.name = name;
      this.oos = new ObjectOutputStream(socket.getOutputStream());
      this.ois = new ObjectInputStream(socket.getInputStream());

    } catch (IOException e) {
      closeAll(socket);
    }
  }


  public void closeAll(Socket socket) {
    try {
      if (socket != null) {
        socket.close();
      }
    } catch (IOException e) {
      e.getStackTrace();
    }
  }

    // main method
    public static void main (String[]args) throws UnknownHostException, IOException {
      try (Scanner sc = new Scanner(System.in)) {

        // GET NAME
        String name = "$";
        while(name.startsWith("$")){
          System.out.print("Enter your username:");
          name = sc.nextLine();
          if(name.startsWith("$")){
            System.out.printf("Name cannot start with '$'\n");
          }
        }

        System.out.println("[ " + name + " has connected!]");

        System.out.printf("Type $user to see users that are currently online.\n");
        System.out.printf("Type $all [message] to do broadcast.\n");
        System.out.printf("Type $pm [name] [message] to do private message.\n");
        System.out.printf("Type $exit to disconnect.\n");

        // START CONNECTION
        Socket socket = new Socket("localhost", 1235);
        ClientSide client = new ClientSide(socket, name);

        // kasi tau nama
        Message init = new Message(name, "", "");
        client.oos.writeObject(init);
        client.oos.flush();
        // ?
//        client.oos.close();
//        client.ois.close();

//        System.out.printf("Finished init name\n");

        // loop app
//        System.out.printf("Creating listener\n");
        MessageListener bl = new MessageListener(client.ois);
//        System.out.printf("Starting listener\n");
        bl.start();
//        System.out.printf("Finish starting listener, creating sender\n");
        MessageSender ms = new MessageSender(client.oos, client.name);
//        System.out.printf("Starting sender\n");
        ms.start();
//        System.out.printf("Finish starting sender\n");
        try{
          ms.join();
          bl.join();
        }
        catch (InterruptedException e){
          client.closeAll(client.socket);
        }
    }
  }
}

//  private int askAction() {
//    System.out.printf("[ MAIN MENU ]\n");
//    System.out.printf("Hello %s, choose your action!\n");
//    System.out.printf("1. Send broadcast messages.\n");
//    System.out.printf("2. See online user.\n");
//    System.out.printf("3. Send private messages.\n");
//    System.out.printf("4. Disconnect.\n");
//
//    try  {
//      Scanner sc = new Scanner(System.in);
//      int action = 5;
//      while (action > 4) {
//        action = sc.nextInt();
//        if (action > 4) {
//          System.out.printf("I don't recognize the command you gave me, please type it again.\n");
//        } else {
//          System.out.printf("Received: %d\n", action);
//        }
//      }
//      return action;
//    }
//    catch (Exception ex){
//      System.err.print(ex);
//    }
//    return -1;
//  }




// method to send messages using thread
//  public void sendMessage() {
//    try {
//      buffWriter.write(name);
//      buffWriter.newLine();
//      buffWriter.flush();
//
//      try (Scanner sc = new Scanner(System.in)) {
//        while (socket.isConnected()) {
//          String messageToSend = sc.nextLine();
//          buffWriter.write(name + ": " + messageToSend);
//          buffWriter.newLine();
//          buffWriter.flush();
//
//        }
//      }
//    } catch (IOException e) {
//      closeAll(socket, buffReader, buffWriter);
//
//    }
//  }

// method to read messages using thread
//  public void readMessage() {
//    new Thread(new Runnable() {
//
//      @Override
//      public void run() {
//        String msgFromGroupChat;
//
//        while (socket.isConnected()) {
//          try {
//            System.out.print("Enter your message: ");
//            msgFromGroupChat = buffReader.readLine();
//            System.out.println(msgFromGroupChat);
//          } catch (IOException e) {
//            closeAll(socket, buffReader, buffWriter);
//          }
//
//        }
//
//      }
//
//    }).start();
//  }

// method to close everything in the socket
//  public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter) {
//    try {
//      if (buffReader != null) {
//        buffReader.close();
//      }
//      if (buffWriter != null) {
//        buffWriter.close();
//      }
//      if (socket != null) {
//        socket.close();
//      }
//    } catch (IOException e) {
//      e.getStackTrace();
//    }
//  }
