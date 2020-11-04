// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    try {
    	openConnection();
    }catch (IOException e) {
    	clientUI.display("No server with this port number " + String.valueOf(port));
    }
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
	if (message.charAt(0)=='#'){
		
		String[] info = message.substring(1).split(" ");
		
		switch (info[0]) {
			case "quit": 
				quit();
				clientUI.display("Connection closed. Successfully quitted.");
				System.exit(0);
				break;
				
			case "logoff":
				try{
					closeConnection();
					clientUI.display("Successfully logoff.");
				}catch(IOException e)
			    {
			    	clientUI.display
			        ("Problem in loging off.  Terminating client.");
			    	System.exit(0);
			    }
				break;
				
			case "sethost":
				if(!isConnected()) {
					if(info.length==2) {
						setHost(info[1]);
						clientUI.display("Set host successfully to :" + info[1]);
					}else {
						clientUI.display("Please provide host paramater.");
					}
				}else {
					clientUI.display("Please logoff first.");
				} 
				break;
				
			case "setport":
				if(!isConnected()) {
					if(info.length==2) {
						setPort(Integer.parseInt(info[1]));
						clientUI.display("Set port suffessfully to :"+ info[1]);
					}else {
						clientUI.display("Please provide port paramater.");
					}
					
				}else {
					clientUI.display("Please logout first.");
				}
				break;
				
			case "login":
				if(info.length==2) {
					try
				    {
				    	openConnection();
				    	sendToServer(message);
				    }
				    catch(IOException e)
				    {
				    	clientUI.display
				        ("Could not send message to server.  Terminating client.");
				      quit();
				    }
				}else {
					if(!isConnected()) {
						try{
							openConnection();
							clientUI.display("Successfully login.");
						}catch(IOException e)
					    {
					    	clientUI.display
					        ("Problem in loging in. Could not find server. ");
					    	
					    }
						
						
					}else {
						clientUI.display("You have been logged in.");
					}
					
				}
				
				break;
				
			case "gethost":
				clientUI.display("Current host is: " + getHost());
				break;
				
			case "getport":
				clientUI.display("Current port is: " + String.valueOf(getPort()));
				break;
				
			default:
				clientUI.display("can not recognize command.");
		}
		
	}else {
		try
	    {
	    	
	    	sendToServer(message);
	    }
	    catch(IOException e)
	    {
	    	clientUI.display
	        ("Could not send message to server.  Terminating client.");
	      quit();
	    }
	}
	
    
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  

  
  public void connectionException(Exception excep) {
	  clientUI.display("Server closed");
	  try {
		closeConnection();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

  }
  
  protected void connectionEstablished() {
	  try
	    {
	    	
	    	sendToServer("#login "+loginID);
	    }
	    catch(IOException e)
	    {
	    	clientUI.display
	        ("Could not send message to server.  Terminating client.");
	      quit();
	    }
  }
}
//End of ChatClient class
