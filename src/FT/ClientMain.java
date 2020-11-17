package FT;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

public class ClientMain
{
    private static List<String> servers;
    private static int nextServer;

    public static void main(String[] args)
    {
        System.out.println("Launching client");
        servers = new ArrayList<>();
        nextServer = 0;

        try
        {
            String server = args[0];

            FTBillboard billboard = (FTBillboard) Naming.lookup("rmi://"+server+"/FTBillboardServer");

            servers = billboard.getNeighbors();

            System.out.println("| Message from server: "+billboard.getMessage());

            System.out.println("| Found "+servers.size()+" other replicas.");

            System.out.println("-- Enter a message to update the billboard --");

            execute(billboard);

        }
        catch (Exception e)
        {
            System.out.println("An error occured while trying to connect: "+e.getMessage());
        }
    }

    private static FTBillboard selectNextServer()
    {
        FTBillboard billboard = null;

        if(servers.size() > nextServer)
        {
            try
            {
                billboard = (FTBillboard) Naming.lookup("rmi://"+servers.get(nextServer)+"/FTBillboardServer");
                nextServer+=1;
            }
            catch (Exception e)
            {
                System.out.println("An error occured while trying to connect: "+e.getMessage());
            }
        }
        else
        {
            System.out.println("No other replicas available");
        }
        return billboard;
    }

    private static void execute(FTBillboard billboard)
    {
        Boolean shouldExit = false;

        if(billboard == null)
        {
            System.out.println("No new server to connect to, exiting...");
            shouldExit = true;
            System.exit(0);
            return;

        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String message = "";

        while (!shouldExit)
        {
            try
            {
                System.out.print(">");
                message = br.readLine().trim();

                if(message.equals("exit"))
                {
                    shouldExit = true;
                    System.exit(0);
                    break;
                }
                else
                {
                    billboard.setMessage(message);
                }

            }
            catch (Exception firstSendError)
            {
                System.out.println("Server lost, trying one more time to reach the server...");
                try
                {
                    billboard.setMessage(message);
                }
                catch (Exception secondSendError)
                {
                    System.out.println("Server lost for the 2nd time, changing server");
                    execute(selectNextServer());
                }
            }
        }
    }
}

