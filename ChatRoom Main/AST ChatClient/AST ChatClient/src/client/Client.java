package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import chatroominterface.ServerInterface;

/** This is the main Client class which executes the client program. 
 *  Here the user is given the choice between a CMD and a GUI approach. */
public class Client{
	private static ServerInterface serverInterface;
	private ClientImplementation clientImplementation;
	private String name;
	
	/** The class constructor is used once when the user chooses a non-GUI approach.
	 * 	It gets the server's ip, the user name and searches for the remote item/service "Chatroom"
	 *  in the server's registry for the given ip.
	 *  Then the user gets logged in to the server. */
	public Client(){
		String ip;
		Scanner scanner=new Scanner(System.in);	//Do not close the scanner.
		
		System.out.println("[System] Enter your name:");
		name=scanner.nextLine();

		System.out.println("[System] Enter server's ip:");
		ip=scanner.nextLine();
		
		try{
			clientImplementation=new ClientImplementation(name);
			serverInterface=( ServerInterface) Naming.lookup( "rmi://"+ip+"/Chatroom");
			serverInterface.login( clientImplementation);	//try to connect to chatroom.
		}catch( RemoteException | MalformedURLException | NotBoundException exception){
			exception.printStackTrace();
		}
	}
	
	public static void main( String...args){
		String answer;
		//TODO Transfer some Client code to ClientImplementation class.
		System.out.println("[System] Client is running.");
		System.out.println("[System] Would you like to load G.U.I.? (y/n)");
		answer=getUserAnswer();

		if(answer.equalsIgnoreCase("n") ){
		System.out.println("[System] Continuing in C.M.D...");
			Client client=new Client();
			client.startCommunication();
		 }else{
			System.out.println("[System] Loading G.U.I...");
			new ClientUserInterface();
		 }		
		
	}
	
	/** Get the user's choice on GUI loading.
	 *  The asking process loops until the user provides a satisfying answer. */
	private static String getUserAnswer(){
		String answer;
		Scanner scanner=new Scanner(System.in);			//Do not close the scanner.
		boolean correctAnswer=false;
		
		do{
			answer=scanner.nextLine();
			if(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")  ) correctAnswer=true;
			else
				System.out.println("[System] Incorrect answer. Please answer (\"y\" for YES or \"n\" for NO)");
		}while( !correctAnswer );
		return answer;
	}
	
	/** This method is used when GUI is not loaded.
	 * It is used to read the user messages and to publish them.
	 * It also terminates the program execution when the user messages equals "/terminate" */
	private void startCommunication(){
		Scanner scanner=new Scanner(System.in);
		String message;

		System.out.println("[System] Type and send your messages. Press /terminate to end session.");
		while(true){
			message=scanner.nextLine();
			try{
				if(message.equals( "/terminate") ){
					serverInterface.logout( clientImplementation);
					System.exit(0);	//terminate program execution when logging out of the Chatroom.
				}else
					serverInterface.publish("["+name+"] "+ message);
			}catch( RemoteException remoteException){
				remoteException.printStackTrace();
			}
		}
	}

}