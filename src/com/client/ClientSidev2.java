package com.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import com.instantmessage.app.Message;

import java.io.*;

public class ClientSidev2 {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String name;

    public ClientSidev2(Socket socket, String name) {
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.name = name;

        } catch (IOException e) {
            closeAll(socket, buffReader, buffWriter);
        }
    }

    public void sendMessage() {
        try {
            buffWriter.write(name);
            buffWriter.newLine();
            buffWriter.flush();

            try (Scanner sc = new Scanner(System.in)) {
                while (socket.isConnected()) {
                    String messageToSend = sc.nextLine();
                    Message message = new Message(name, messageToSend);
                    objectOutputStream.writeObject(message);
                    objectOutputStream.flush();
                }
            }
        } catch (IOException e) {
            closeAll(socket, buffReader, buffWriter);
        }
    }

    // method to read messages using thread
    public void readMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                    while (socket.isConnected()) {
                        String message = (String) objectInputStream.readObject();
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    closeAll(socket, buffReader, buffWriter);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

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

    public static void main(String[] args) throws IOException {
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
