package com.instantmessage.app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
  private Socket clientSocket;
  private ObjectOutputStream outputStream;
  private String username;

  public ClientHandler(Socket clientSocket) {
      this.clientSocket = clientSocket;
  }

  public void run() {
      try {
          outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
          ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());

          // Perform user authentication
          

          while (true) {
              Message message = (Message) inputStream.readObject();

              // Handle different types of messages (private, broadcast)
              // ...

              // Send response or broadcast the message
              // ...
          }
      } catch (IOException | ClassNotFoundException e) {
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