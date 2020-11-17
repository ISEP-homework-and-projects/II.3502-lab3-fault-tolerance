package FT;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Billboard extends Remote
{
    String getMessage() throws RemoteException;
    void setMessage(String message) throws  RemoteException;
    void updateMessageFromServer(String message) throws RemoteException;
}
