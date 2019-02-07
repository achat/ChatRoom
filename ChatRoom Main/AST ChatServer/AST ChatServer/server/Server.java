package server;

import java.net.InetAddress;
import java.rmi.Naming;
import java.util.Scanner;

public class Server{
	private static final String serviceName="Chatroom";
	private static final int port=1099;
	
	public static void main( String...args){
		try{
			String privateIP=InetAddress.getLocalHost().getHostAddress();

			//Create a Registry instance on the local host that accepts requests on the port 1099. 
			java.rmi.registry.LocateRegistry.createRegistry( port);
			
			//Create a database reference that will connect to the Oracle Database.
			ServerDatabase serverDatabase=new ServerDatabase();
			
			//The remote object to be bound, offering the Chatroom service.
			ServerImplementation serverImplementation=new ServerImplementation(serverDatabase);
			
			//Bind the object instance "new ServerImplementation()" to the name serviceName.
			//Please note that you cannot use rebind on an external IP.	
			Naming.rebind( "rmi://"+IP+"/"+serviceName, serverImplementation );
			
			System.out.println( "[System] Chatroom is running on private/public ip: "
					+privateIP+"/"+serverImplementation.getServerPublicIP() );
				
			Scanner scanner=new Scanner(System.in);
			String adminMessage;
			do{ //Send administrator messages to chatroom clients.
				adminMessage=scanner.nextLine();
				if(adminMessage.contains( "[Admin]") )
					serverImplementation.publish(adminMessage);
			}while(true);
		}catch( Exception exception){
			System.out.println( "[System] Chatroom failed to run: "+exception);
		}
	}
}