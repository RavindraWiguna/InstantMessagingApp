package com.instantmessage.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.serialization.Person;

public class ClientHandler extends Thread {
  private Socket clientSocket;
  private ObjectOutputStream outputStream;
  // private String username;

  public ClientHandler(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  public void run() {
    try {
      System.out.println("Client connected: " + clientSocket.getInetAddress());

      // Read the message from the client
      ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
      Person person = (Person) ois.readObject();

      System.out.println("Received: " + person.getFullName());
      ois.close();
      clientSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void sendMessage(Message message) {
    try {
      // Send the message to the client
      outputStream.writeObject(message);
      outputStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}