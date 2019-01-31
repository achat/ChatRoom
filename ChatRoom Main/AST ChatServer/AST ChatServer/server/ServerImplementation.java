package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import chatroominterface.ServerInterface;

public class ServerImplementation extends UnicastRemoteObject implements ServerInterface{
	public static final String MESSAGE="Hello World";

	public ServerImplementation() throws RemoteException{
		super( 0);    // required to avoid the 'rmic' step.
	}
	
	@Override
	public String getMessage(){
		return MESSAGE;
	}
}