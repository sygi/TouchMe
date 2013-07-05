package com.example.touchme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class Connection {
	private Socket s;
	private PrintWriter out;
	private BufferedReader in;
	private String serverIP;
	private int port;
	private void connect(){
		try {
		s = new Socket(serverIP, port);
		} catch (UnknownHostException e){
			System.out.printf("Unknown host\n");
		} catch (IOException e) {
			System.out.printf("IOException");
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("Inny dziwny blad");
		}
		try {
		out = new PrintWriter(s.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e){
			System.out.printf("Error creating i/o from socket\n");
		}
		send("connected");
	}
	public Connection(String IP, int port){
		serverIP = IP;
		this.port = port;
		connect();
	}
	public void send(String command){
		Log.d("sygi", "wysylam " + command);
		out.println(command);
		try {
			while (in.ready()){
				Log.d("sygi", in.readLine());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
