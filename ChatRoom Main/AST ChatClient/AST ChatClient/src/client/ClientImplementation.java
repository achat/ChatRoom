package client;
//my first commit

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import chatroominterface.ClientInterface;

public class ClientImplementation extends UnicastRemoteObject implements ClientInterface{
	private String name;

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

}
