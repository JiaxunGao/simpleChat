// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 * 
 */



public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  private int currentClient = 0;
  ChatIF serverUI;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port,ChatIF serverUI) 
  {
    super(port);
    this.serverUI=serverUI;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  
	  
	if (msg.toString().charAt(0)=='#') {
		if (client.getInfo("ID")==null) {
			String[] info = msg.toString().substring(1).split(" ");
			
			client.setInfo("ID", info[1]);
		}else {
			
			try {
				client.close();
				client.sendToClient("Can not modify name while logged in.");
				}
			catch (IOException e) 
			{serverUI.display("Problem in close connection to a client.");}
		}
		
		
	}
	else {
		System.out.println("Message received: " + msg + " from " + client);
		String id = (String)client.getInfo("ID");
		
	    this.sendToAllClients(id+"---"+(String)msg);
	    serverUI.display((String)msg);
	}
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  /*public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }*/
  
  protected void clientConnected(ConnectionToClient client) {
	  currentClient++;
	  
	  System.out.println("A client has been connected.Total connected client: "+ currentClient);
  }
  

  
  synchronized protected void clientException(
		    ConnectionToClient client, Throwable exception) {
	  currentClient--;
	  System.out.println("A client just disconnected. Total connected client: "+ currentClient);
  }
  
  public void handleMessageFromServerUI(String message) {
	  
	  if (message.charAt(0)=='#') {
		  String[] info = message.substring(1).split(" ");
		  
		  switch (info[0]) {
		  	case "quit":
		  		try {
		  			close();
		  			serverUI.display("Successfully closed server.");
		  			
		  		}catch (IOException e) {
		  			serverUI.display("Problem in quiting. Force quit");
		  		}
		  		finally{
		  			System.exit(0);
		  		}
		  		break;
		  	
		  	case "stop":
		  		stopListening();
		  		serverUI.display("Server listening is stopped");
		  		break;
		  		
		  	case "close":
		  		try {
		  			close();
		  			serverUI.display("Successfully closed server.");
		  		}catch (IOException e){
		  			serverUI.display("Problem in closing server.");
		  		}
		  		break;
		  	
		  	case "start":
		  		if (!isListening()) {
		  			try {
			  			listen();
			  		}catch (IOException e){
			  			serverUI.display("Problem in start listening.");
			  		}
		  		}else {
		  			serverUI.display("Server is already istening.");
		  		}
		  		break;
		  	
		  	case "setport":
		  		if (!isListening() && getNumberOfClients()==0) {
		  			if(info.length==2) {
						setPort(Integer.parseInt(info[1]));
						serverUI.display("Set host successfully to :" + info[1]);
					}else {
						serverUI.display("Please provide port paramater.");
					}
		  		}else {
		  			serverUI.display("Please close server first using command #close.");
		  		}
		  		break;
		  	
		  	case "getport":
		  		serverUI.display("Current port is :" + String.valueOf(getPort())  );
		  		break;
		  		
		  	default:
		  		serverUI.display("Can not recognize command");
		  }
		  
		  
	  }else {
		  this.sendToAllClients("SERVER MSG> "+message);
	  }
	  
  }
  
}
//End of EchoServer class
