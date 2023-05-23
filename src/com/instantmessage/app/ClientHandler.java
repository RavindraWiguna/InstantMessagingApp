package com.instantmessage.app;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ClientHandler extends Thread {

  public Socket socket;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private BufferedReader buffReader;
  private BufferedWriter buffWriter;
  private String name;

  private ReentrantLock mutex;
  private List<ClientHandler> clients;

  public ClientHandler(Socket socket, List<ClientHandler> clients) {
    // Constructors of all the private classes
    try {
      this.socket = socket;
      this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      this.ois = new ObjectInputStream(socket.getInputStream());
      this.oos = new ObjectOutputStream(socket.getOutputStream());
      this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      // baca nama
      Object obj = ois.readObject();
      if(obj instanceof Message){
        Message init = (Message) obj;
        this.name = init.getSenderName();
        System.out.printf("Connected to user: %s\n", this.name);
      }
      else {
        System.out.println("Received unknown object from le client: " + obj);
      }

      this.mutex = new ReentrantLock();
      this.clients = clients;

    } catch (IOException | ClassNotFoundException e) {
      closeAll(socket);
    }
  }

  @Override
  public void run(){
    Object obj;
    try{
      // loop baca kiriman si client
      while ((obj = ois.readObject()) != null) {

        if(obj instanceof Message){
          Message msg = (Message) obj;
          System.out.printf("Ok got msg, sender from: %s, to %s, msg:%s\n", msg.getSenderName(), msg.getReceiverName(), msg.getMessage());

          // cek ini pesan ke broadcast apa ke private
          if(msg.isBroadcast()){
            // broadcast kirim ke semua
            for (ClientHandler cl : clients) {
              this.mutex.lock(); // lok dulu this client
              cl.sendMessage(msg);
              this.mutex.unlock();
            }
          }

          else{
            // ok ini berarti private meseji, kirim ke penerima sesuai
//            System.out.printf("Not implemented yet\n");
            for(ClientHandler cl : clients){
              if(cl.name.equals(msg.getReceiverName())){
                this.mutex.lock();
                cl.sendMessage(msg);
                this.mutex.unlock();
                break;
              }


            }
          }
        }

        else{
          System.out.println("Received unknown object from le client: " + obj);
        }
      }
    }

    catch (IOException | ClassNotFoundException e){
      closeAll(this.socket);
    }
  }


  public void sendMessage(Message msg) {
    // kirim ke clientnya ini ada broadcast meseji
    try{
      this.oos.writeObject(msg);
      this.oos.flush();
    }
    catch (IOException e){
      closeAll(this.socket);
    }
  }


  public void closeAll(Socket socket) {

    // handle the removeClient funciton
    // PENTING REMOVE SELF YAAA
//    removeClientHandler();
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

}

//  public void run() {
//
//    String messageFromClient;
//
//    while (socket.isConnected()) {
//      try {
//        messageFromClient = buffReader.readLine();
//        boradcastMessage(messageFromClient);
//      } catch (IOException e) {
//        closeAll(socket, buffReader, buffWriter);
//        break;
//      }
//    }
//  }

// notify if the user left the chat
//  public void removeClientHandler() {
//    clientHandlers.remove(this);
//    boradcastMessage("[ " + name + " has disconnected!]");
//  }
