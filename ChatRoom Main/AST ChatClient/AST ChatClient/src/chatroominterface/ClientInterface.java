package chatroominterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface ClientInterface extends Remote{
	public void sendMessageToClient( String message) throws RemoteException; 	//Send message to Client.
	public String getName() throws RemoteException;								//Get Client's name.
	//Update the connected users jList.
	public void updateConnectedUsersList(Vector<ClientInterface> vector) throws RemoteException;
}
