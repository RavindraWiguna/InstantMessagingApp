package com.instantmessage.app;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerSide {
	private ServerSocket serverSocket;
	private List<ClientHandler> clients = new ArrayList<>();

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
				ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
				clients.add(clientHandler);
				clientHandler.start();
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


//		ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
//		ServerSocket ss = ssf.createServerSocket(1235);
//		SSLServerSocket sslServerSocket = (SSLServerSocket) ss;
//		sslServerSocket.setEnabledProtocols(new String[] {"TLSv1.2"});
////		sslServerSocket.setEnabledCipherSuites(new String[] {"TLS_RSA_WITH_AES_128_GCM_SHA256"});
//		String[] supportedCipherSuites = sslServerSocket.getSupportedCipherSuites();
//		System.out.println("Supported Cipher Suites on Server: " + Arrays.toString(supportedCipherSuites));
//		ServerSide server = new ServerSide(sslServerSocket);
//		server.serverStart();
	}
}
