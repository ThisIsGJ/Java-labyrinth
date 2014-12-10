import java.util.ArrayList;
import java.util.Random;
// Imports all of the static variables from DODMap (i.e. The constants for map objects)


/**
 * This class controls the game logic and other such magic.
 */
public class DODGame
{		
	public Player player;
	
    public DODMap dodMap;
    
    public ArrayList<Player> play;// to store the player in the play list
   
    
    
    /**
     * Default Constructor.
     * 
     * This happens if there isn't a map to load.
     */
    public DODGame()
    {
    	dodMap = new DODMap();
    	play = new ArrayList<Player>();
    }
    
    
    public String processInput(String str){
    	return null;
    }
    
    /**
     * Constructor that specifies the map which the game should be played on.
     * 
     * @param map The name of the file to load the map from.
     */
    public DODGame(String map)
    {
    	dodMap = new DODMap(map);
    	play = new ArrayList<Player>();
    }
    
    
    /**
     * Starts a new game of the Dungeon of Dooooooooooooom.
     */
    public void startNewGame()
    {
    	// Create, register & randomly position a new player
    	player = new Player(3, 6, 0, 0, 0, 0, false);
    	setRandomStartLocation();
    	play.add(player);
    	startTurn();
    }
    
    
    /**
     * This doesn't really do anything as yet, other than reset the player AP. 
     * It should do more when the game is multiplayer
     */
    private void startTurn()
    {	
    	printMessageFromGame("STARTTURN");
    	player.setAp(6 - (player.getLantern() + player.getSword() + player.getArmour()));
    }
    
    public void setPlayer(int i){
    	player = play.get(i);
    }
    
    /**
     * This is pretty redundant at this point, but it might be useful when the client &
     * server are separated over, say, a network.
     * 
     * @param message The message to be printed
     */
    private String printMessageFromGame(String message)
    {
    	return message;
    }
    
    
    /** 
     * Once a player has performed an action the game needs to move onto the next turn 
     * to do this the game needs to check for a win and then test to see if the current
     * player has more AP left.
     * 
     * Note that in this implementation we currently playing this as a single player game so the 
     * next turn will always be the current player so we simply start their turn again. 
     */
    
    
    /**
     * Check if the turn as finished (i.e. if the player has any AP left)
     * 
     * @return true if the player has AP left, false otherwise.
     */
    public boolean isTurnFinished()
    {
    	return (player.getAp() == 0);
    }
    
    
    /**
     * Puts the player in a randomised start location.
     */
    private void setRandomStartLocation()
    {
    	boolean wall = true;
    	while(wall) {
	    	// Generate a random location
	    	Random random = new Random();
	    	int randomY = random.nextInt(dodMap.getMapHeight());
	    	int randomX = random.nextInt(dodMap.getMapWidth());
	    	
	    	if(dodMap.getMap()[randomY][randomX] != DODMap.WALL)
	    	{
	    		// If it's not a wall then we can put them there
	    		player.setLocation(randomX, randomY);
	    		wall = false;
	    	}
    	}	    	
    }
    
    
    
    /***GAME COMMANDS AND LOGIC***/
    

    /**
     *  Handles the client message HELLO
     *  
     *  @param newName 	The name of the player to say hello to
     *  @return 		The message to be passed back to the command line
     */
    public String clientHello(String newName) 
    {
    	// Change the player name and then say hello to them
		player.setName(newName);
		return "HELLO " + newName + "\nGOAL " + dodMap.getGoal();
    }
    
    
    /**
     * Handles the client message LOOK
     * Shows the portion of the map that the player can currently see.
     * 
     * @return The part of the map that the player can currently see.
     */
    public String clientLook() 
    {
    	String message = "";
    	
    	// Work out how far the player can see
	    int distance = 2 + player.getLantern();
	    System.out.println("lanern:" + player.getLantern());
	    // Iterate through the rows.
	    for (int i = -distance; i <= distance; ++i) 
	    {
	    	String line = "";
			
	    	// Iterate through the columns.
			for (int j = -distance; j <= distance; ++j) 
			{

				char content = '?';
			    
				// Work out which location is next.
			    int targetX = player.getX() + j;
			    int targetY = player.getY() + i;
			    
			    // Work out what is in the square field of vision.
			    if (Math.abs(i) + Math.abs(j) > distance + 1) 
			    {
					// It's outside the FoV so we don't know what it is.
					content = 'X';
			    } 
			    else if ((targetX < 0) || (targetX >= dodMap.getMapWidth()) ||
				         (targetY < 0) || (targetY >= dodMap.getMapHeight())) 
			    {	
			    	// It's outside the map, so just call it a wall.
			    	content = '#';
			    }
			    /*
			    else if(targetX == player.getX() && targetY == player.getY()){
		    		content = 'P';
		    	}*/
			    else 
			    {
					// Look up and see what's on the map
					switch (dodMap.getMap()[targetY][targetX]) 
					{
						case DODMap.EMPTY: 		content = '.'; break;
						case DODMap.HEALTH: 	content = 'H'; break;
						case DODMap.LANTERN: 	content = 'L'; break;
						case DODMap.SWORD: 		content = 'S'; break;
						case DODMap.ARMOUR: 	content = 'A'; break;
						case DODMap.EXIT: 		content = 'E'; break;
						case DODMap.WALL: 		content = '#'; break;
						case DODMap.GOLD:		content = 'G'; break;
						default : 
					    	// This shouldn't happen
					    	System.exit(1);
					}
							
			    }
			    
			    for(int n = 0; n < play.size(); n++)
			    {
			    	if(play.get(n).getX() == targetX && play.get(n).getY() == targetY)
			    	{
				    		int state = play.get(n).getPeople();
					    	String people = Integer.toString(state);
					    	char map [][] = dodMap.getMap();
					    	if(map[targetY][targetX] == 'E'){
					    		String peopleInExit = Integer.toString(state + 4);
					    		content = (peopleInExit).charAt(0);								//if this place is exit
					    	}else{
					    		content = people.charAt(0);   									//for people different posture while they moving
					    	}
					    	
			    	}
			    }
				    // Add to the line
				    line += content;
				}
			    
			   
			// Send a line of the look message
			message += line;
	    }
	    
	    return message;
    }
    
    
    /**
     * Returns the current message to the client. Note that this becomes important when using
     * multiple clients across a network.
     * 
     * @param message 	The message to be shouted
     * @return			The message
     */
    public String clientShout(String message) 
    {
		return "MESSAGE: \n\t" + message;
    }
    
    
    /**
     * Handles the client message PICKUP.
     * Generally it decrements AP, and gives the player the item that they picked up
     * Also removes the item from the map
     * 
     * @return A message indicating the success or failure of the action of picking up.
     */
    public String clientPickup() 
    {
		
		// Can only pick up in you have action points left
		if (player.getAp() > 0) 
		{
		    // Check that there is something to pick up
		    switch (dodMap.getMap()[player.getY()][player.getX()]) 
		    {
			    case DODMap.EXIT : // Can't pick up the exit
			    case DODMap.EMPTY : // Nothing to pick up
			    	break;
			    	
			    case DODMap.HEALTH :
					// Costs the rest of your actions
					player.setAp(0);
					
					// Remove from the map
					dodMap.setMapCell(player.getY(), player.getX(), DODMap.EMPTY);
					
					// Add one to health...
					player.incrementHealth();
					
					// ... notify the client ...
					
			    case DODMap.LANTERN :
			    	
					// Can pick up if we don't have one
					if (player.getLantern() == 0) 
					{
					    // Costs one action point
					    player.decrementAp();
					    
					    // Remove from the map
					    dodMap.setMapCell(player.getY(), player.getX(), DODMap.EMPTY);
					    
					    // ... give them a lantern ...
					    player.setLantern(1);
					} 
					
					break;
					
			    case DODMap.SWORD :
			    	
					// Does almost exactly the same thing as picking up a lantern		
					if (player.getSword() == 0) 
					{
					    player.decrementAp();
					    
					    dodMap.setMapCell(player.getY(), player.getX(), DODMap.EMPTY);
					    
					    player.setSword(1);
					} 
					break;
					
			    case DODMap.ARMOUR :
			    	
					// Similar again
					if (player.getArmour() == 0) 
					{
					    player.decrementAp();
					    
					    dodMap.setMapCell(player.getY(), player.getX(), DODMap.EMPTY);
					    
					    player.setArmour(1);
					} 
					break;
					
			    case DODMap.GOLD:
			    	
				    // Costs one action point
				    player.decrementAp();
				    
				    // Remove from the map
				    dodMap.setMapCell(player.getY(), player.getX(), DODMap.EMPTY);
				    
				    // Add to the amount of treasure
				    player.addGold(1); 
				    
				  
		    }
		} 
		
		// Fail unless the process explicitly succeeded
		return clientLook();
    }
    
    
    /**
     * Handles the client message MOVE
     * 
     * Move the player in the specified direction - assuming there isn't a wall in the way
     * 
     * @param direction The direction (NESW) to move the player
     * @return			An indicator of the success or failure of the movement.
     */
    public String clientMove(char direction) 
    {
		// Can only move in the player's turn and if they have action points remaining
		if (player.getAp() > 0) 
		{
		    // Work out where the move would take the player
		    int targetX = player.getX();
		    int targetY = player.getY();
		    
		    switch (direction) 
		    {	
			    case 'N' : --targetY; break;
			    case 'S' : ++targetY; break;
			    case 'E' : ++targetX; break;
			    case 'W' : --targetX; break;
			    
		    }
		    
		    // Ensure that the movement is within the bounds of the map
		    if ((targetX >= 0) && (targetX < dodMap.getMapWidth()) &&
		    	(targetY >= 0) && (targetY < dodMap.getMapHeight())) 
		    {
			
		    	// The move must not be into a wall
				if (dodMap.getMap()[targetY][targetX] != DODMap.WALL) 
				{
					// Costs one action point
					player.decrementAp();
					
					// Move the player
					player.setX(targetX);
					player.setY(targetY);
				
					// Notify the client of the success
				} 
		    }
		
		// Fail unless there is an explicit reason why we succeed
		}
		return clientLook();
    }
    
    
    /**
     * Handles the client message ATTACK
     * 
     * Note: In the single player version of the game this doesn't do anything
     * 
     * @param direction	The direction in which to attack
     * @return			A message indicating the success or failure of the attack
     */
    public String clientAttack(char direction) 
    {
    	String failMessage = "FAIL";
	
    	// Can only attack if it is this player's turn and they have action points left
		if (player.getAp() > 0) 
		{
		    // Work out which square we're targeting
		    int targetX = player.getX();
		    int targetY = player.getY();
		    
		    switch (direction) 
		    {
			    case 'N' : --targetY; break;
			    case 'S' : ++targetY; break;
			    case 'E' : ++targetX; break;
			    case 'W' : --targetX; break;
			    
			    default :  // Shouldn't happen
					System.err.println("Internal error in connection base.");
					System.err.println("'" + direction + "' is not a direction.");
					System.exit(1);
		    }
		    
		    /** 
		     * TODO .... This code does not need to be filled in until Coursework 3!
		     * 
		     * 1. Work out which player the attack is on...
		     * 
		     * 2. Have you hit the target? - hint, you might want to make the chance of a successful attack 75%?
		     * 
		     * 2.1 if the player has hit the target then hp of the target should be reduced based on this formula...
		     * 
		     * damage = 1 + player.sword - target.armour 
		     * 
		     * i.e. the damage inflicted is 1 + 1 if the player has a sword and - 1 if the target has armour.
		     * 
		     * Player and target are informed about the attack as set out in the wire_spec
		     * 
		     * 2.2 if the player has missed the target then nothing happens.  Optionally, the player and target can be informed
		     * about the failed attack 
		     *  
		     */

			return "FAIL attacking has not been implemented";
		    
		} 
		else 
		{
		    failMessage += "No action points left";
		}
		
		// Fail unless there is an explicit reason why we succeed
		return failMessage;
    }
    
    /**
     * Handles the client message ENDTURN
     * 
     * Just sets the AP to zero currently as it's only a single player game.
     * 
     * @return A message indicating the status of ending a turn (currently always successful).
     */
    public void clientEndTurn() 
    {
		player.setAp(0);
    }
    
    
    /**
     * Passes the goal back (for the bot)
     * 
     * @return The current goal
     */
    public int getGoal()
    {
    	return dodMap.getGoal();
    }
}