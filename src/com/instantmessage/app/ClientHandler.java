package com.instantmessage.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {

  public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
  public Socket socket;
  private BufferedReader buffReader;
  private BufferedWriter buffWriter;
  private ObjectInputStream objectInputStream;
  private ObjectOutputStream objectOutputStream;
  private String name;

  public ClientHandler(Socket socket) {
    // Constructors of all the private classes
    try {
      this.socket = socket;
      this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      this.objectInputStream = new ObjectInputStream(socket.getInputStream());
      System.out.println("a");
      this.name = (String) objectInputStream.readObject();
      System.out.println("b");
      clientHandlers.add(this);
      broadcastMessage("[ " + name + " has connected!]");

    } catch (IOException e) {
      // closeAll(socket, buffReader, buffWriter);
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void run() {

    while (socket.isConnected()) {
      try {
        System.out.print("1");
        String messageFromClient = (String) objectInputStream.readObject();
        System.out.println(messageFromClient);
        broadcastMessage(messageFromClient);
      } catch (IOException e) {
        // closeAll(socket, buffReader, buffWriter);
        break;
      } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void broadcastMessage(String messageToSend) {
    for (ClientHandler clientHandler : clientHandlers) {
      try {
        if (!clientHandler.name.equals(name)) {
          clientHandler.objectOutputStream.writeObject(messageToSend);
          clientHandler.objectOutputStream.flush();
        }
      } catch (IOException e) {
        // closeAll(socket, buffReader, buffWriter);

      }
    }
  }

  // notify if the user left the chat
  public void removeClientHandler() {
    clientHandlers.remove(this);
    broadcastMessage("[ " + name + " has disconnected!]");
  }

  public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter) {

    // handle the removeClient funciton
    removeClientHandler();
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

}
