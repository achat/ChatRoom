package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import chatroominterface.ServerInterface;

public class Client{
	private static ServerInterface serverInterface;
	private ClientImplementation clientImplementation;
	private String name;
	
	public Client(){
		String ip;
		Scanner scanner=new Scanner(System.in);
		
		System.out.println("[System] Enter server's ip:");
		ip=scanner.nextLine();
		
		System.out.println("[System] Enter your name:");
		name=scanner.nextLine();
		
		try{
			clientImplementation=new ClientImplementation(name);
			serverInterface=( ServerInterface) Naming.lookup( "rmi://"+ip+"/Chatroom");
			serverInterface.login( clientImplementation);	//try to connect to chatroom.
		}catch( RemoteException | MalformedURLException | NotBoundException exception){
			exception.printStackTrace();
		}
		
	}
	
	public static void main( String...args){
		System.out.println("[System] Client is running.");

		Client client=new Client();
		client.startCommunication();		
	}
	
	private void startCommunication(){
		Scanner scanner=new Scanner(System.in);
		String message;

		System.out.println("[System] Type and send your messages. Press /terminate to end session.");
		while(true){
			message=scanner.nextLine();
			try{
				if(message.equals( "/terminate") ){
					serverInterface.logout( clientImplementation);
					System.exit(0);	//terminate program execution when loging out of the chatroom.
				}else
					serverInterface.publish("["+name+"] "+ message);
			}catch( RemoteException remoteException){
				remoteException.printStackTrace();
			}
		}
	}

}
