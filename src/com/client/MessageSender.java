package com.client;

import com.instantmessage.app.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MessageSender extends Thread{
//    private Socket socket;
    private ObjectOutputStream oos;
    private String senderName;

    public MessageSender(ObjectOutputStream oos, String senderName){
//        try{
//            this.socket = socket;
//            this.oos = new ObjectOutputStream(new socket.getOutputStream());
            this.senderName = senderName;
            this.oos = oos;
//        }
//        catch (IOException e){
//            System.out.print("Error di message sender\n");
//            closeAll(this.socket);
//        }
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
    public void run(){
        try{
            boolean isRun = true;
            Scanner sc = new Scanner(System.in);
            String message  = "";
            String finalMsg = "";
            Message sendMsg;
            StringBuilder restOfStringBuilder;
            while(isRun){
                System.out.printf("Your message:\n");
                message = sc.nextLine();
                // parse pesan
                String[] words = message.trim().split(" ");
                if(words.length > 0){
                    switch (words[0]){
                        case "$exit":
                            isRun=false;
                            System.out.print("Exiting\n");
                            break;

                        case "$all":
                            System.out.print("Broadcasting\n");
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
                            oos.writeObject(sendMsg);
                            oos.flush();
                            break;

                        case "$pm":
                            System.out.print("PC sir\n");
                            restOfStringBuilder = new StringBuilder();
                            for (int i = 2; i < words.length; i++) {
                                restOfStringBuilder.append(words[i]);
                                if (i < words.length - 1) {
                                    restOfStringBuilder.append(" ");
                                }
                            }
                            finalMsg = restOfStringBuilder.toString();
                            sendMsg = new Message(this.senderName, words[1], finalMsg);
                            oos.writeObject(sendMsg);
                            oos.flush();
                            break;

                        case "$user":
                            // to be implemented
                            System.out.print("Check online\n");
                            break;
                    }
                }



            }
        }
        catch (IOException e){
//            closeAll(this.socket);
            System.err.print(e);
        }


    }

}
