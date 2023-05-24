package com.instantmessage.app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandlerv2 extends Thread {
    public static ArrayList<ClientHandlerv2> clientHandlers = new ArrayList<>();
    public Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String name;

    public ClientHandlerv2(Socket socket) {
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.name = (String) objectInputStream.readObject();
            clientHandlers.add(this);
            broadcastMessage("[ " + name + " has connected!]");
        } catch (IOException | ClassNotFoundException e) {
            closeAll(socket, objectInputStream, objectOutputStream);
        }
    }

    public void run() {
        try {
            while (socket.isConnected()) {
                String messageFromClient = (String) objectInputStream.readObject();
                broadcastMessage(messageFromClient);
            }
        } catch (IOException | ClassNotFoundException e) {
            closeAll(socket, objectInputStream, objectOutputStream);
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandlerv2 clientHandlerv2 : clientHandlers) {
            try {
                if (!clientHandlerv2.name.equals(name)) {
                    clientHandlerv2.objectOutputStream.writeObject(messageToSend);
                    clientHandlerv2.objectOutputStream.flush();
                }
            } catch (IOException e) {
                closeAll(socket, objectInputStream, objectOutputStream);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("[ " + name + " has disconnected!]");
    }

    public void closeAll(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        removeClientHandler();
        try {
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}
