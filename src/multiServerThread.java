import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class multiServerThread extends Thread {
    private Socket socket = null;
    private PlayGame pg; 
    private String sendToUser;
    private String fromUser;
    private int count;
    private godView gv;
    private PrintWriter out;
    private BufferedReader in;
    private int people_post;
    private String toUser = "";
    private ArrayList<multiServerThread> client;
    
    public multiServerThread(Socket socket, PlayGame pg, int count,godView gv,ArrayList client) {
    	super("multiServerThread");
    	this.socket = socket;
    	this.pg = pg;
    	this.gv = gv;
    	this.count = count;
    	this.client = client;
    }
 
    public synchronized void run() {
 
	    try {	    	
	    	pg.game.startNewGame();
	    	
	    	//send information to user
	        out = new PrintWriter(socket.getOutputStream(), true);
	        //from user
	        in = new BufferedReader(
	                    new InputStreamReader(
	                    socket.getInputStream()));
	        while ( (fromUser = in.readLine()) != null) {
	        	toUser = "";
	        	// if one player have won the game, send lose to other player and game over.
	        	if(fromUser.equals("win")){
	        		gv.gameOver();
	        		for(int i = 0; i < client.size(); i++){
	        			client.get(i).out.println("lose");
	        		}
	        	}else{
		        		people_post = Integer.parseInt(fromUser);
		        		fromUser = in.readLine();
		        		//uodate map per 2s for every player.
		        		if(fromUser.equals("updateMap")){
		        			pg.game.setPlayer(count);
			        		pg.game.player.setPeople(people_post);
		        			sendToUser = pg.playerCommand("look");
		        			sendToClient();
		        		}else{
			        		pg.game.setPlayer(count);
			        		pg.game.player.setPeople(people_post);
					        sendToUser = pg.playerCommand(fromUser);
					        pg.game.setPlayer(0); 
				        	int i = 1;
				        	//each player only could move 6 steps in one turn.
				        	while(pg.game.isTurnFinished()){
					        	if (i < pg.game.play.size())
					       		{ 	
					        		pg.game.setPlayer(i); 
					        		i++;
					        	}
					        	else{
					        		for (int setAP = 0; setAP < pg.game.play.size(); setAP++){
					        			pg.game.setPlayer(setAP);
					        			pg.game.player.setAp(6 - pg.game.player.getLantern() - pg.game.player.getSword() -pg.game.player.getArmour()); 
					        		}
					        	break;
					        	}
				        	} 
				        	gv.upDate();
				        	sendToClient();
		       		}
		       
		        	
	        		}
	        	}
	    }catch (IOException e) {
	        e.printStackTrace();
	    }
    }
    
    //send messafe to client 
    private void sendToClient(){
     	toUser += (pg.game.player.getSword() + "\n");
     	toUser += (pg.game.player.getArmour() + "\n");
     	toUser += (pg.game.player.getHp() + "\n");
     	toUser += (pg.game.player.getAp() + "\n");
     	toUser += (pg.game.dodMap.getGoal() + "\n");
     	toUser += (pg.game.player.getLantern() + "\n");
     	toUser += (pg.game.player.getGold() + "\n");
     	toUser += sendToUser; 
     	out.println(toUser);
    }
}
