package com.instantmessage.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
	private static List<ClientHandler> clients = new ArrayList<>();

	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(1235)) {
			System.out.println("Server started and listening on port 1235");

			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("New client connected: " + clientSocket);

				ClientHandler clientHandler = new ClientHandler(clientSocket);
				clients.add(clientHandler);
				clientHandler.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
