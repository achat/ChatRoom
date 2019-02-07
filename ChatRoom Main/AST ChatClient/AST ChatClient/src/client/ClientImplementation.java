package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import chatroominterface.ClientInterface;

/** This class implements the methods of the client's interface.
 * 	It updates the user's console with his own messages and updates the list of connected users in the GUIs.*/
public class ClientImplementation extends UnicastRemoteObject implements ClientInterface{
	private String name;
	private ClientUserInterface clientUserInterface;

	/** The class constructor.
	 * @param name Client's name. */
	public ClientImplementation( String name) throws RemoteException{
		this.name=name;
	}

	@Override
	/** The method is used to write messages to the client CMD or GUI.
	 * @param message The message to be written. */
	public void sendMessageToClient( String message) throws RemoteException{
		System.out.println( message);					//to client's console
		if (clientUserInterface!=null)
			clientUserInterface.writeMessage( message);	//to client's UI
	}

	@Override
	/** The method return the client's name. */
	public String getName() throws RemoteException{
		return name;
	}
	

	/** This method is used to set the client's GUI.
	 * @param clientUserInterface The GUI of the client. */
	public void setGUI( ClientUserInterface clientUserInterface){
		this.clientUserInterface=clientUserInterface;
	}

	@Override
	/** This method is used to update the client's jList in the GUI.
	 * @param vector The data structure that holds the whole of the connected users. */
	public void updateConnectedUsersList(Vector<ClientInterface> vector) throws RemoteException{
		if (clientUserInterface!=null)
			clientUserInterface.updateConnectedUsersList( vector);
	}
}