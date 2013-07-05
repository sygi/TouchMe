package com.example.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class RunServer {

	/**
	 * @param args
	 */
	private static ServerSocket ss;
	public static void main(String[] args) {
		try {
			ss = new ServerSocket(4444);
		} catch (IOException e){
			System.out.printf("Could not listen to port 4444\n");
			System.exit(-1);
		}
		System.out.printf("Starting server on \n");
		try {
			System.out.printf("%s:%d\n", InetAddress.getLocalHost().getHostAddress(), ss.getLocalPort());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Server s = new Server(ss);
		s.estabilishConnection();
		s.listen();
	}

}