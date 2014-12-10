import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Class to control the interaction with the game through the command line. 
 */
public class PlayGame 
{
	private final int begin = 0;
	String outPut = null;
	public static final int MODE_PLAYER = 0;
	public static final int MODE_BOT = 1;

	
	// The game object for this player.
	public DODGame game;
	
	
	
	/**
	 * Creates a new instance of PlayGame.
	 */
	public PlayGame()
	{
		//create an instance of the DODGame
		game = new DODGame();
	}
		
	
	/*
	 * Creates a new instance of PlayGame with the specified map.
	 * 
	 * @param map The name of the file to read the map from.
	 */
	public PlayGame(String map)
	{		
		//create an instance of the DODGame
		game = new DODGame(map);
	}
	
	
	
	
	/**
	 * Interpret the commands passed to the command line. The commands that are accepted by the client
	 * should be in the wire-spec.
	 * 
	 * @param command The command entered.
	 */
	public String playerCommand(String command)
	{	
		// Because continuously pressing the shift key while testing made my finger hurt...
		command = command.toUpperCase();
		//process the command string e.g. MOVE N 
		String tmp[] = command.split(" ", 2);
		String com = tmp[0];
		String arg = ((tmp.length == 2) ? tmp[1] : null);
		
		String message = "";
		
		//call the relevant method in the game object
		if (com.equals("HELLO")) 
		{
			if (tmp.length != 2) 
			{
				return null;
			}
			message = game.clientHello(sanitise(arg));
		} 
		else if (com.equals("LOOK")) 
		{
			message = game.clientLook();
		}
		else if (com.equals("PICKUP")) 
		{
			message = game.clientPickup();
		} 
		else if (com.equals("MOVE")) 
		{
			// We need to know which direction to move in.
			String dir = sanitise(arg, "[NESW]");
			if (dir.length() > 0) 
			{
				message = game.clientMove(dir.charAt(0));
			}
		} 
		else if (com.equals("ATTACK")) 
		{
			// They have to tell us what direction to attack in
			String dir = sanitise(arg, "[NESW]");
			if (dir.length() > 0) 
			{
				message = game.clientAttack(dir.charAt(0));
			} 
		} 
		else if (com.equals("SHOUT")) 
		{
			// Ensure they have given us something to shout.
			if (tmp.length != 2) 
			{
			}
			
			message = game.clientShout(sanitizeMessage(arg));
		}
		
		// Print the response from the game.
		return message;
	}
	
	
	
	/** HELPER METHODS **/
	
	
	/**
	 * Strip out anything that isn't alphanumeric
	 */
	private static String sanitise(String s)
	{
		return sanitise(s, "[a-zA-Z0-9-_:,]");
	}
	
	
	/**
	 * Strip out anything that isn't in the specified regex.
	 * 
	 * @param s		The string to be sanitised
	 * @param regex The regex to use for sanitisiation
	 * @return		The sanitised string
	 */
	private static String sanitise(String s, String regex)
	{
		String rv = "";
		
		for(int i = 0; i < s.length(); i++) 
		{
			String tmp = s.substring(i, i + 1);
			
			if (tmp.matches(regex)) 
			{
				rv += tmp;
			}
		}
		
		return rv;
	}
	
	
	/**
	 * Sanitises the given message - there are some characters that we can put in the messages that
	 * we don't want in other stuff that we sanitise.
	 * 
	 * @param s	The message to be sanitised
	 * @return 	The sanitised message
	 */
	private static String sanitizeMessage(String s)
	{
		return sanitise(s, "[a-zA-Z0-9-_ \\.,:!\\(\\)#]");
	}
	
	
}