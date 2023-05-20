package com.instantmessage.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
  private Socket clientSocket;
  private ObjectOutputStream outputStream;
//   private String username;

  public ClientHandler(Socket clientSocket) {
      this.clientSocket = clientSocket;
  }

  public void run() {
      try {
        System.out.println("Client connected: " + clientSocket.getInetAddress());

        // Create an input stream reader to read messages from the client
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Read the message from the client
        String clientMessage = reader.readLine();
        System.out.println("Message from client: " + clientMessage);

        clientSocket.close();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  public void sendMessage(Message message) {
      try {
          outputStream.writeObject(message);
          outputStream.flush();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
}