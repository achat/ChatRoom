package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import chatroominterface.ClientInterface;
import chatroominterface.ServerInterface;

/** This class manages the implementation of the server as a rmi object that will
 *  be bound to the server's registry. It handles operations suck as login, logout,
 *  Database updates, GUI update signals,  */
public class ServerImplementation extends UnicastRemoteObject implements ServerInterface{
	private Vector<ClientInterface> vector=new Vector<ClientInterface>();	//A vector that holds connected clients.
	private ServerDatabase serverDatabase;
	
	public ServerImplementation(ServerDatabase serverDatabase) throws RemoteException{ //Do not remove this.
		this.serverDatabase=serverDatabase;
	} 	
	
	@Override
	/** The Client uses this method to connect to the server.
	 * @param clientInterface The client who tries to login. */
	public boolean login( ClientInterface clientInterface) throws RemoteException{
	
		//Inform client for successful connection.
		clientInterface.sendMessageToClient( "[System] You have connected successfully to the chatroom.");
		
		//Inform all clients that a new client has connected.
		publish("[System] "+ clientInterface.getName()+" has just connected."); 
		
		if (serverDatabase!=null)							//update only if database enabled.
			updateLogsTable("connection",clientInterface);
		
		vector.add( clientInterface); 						//add client to client list.
		updateClientsUI();									//Update the client jList on connected users.
		return true;										//true for successful connection.
	}

	@Override
	/** The Client uses this method to connect to the server.
	 *  @param clientInterface The client who tries to logout. */
	public boolean logout( ClientInterface clientInterface) throws RemoteException{
		
		//Inform client for successful disconnection.
		clientInterface.sendMessageToClient( "[System] You have disconnected successfully from the chatroom.");
		
		if (serverDatabase!=null)							//update only if database enabled.
			updateLogsTable("connection",clientInterface);
		
		vector.remove( clientInterface); 					//Remove client from client list before publishing.
		
		//Inform all clients that the client has disconnected.
		publish("[System] "+ clientInterface.getName()+" has just disconnected."); 
			
		updateClientsUI();									//Update the client jList on connected users.
		return true;										//true for successful disconnection.
	}

	@Override
	/** Publish the message to all users in the chatroom and to the server's black screen.
	 * @param message The message to send. */
	public void publish( String message) throws RemoteException{
		if (!message.contains( "[Admin]") )
			System.out.println( message);				//This will write the message to the server's black screen.
		
		for( int i=0; i<vector.size(); i++){	//Send to all users
			try{
				ClientInterface clientInterface=vector.get( i);
				clientInterface.sendMessageToClient( message);
			}catch( Exception exception){
				exception.printStackTrace();
			}
		}
	}

	@Override
	/** Get all clients connected to the chatroom.*/
	public Vector<ClientInterface> getConnectedClients() throws RemoteException{
		return vector;
	}
	
	@Override
	/** Get the server's public IP. */
	public String getServerPublicIP() throws RemoteException{
		String publicIP=null;
		
		try{
			URL url=new URL( "http://checkip.amazonaws.com");
			BufferedReader bufferedReader=new BufferedReader( new InputStreamReader( url.openStream() ) );
			publicIP=bufferedReader.readLine(); 	//Get the public IP as a String
		}catch( IOException exception){
			exception.printStackTrace();
		}

		return publicIP;
	}
	
	/** The method to update the Logs table in the database.
	 * @param action The action performed by the user, either connection or disconnection.
	 * @param clientInterface The user who performed this action. */
	private void updateLogsTable(String action, ClientInterface clientInterface){
		try{	//Update chatroom_logs table in database.
			serverDatabase.updateLogsTable( "connection", getClientHost(), clientInterface.getName() );
		}catch( ServerNotActiveException | RemoteException exception){
			exception.printStackTrace();
		}
	}
	
	/** This method should be called when a user logs in/out to update the other users jList on their GUI. */
	private void updateClientsUI(){
		for( int i=0; i<vector.size(); i++){			//Update connected users list according to logins/logouts
			try{
				ClientInterface clientInterface=vector.get( i);
				clientInterface.updateConnectedUsersList(vector);
			}catch( Exception exception){
				exception.printStackTrace();
			}
		}
	}
	
}