package chatroominterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface ServerInterface extends Remote{
    public boolean login( ClientInterface clientInterface) throws RemoteException;	//Use to login to the chatroom.
	public boolean logout( ClientInterface clientInterface) throws RemoteException; //Use to logout from chatroom.
	public void publish( String message) throws RemoteException; 		//Send messages to clients and to server.
	public Vector<?> getConnectedClients() throws RemoteException;	 	//Get all clients connected to the chatroom.
	public String getServerPublicIP() throws RemoteException;	 		//Get the server's public ip.
}