package com.client;

import com.instantmessage.app.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class MessageListener extends Thread {
    // private Socket socket;
    private ObjectInputStream ois;

    public MessageListener(ObjectInputStream ois) {
        this.ois = ois;

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
        Object obj;
        try {
            // kayanya di stop dari client bilang $exit
            while ((obj = ois.readObject()) != null) {
                if (obj instanceof Message) {
                    Message msg = (Message) obj;
                    msg.printMessage();
                } else {
                    System.out.println("Received unknown object from le server: " + obj);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // closeAll(this.socket);
            System.err.print(e);
        }

    }
}
