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

        Person person = new Person("John Doe", 30);
        ObjectOutputStream ous = new ObjectOutputStream(serverSocket.getOutputStream());
        ous.writeObject(person);
        ous.flush();
        ous.close();
        serverSocket.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
  }
}