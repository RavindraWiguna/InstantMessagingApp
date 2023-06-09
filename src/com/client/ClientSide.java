package com.client;

import com.instantmessage.app.Message;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
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
      System.err.print(e);
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
  public static void main(String[] args) throws UnknownHostException, IOException {
    try (Scanner sc = new Scanner(System.in)) {

      // GET NAME
      String name = "$";
      boolean isCorrect = false;
      while (!isCorrect) {
        System.out.print("Enter your username:");
        name = sc.nextLine();
        if (name.startsWith("$")) {
          System.out.printf("Name cannot start with '$'\n");
        }
        else if(name.contains(" ")){
          System.out.printf("Name cannot contain space ' '\n");
        }
        else{
          isCorrect=true;
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

//      SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
//      SSLSocket socket = (SSLSocket) factory.createSocket("localhost", 1235);
//      socket.setEnabledProtocols(new String[] {"TLSv1.2"});
//      socket.setEnabledCipherSuites(new String[] {"TLS_RSA_WITH_AES_128_CBC_SHA256"});
//      ClientSide client = new ClientSide(socket, name);

      // kasi tau nama
      Message init = new Message(name, "", "");
      client.oos.writeObject(init);
      client.oos.flush();
      MessageListener bl = new MessageListener(client.ois);
      bl.start();
      MessageSender ms = new MessageSender(client.oos, client.name);
      ms.start();
      try {
        ms.join();
        bl.join();
      } catch (InterruptedException e) {
        client.closeAll(client.socket);
      }
    }
  }
}
