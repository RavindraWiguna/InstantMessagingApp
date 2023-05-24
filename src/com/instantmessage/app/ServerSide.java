package com.instantmessage.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerSide {
	private ServerSocket serverSocket;
	private static List<ClientHandler> clients = new ArrayList<>();

	// Constructor
	public ServerSide(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void serverStart() {
		try {
			System.out.println("[Server started and listening on port 1235]");
			// this will keep the server running
			while (!serverSocket.isClosed()) {
				Socket clientSocket = serverSocket.accept();
				ClientHandler clientHandler = new ClientHandler(clientSocket);
				clients.add(clientHandler);
				clientHandler.start();
				System.out.println("Client connected: " + clientSocket.getInetAddress());
			}
		} catch (IOException e) {

		}
	}

	public void closerServer() {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(1235);
		ServerSide server = new ServerSide(serverSocket);
		server.serverStart();
	}
}
