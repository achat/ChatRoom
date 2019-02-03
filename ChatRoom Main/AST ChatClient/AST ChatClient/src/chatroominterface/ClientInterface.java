package chatroominterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
/** Exposing Client's methods to server.*/
public interface ClientInterface extends Remote{
	public void sendMessageToClient( String message) throws RemoteException; 	//Send message to Client.
	public String getName() throws RemoteException;								//Get Client's name.
}