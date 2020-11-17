package FT;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class FTBillboardServer extends UnicastRemoteObject implements  FTBillboard
{
    List<String> neighbors;
    private static String message;

    protected FTBillboardServer() throws RemoteException
    {
        super();
        neighbors = new ArrayList<>();
    }

    /**
     *
     * @param args 0:adress:port 1(Optional)port of the server to register to
     */


    public static void main(String args[])
    {
        message = "Welcome to billboardServer";

        try
        {
            String endpoint = args[0];

            int port = Integer.valueOf(endpoint.split(":")[1]);

            FTBillboardServer server = new FTBillboardServer();
            java.rmi.registry.LocateRegistry.createRegistry(port);

            Naming.rebind("rmi://"+endpoint+"/FTBillboardServer",server);

            System.out.println("FTBillboardServer bound in registry at "+endpoint);

            if(args.length > 1)
            {
                String mainServerEndpoint = args[1];
                System.out.println("Main server to register to found at "+mainServerEndpoint);
                FTBillboard currentLeader = (FTBillboard) Naming.lookup("rmi://"+mainServerEndpoint+"/FTBillboardServer");
                currentLeader.registerReplica(endpoint,server);
            }

        }
        catch (Exception e)
        {
            System.out.println("An error occured while receiving connection: "+e.getMessage() );
        }
    }


    @Override
    public String getLeader() throws RemoteException {
        return null;
    }

    @Override
    public List<String> getNeighbors() throws RemoteException {
        return neighbors;
    }

    @Override
    public void registerReplica(String server, FTBillboard replica) throws RemoteException
    {
        System.out.println("New replica was registered from "+server);
        neighbors.add(server);
    }

    @Override
    public String getMessage() throws RemoteException
    {
        return message;
    }

    @Override
    public void setMessage(String newMessage) throws RemoteException
    {
        System.out.println("Received message: "+newMessage);
        message = newMessage;
        broadCastMessage();
    }

    @Override
    public void updateMessageFromServer(String newMessage) throws RemoteException
    {
        message = newMessage;
        System.out.println("The new message is now: "+newMessage);
    }

    private void broadCastMessage()
    {
        try
        {
            for (String replicaEndpoint : neighbors)
            {
                FTBillboard replica = (FTBillboard) Naming.lookup("rmi://"+replicaEndpoint+"/FTBillboardServer");
                replica.updateMessageFromServer(message);
            }
        }
        catch (Exception e)
        {
            System.out.println("Error while broadcasting new message to replicas");
        }
    }
}
