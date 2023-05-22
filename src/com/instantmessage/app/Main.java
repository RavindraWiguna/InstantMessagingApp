package com.instantmessage.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
	private static List<ClientHandler> clients = new ArrayList<>();
	private static List<String> usernames = new ArrayList<>(
			List.of("panggah", "avin", "dalem", "rere", "inggih", "rama", "user", "afiq")
	);
	// private static List<Socket> socket_active = new ArrayList<>();


	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(1235)){
					System.out.println("Server started and listening on port 1235");

					while (true) {
							Socket clientSocket = serverSocket.accept();
							System.out.println("New client connected: " + clientSocket);
							// add socket to list

							ClientHandler clientHandler = new ClientHandler(clientSocket, usernames);
							clients.add(clientHandler);
							clientHandler.start();
					}
			} catch (IOException e) {
					e.printStackTrace();
			}
	}

	// function to close client socket and remove it from the list
	public static void closeSocket(ClientHandler c) {
    synchronized (clients) {
			try {
					c.clientSocket.close();
					clients.remove(c);
			} catch (Exception e) {
					e.printStackTrace();
			}
    }
	}
}
