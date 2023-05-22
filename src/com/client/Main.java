package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.serialization.Person;

public class Main {
  public static void main(String[] args) {
    try {
      // Connect to the server
      Socket serverSocket = new Socket("localhost", 1235);
      System.out.println("Connected to server: " + serverSocket.getInetAddress());

      // Get username input from the user
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      System.out.print("Enter username: ");
      String username = reader.readLine();

      // Create a Person object with the username and age
      Person person = new Person(username);

      // Send the Person object to the server
      ObjectOutputStream ous = new ObjectOutputStream(serverSocket.getOutputStream());
      ous.writeObject(person);
      ous.flush();
      ous.close();

      // Close the socket
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
