package server;

// First commit.
// Second test.

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server{
	private static final long serialVersionUID=1L;

	public static void main( String... args)  throws Exception{
		System.out.println( "RMI server started.");

		try{ //special exception handler for registry creation.
			LocateRegistry.createRegistry( 1099);
			System.out.println( "Java RMI registry created.");
		}catch( RemoteException e){ //do nothing, error means registry already exists.			
			System.out.println( "Java RMI registry already exists.");
		}

		// Bind this object instance to the name "Chatroom".
		Naming.rebind( "//localhost/Chatroom", new ServerImplementation() );
		System.out.println( "PeerServer bound in registry.");
	}
}