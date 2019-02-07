package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import chatroominterface.ClientInterface;

public class ClientImplementation extends UnicastRemoteObject implements ClientInterface{
	private String name;
	private ClientUserInterface clientUserInterface;

	/** 
	 * @param name Client's name. */
	public ClientImplementation( String name) throws RemoteException{
		this.name=name;
	}

	@Override
	public void sendMessageToClient( String message) throws RemoteException{
		System.out.println( message);					//to client's console
		if (clientUserInterface!=null)
			clientUserInterface.writeMessage( message);	//to client's UI
	}

	@Override
	public String getName() throws RemoteException{
		return name;
	}
	
	public void setGUI( ClientUserInterface clientUserInterface){
		this.clientUserInterface=clientUserInterface;
	}

	@Override
	public void updateConnectedUsersList(Vector<ClientInterface> vector) throws RemoteException{
		if (clientUserInterface!=null)
			clientUserInterface.updateConnectedUsersList( vector);
	}
}