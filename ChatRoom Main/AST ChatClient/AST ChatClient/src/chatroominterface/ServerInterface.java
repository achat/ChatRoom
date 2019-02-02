package chatroominterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

//first commit
public interface ServerInterface extends Remote {
    public String getMessage() throws RemoteException;
}
