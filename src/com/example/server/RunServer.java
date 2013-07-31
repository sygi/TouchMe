package com.example.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Enumeration;

public class RunServer {

	/**
	 * @param args
	 */
	private static ServerSocket ss;

	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		try {
			ss = new ServerSocket(4444);
		} catch (IOException e) {
			System.out.printf("Could not listen to port 4444\n");
			System.exit(-1);
		}
		System.out.printf("Starting server on \n");
		try {
			NetworkInterface wifiInterface = NetworkInterface
					.getByName("wlan0");
			Enumeration<InetAddress> addresses = wifiInterface
					.getInetAddresses();
			InetAddress addr = addresses.nextElement();
			System.out.printf("%s:%d\n", addr.getHostAddress(),
					ss.getLocalPort());
		} catch (SocketException e) {
			e.printStackTrace();
		}
		Server s = new Server(ss);
		s.estabilishConnection();
		s.listen();
	}
}