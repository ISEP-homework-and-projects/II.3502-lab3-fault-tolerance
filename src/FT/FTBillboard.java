package FT;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface FTBillboard extends Remote,Billboard
{
    String getLeader() throws RemoteException;

    List<String> getNeighbors() throws RemoteException;

    void propagateNeighbors(List<String> neighbors) throws RemoteException;

    void registerReplica(String server, FTBillboard replica) throws RemoteException;

}
