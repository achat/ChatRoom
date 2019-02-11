package server;

import java.net.InetAddress;
import java.rmi.Naming;
import java.util.Scanner;

/** This is the main Server class which registers the remote object serverImplementation under the name
 *  Chatroom in the local registry on the default RMI port 1099 on localhost.
 *  It also gives to the admin the possibility to enable the database for log registration
 *  and to publish admin messages in the chatroom. */
public class Server{ //-Djava.rmi.server.hostname=178.59.254.132
	private static final String SERVICE_NAME="Chatroom";
	private static final int PORT=1099;
	
	public static void main( String...args){
		try{
			ServerDatabase serverDatabase;
			String answer, adminMessage;
			String privateIP=InetAddress.getLocalHost().getHostAddress();

			//Create a Registry instance on the local host that accepts requests on the port 1099. 
			java.rmi.registry.LocateRegistry.createRegistry( PORT);
			
			System.out.println("[System] Would you like to enable the DataBase? (y/n)");
			Scanner scanner=new Scanner(System.in);
			answer=getAdminAnswer(scanner);

			if(answer.equalsIgnoreCase("n") ){
				System.out.println("[System] Skipping DataBase activation...");
				serverDatabase=null;
			 }else{
				System.out.println("[System] Activating DataBase...");
				//Create a database reference that will connect to the Oracle Database.
				serverDatabase=new ServerDatabase();
			 }	
			
			//The remote object to be bound, offering the Chatroom service.
			ServerImplementation serverImplementation=new ServerImplementation(serverDatabase);
			
			//Bind the object instance "new ServerImplementation()" to the name serviceName.
			//Please note that you cannot use rebind on an external IP.	
			Naming.rebind( "rmi://"+privateIP+"/"+SERVICE_NAME, serverImplementation );
			
			System.out.println( "[System] Chatroom is running on private/public ip: "
				+privateIP+"/"+serverImplementation.getServerPublicIP() );
										
			do{ //Send administrator messages to chatroom clients.
				adminMessage=scanner.nextLine();
				if(adminMessage.contains( "[Admin]") )
					serverImplementation.publish(adminMessage);
			}while(true);
		}catch( Exception exception){
			System.out.println( "[System] Chatroom failed to run: "+exception);
		}
	}
	
	/** Get the admin's choice on DataBase loading.
	 *  The asking process loops until the user provides a satisfying answer.
	 *  @param scanner The scanner used to read the admin's answer. */
	private static String getAdminAnswer(Scanner scanner){
		String answer;	
		boolean correctAnswer=false;
		
		do{ //Persist till correct answer given.
			answer=scanner.nextLine();
			if(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")  ) correctAnswer=true;
			else
				System.out.println("[System] Incorrect answer. Please answer (\"y\" for YES or \"n\" for NO)");
		}while( !correctAnswer );
		
		return answer;
	}
}