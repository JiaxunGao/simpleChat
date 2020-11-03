import java.io.IOException;
import java.util.Scanner;

import client.ChatClient;
import common.ChatIF;

public class ServerConsole implements ChatIF {
	
	final public static int DEFAULT_PORT = 5555;
	
	EchoServer server;
	
	Scanner fromConsole; 
	
	
	
	
	public ServerConsole(int port) 
	  {
	    
	    server= new EchoServer( port,this);
	      
	      
	    try 
	    {
	      server.listen(); 
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("ERROR - Could not listen for clients!"); 
	    
	    }
	    // Create scanner object to read from console
	    fromConsole = new Scanner(System.in); 
	  }
	
	@Override
	public void display(String message) {
		System.out.println("> " + message);

	}
	
	public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        server.handleMessageFromServerUI(message);
	        if (message.charAt(0)!='#') {
	        	display("SERVER MSG>"+message);
	        }
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }
	
	public static void main(String[] args) 
	  {
	    int port = 0;


	    try
	    {
	      port = Integer.parseInt(args[0]);
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {

	      port = DEFAULT_PORT;
	    }
	    ServerConsole chat= new ServerConsole( port);
	    chat.accept();  //Wait for console data
	  }
	

}
