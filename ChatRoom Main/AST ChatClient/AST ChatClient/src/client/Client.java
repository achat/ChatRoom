package client;

import java.rmi.Naming;

import chatroominterface.ServerInterface;

public class Client{
	public static void main( String...args) throws Exception{
		ServerInterface serverInteface=( ServerInterface) Naming.lookup( "//localhost/Chatroom");
		
		ClientImplementation clientImplementation=new ClientImplementation( "Tester");
		System.out.println(clientImplementation.getName()+" connected to server.");
		
		clientImplementation.sendMessageToClient( "Received from Server: "+ serverInteface.getMessage() );
	}
}