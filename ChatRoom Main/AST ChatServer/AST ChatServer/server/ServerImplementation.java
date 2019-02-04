package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import chatroominterface.ClientInterface;
import chatroominterface.ServerInterface;

public class ServerImplementation extends UnicastRemoteObject implements ServerInterface{
	private Vector<ClientInterface> vector=new Vector<ClientInterface>();	//A vector that holds connected clients.
	public ServerImplementation() throws RemoteException{} 	//Do not remove this.
	
	@Override
	/** The Client uses this method to connect to the server. */
	public boolean login( ClientInterface clientInterface) throws RemoteException{
		//System.out.println("[System] "+ clientInterface.getName()+" got connected...");
		
		//Inform client for successful connection.
		clientInterface.sendMessageToClient( "[System] You have connected successfully to the chatroom.");
		//Inform all clients that a new client has connected.
		publish("[System] "+ clientInterface.getName()+" has just connected."); 
		
		vector.add( clientInterface); 						//add client to client list.
		return true;										//true for successful connection.
	}

	@Override
	/** The Client uses this method to connect to the server. */
	public boolean logout( ClientInterface clientInterface) throws RemoteException{
		
		//Inform client for successful disconnection.
		clientInterface.sendMessageToClient( "[System] You have disconnected successfully from the chatroom.");
		
		vector.remove( clientInterface); 					//Remove client from client list before publishing.
		
		//Inform all clients that the client has disconnected.
		publish("[System] "+ clientInterface.getName()+" has just disconnected."); 
		
		return true;										//true for successful disconnection.
	}
	
	//need 2 more methods here
	
	

	
}
