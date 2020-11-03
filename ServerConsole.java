import java.util.Scanner;

import common.ChatIF;

public class ServerConsole implements ChatIF {
	
	final public static int DEFAULT_PORT = 5555;
	
	EchoServer server;
	
	Scanner fromConsole; 
	
	@Override
	public void display(String message) {
		// TODO Auto-generated method stub

	}

}
