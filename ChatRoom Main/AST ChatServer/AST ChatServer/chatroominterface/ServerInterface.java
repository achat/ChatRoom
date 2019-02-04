package chatroominterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface ServerInterface extends Remote {
    @Deprecated
    public boolean login( ClientInterface clientInterface) throws RemoteException;	//Use to login to the chatroom.
	public boolean logout( ClientInterface clientInterface) throws RemoteException; //Use to logout from the chatroom.
	public void publish( String message) throws RemoteException; 		//Send messages to clients and to server.
	public Vector<?> getConnectedClients() throws RemoteException;	 	//Get all clients connected to the chatroom.
}