package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Main {
  public static void main(String[] args) {
    try {
        // Connect to the server
        Socket serverSocket = new Socket("localhost", 1235);
        System.out.println("Connected to server: " + serverSocket.getInetAddress());

        // Create an output stream writer to send a plain text message to the server
        OutputStream outputStream = serverSocket.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        // Send a plain text message to the server
        String clientMessage = "Hello, server! This is the client.";
        writer.write(clientMessage);
        writer.flush(); // Flush the writer to ensure the message is sent immediately

        // Close the writer, reader, and socket
        writer.close();
        // reader.close();
        serverSocket.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
  }
}