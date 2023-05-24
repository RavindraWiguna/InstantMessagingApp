package com.client;

import com.instantmessage.app.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MessageSender extends Thread {
    // private Socket socket;
    private ObjectOutputStream oos;
    private String senderName;

    public MessageSender(ObjectOutputStream oos, String senderName) {
        this.senderName = senderName;
        this.oos = oos;
    }

    public void sendNull() {
        try {
            this.oos.writeObject(null);
            this.oos.flush();
        } catch (IOException e) {
            System.err.print(e);
        }
    }

    public void sendToServer(Message msg) {
        try {
            this.oos.writeObject(msg);
            this.oos.flush();
        } catch (IOException e) {
            System.err.print(e);
        }
    }

    // method to close everything in the socket
    public void closeAll(Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    @Override
    public void run() {
        boolean isRun = true;
        Scanner sc = new Scanner(System.in);
        String message = "";
        String finalMsg = "";
        Message sendMsg;
        StringBuilder restOfStringBuilder;
        while (isRun) {
            // System.out.printf("Your message:\n");
            message = sc.nextLine();
            // parse pesan
            String[] words = message.trim().split(" ");
            if (words.length > 0) {
                switch (words[0]) {
                    case "$exit":
                        isRun = false;
                        System.out.print("Exiting\n");
                        sendNull();
                        break;

                    case "$all":
                        // System.out.print("Broadcasting\n");
                        // Concatenating the remaining words
                        restOfStringBuilder = new StringBuilder();
                        for (int i = 1; i < words.length; i++) {
                            restOfStringBuilder.append(words[i]);
                            if (i < words.length - 1) {
                                restOfStringBuilder.append(" ");
                            }
                        }
                        finalMsg = restOfStringBuilder.toString();
                        sendMsg = new Message(this.senderName, "$broadcast", finalMsg);
                        sendToServer(sendMsg);
                        break;

                    case "$pm":
                        // System.out.print("PC sir\n");
                        restOfStringBuilder = new StringBuilder();
                        for (int i = 2; i < words.length; i++) {
                            restOfStringBuilder.append(words[i]);
                            if (i < words.length - 1) {
                                restOfStringBuilder.append(" ");
                            }
                        }
                        finalMsg = restOfStringBuilder.toString();
                        sendMsg = new Message(this.senderName, words[1], finalMsg);
                        sendToServer(sendMsg);
                        break;

                    case "$user":
                        // to be implemented
                        // System.out.print("Check online\n");
                        sendMsg = new Message(this.senderName, "$user", "");
                        sendToServer(sendMsg);
                        break;
                }
            }
        }
    }
}
