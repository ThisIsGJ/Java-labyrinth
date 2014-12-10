import java.net.*;
import java.util.ArrayList;
import java.io.*;

 
/**
 * This class is the server class.Every Client should join game by 
 * connecte to the Server Class.
 * @author Guanjun
 *
 */
public class Server implements Runnable {
	
	private ServerSocket serverSocket = null;
    private PlayGame pg;
    private int count = 0;
    private godView gv;
    private int port;
    private ArrayList<multiServerThread> clientList;

    Server(int port,String mapName){
    	clientList = new ArrayList<multiServerThread>();
    	pg= new PlayGame(mapName);
    	this.port = port;
    }

    /**
     * This method is used to make server keep listening and waiting for a client to connect.
     */
    public void run()
	{
		try {
			System.out.println("the port number is:" + port);
            serverSocket = new ServerSocket(port);										//creat new serverSocket		
            serverSocket.setReuseAddress(true);
        } catch (IOException e) {
            System.err.println("Could not listen on this port.");
            System.exit(-1);
        }
		
		gv = new godView(pg);
		gv.theFrame();																	// show the godView interface while the server start listening
		
		while (true){
        	try{
        		
		        System.out.println("Listening for a client on port: "+
		                serverSocket.getLocalPort());
		        Socket clientSocket = serverSocket.accept();
		        System.out.println("A client has arrived.");
		    	// Let a separate Thread handle it.
		        multiServerThread s = new multiServerThread(clientSocket, pg, count,gv,clientList);
		        s.start();
		        clientList.add(s);
		        gv.upDate();
		        count++;
        	}
	      catch (IOException e) {
	        System.err.println("Accept failed.");
	      	}
    	}
	}
	
	//close the godView interface if the server stop listening
	public void closeGodView(){
		gv.setVisible(false);
	}
	
}




