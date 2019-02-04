package server;

import java.rmi.Naming;

public class Server{
	private static final String IP="192.168.1.30";
	private static final String serviceName="Chatroom";
	private static final int port=1099;
	
	public static void main( String...args){
		try{
			//Create a Registry instance on the local host that accepts requests on the port 1099. 
			java.rmi.registry.LocateRegistry.createRegistry( port);
			
			// Bind the object instance "new ServerImplementation()" to the name serviceName.
			Naming.rebind( "rmi://"+IP+"/"+serviceName, new ServerImplementation() );
			
			System.out.println( "[System] Chatroom is running.");
		}catch( Exception exception){
			System.out.println( "[System] Chatroom failed to run: "+exception);
		}
	}
}