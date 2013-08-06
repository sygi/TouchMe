package com.example.server;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	Socket client;
	PrintWriter out;
	BufferedReader in;
	ServerSocket ss;
	Robot robot;
	Double lastX, lastY;

	public Server(ServerSocket serverS) {
		ss = serverS;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void estabilishConnection() {
		System.out.printf("Waiting for client\n");
		try {
			client = ss.accept();
		} catch (IOException e) {
			System.out.printf("Could not accept connection\n");
			System.exit(-1);
		}
		try {
			out = new PrintWriter(client.getOutputStream(), true); // autoFlush
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			System.out.printf("Error connecting to socket in/out streams\n");
			System.exit(-1);
		}
	}

	private Double getX(String command) {
		String pos[] = command.split("[xy]:");
		return Double.valueOf(pos[1]);
	}

	private Double getY(String command) {
		String pos[] = command.split("[xy]:");
		return Double.valueOf(pos[2]);
	}

	private void serve(String command) {
		if (command.matches("down.*")) {
			System.out.printf("Pressed down\n");
			lastX = getX(command);
			lastY = getY(command);
		} else if (command.matches("move.*")) {
			System.out.printf("moving\n");
			Point actual = MouseInfo.getPointerInfo().getLocation();
			robot.mouseMove(actual.x + (int) (6.0 * getY(command)), actual.y - (int) (6.0 * getX(command)));
			lastX = getX(command);
			lastY = getY(command);
		} else if (command.equals("click")) {
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		} else if (command.equals("connected")) {
			out.println("ok");
		} else if (command.equals("disconnect")) {
			System.out.printf("Client disconnected, waiting for another one\n");
		} else {
			System.out.printf("Unknown command: %s\n", command);
		}
	}

	public void listen() {
		System.out.printf("Waiting for messages\n");
		String msg;
		String lastMsg = "";
		long whenLast = System.currentTimeMillis(), whenAct;
		try {
			while ((msg = in.readLine()) != null) {
				System.out.printf("get: %s\n", msg);
				whenAct = System.currentTimeMillis();
				if (!msg.equals("up") && !lastMsg.equals("") && !lastMsg.equals("up") && whenAct - whenLast > 10)
					serve(lastMsg);
				if (msg.equals("click")) {
					serve(msg);
					msg = "";
				}
				System.out.printf("Time diffr: %d\n", whenAct - whenLast);
				lastMsg = msg;
				whenLast = whenAct;
			}
		} catch (IOException e) {
		}
		System.out.printf("client disconnected\n");
		estabilishConnection();
		listen();
	}
}