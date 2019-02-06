package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import chatroominterface.ClientInterface;

public class ClientImplementation extends UnicastRemoteObject implements ClientInterface{
	private String name;
	ClientUserInterface clientUserInterface;
	
	/** 
	 * @param name Client's name. */
	public ClientImplementation( String name) throws RemoteException{
		this.name=name;
	}

	@Override
	public void sendMessageToClient( String message) throws RemoteException{
		System.out.println( message);				//to client's console
	}

	@Override
	public String getName() throws RemoteException{
		return name;
	}
	
	//Do not add @Override here.
	public void setGUI( ClientUserInterface clientUserInterface){
		this.clientUserInterface=clientUserInterface;
	}
	
}