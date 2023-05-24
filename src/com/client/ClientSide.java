package com.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientSide {
  // private classes for the clien
  private Socket socket;
  private ObjectOutputStream objectOutputStream;
  private BufferedReader buffReader;
  private BufferedWriter buffWriter;
  private String name;

  public ClientSide(Socket socket, String name) {
    try {
      // Constructors of all the private classes
      this.socket = socket;
      this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.name = name;

    } catch (IOException e) {
      // closeAll(socket, buffReader, buffWriter);
    }
  }

  // method to send messages using thread
  public void sendMessage() {
    try {
      buffWriter.write(name);
      buffWriter.newLine();
      buffWriter.flush();

      try (Scanner sc = new Scanner(System.in)) {
        while (socket.isConnected()) {
          String messageToSend = sc.nextLine();
          String appendMessage = name + ": " + messageToSend;
          System.out.print("1" + appendMessage);
          objectOutputStream.writeObject(appendMessage);
          objectOutputStream.flush();
          System.out.print("2");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      // closeAll(socket, buffReader, buffWriter);
    }
  }

  // method to read messages using thread
  public void readMessage() {
    new Thread(new Runnable() {

      @Override
      public void run() {
        try {
          System.out.print("Enter your message: ");
          ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

          while (socket.isConnected()) {
            String message = (String) objectInputStream.readObject();
            System.out.println(message);
          }
        } catch (IOException e) {
          // closeAll(socket, buffReader, buffWriter);
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }

      }

    }).start();
  }

  // method to close everything in the socket
  public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter) {
    try {
      if (buffReader != null) {
        buffReader.close();
      }
      if (buffWriter != null) {
        buffWriter.close();
      }
      if (socket != null) {
        socket.close();
      }
    } catch (IOException e) {
      e.getStackTrace();
    }
  }

  // main method
  public static void main(String[] args) throws UnknownHostException, IOException {
    try (Scanner sc = new Scanner(System.in)) {
      System.out.print("Enter your username:");
      String name = sc.nextLine();
      System.out.println("[ " + name + " has connected!]");

      Socket socket = new Socket("localhost", 1235);
      ClientSide client = new ClientSide(socket, name);

      client.readMessage();
      client.sendMessage();
    }
  }

}
