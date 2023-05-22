package com.instantmessage.app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.serialization.Login;
import com.serialization.Person;

public class ClientHandler extends Thread {
  public Socket clientSocket;
  private ObjectOutputStream outputStream;
	private List<String> usernames = new ArrayList<>();
  private String username;

  public ClientHandler(Socket clientSocket, List<String> usernames) {
      this.clientSocket = clientSocket;
      this.usernames = usernames;
      // print this.usernames
      // System.out.println(this.usernames);
  }

  public void run() {
      try {
        System.out.println("Client connected: " + clientSocket.getInetAddress());

        // Read the message from the client
        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
        Login login = (Login) ois.readObject();
        // check is username exist
        boolean isUsernameExist = false;
        for(String username : usernames) {
          if (username.equals(login.getUsername())) {
            login.setStatusCode(400);
            isUsernameExist = true;
            break;
          }
        }

        if (isUsernameExist) {
          login.setStatusCode(400);
          System.out.println("User " + login.getUsername() + " not exist");
          return;
        } else {
          login.setStatusCode(200);
          System.out.println("User " + login.getUsername() + " connected");
          usernames.add(login.getUsername());
        }
        // Person person = (Person) ois.readObject();
        // System.out.println("Received: " + person.getFullName() + " " + person.getAge());

        // person.setFullName("Iam the server dude");
        // person.setAge(1000);

        // Send the message back to the client
        // System.out.println("Sending: " + person.getFullName() + " " + person.getAge());
        // outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        // outputStream.writeObject(person);
        // outputStream.flush();

        // outputStream.close();
        ois.close();
        // clientSocket.close();
      } catch (IOException e) {
          e.printStackTrace();
      } catch (ClassNotFoundException e) {
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