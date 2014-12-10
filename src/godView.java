import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * This class is used for creating godView interface.The user could 
 * see what happens in the dungeon room from the godView interface.
 * @author Guanjun
 *
 */
public class godView extends JFrame{
	
	private JPanel map;
	private PlayGame pg;
	private char[][] theMap;
	private int cols,rows;

	godView(PlayGame pg){
		this.pg = pg;
	}
	
	/**
	 * make frame for the god view.
	 */
	public void theFrame(){
		setTitle("Server");
		
		map();
		
		Container con = getContentPane();
		con.add(map);
		setSize(cols*120,rows*80);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * To create the main panel for the godView While the Server start listening.
	 */
	public void map(){
		map = new JPanel();
		map.setBorder(new TitledBorder("God View"));
		theMap = pg.game.dodMap.getMap();
		rows = theMap.length;
		cols = theMap[0].length;
		map.setLayout(new GridLayout(rows, cols));
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols ; j++){
				char mark = theMap[i][j];
				paint(mark);												//add the iamge to the godView panel
				}
		}
	}
	
	/**
	 * To update the godView while player join the game.
	 */
	public synchronized void upDate(){
		map.removeAll();
		map.setLayout(new GridLayout(rows, cols));
		
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols ; j++){
			char mark = theMap[i][j];
			
			// check players, show all the player on the gadView.
			for(int n = 0; n < pg.game.play.size(); n++)
		    {
		    	int y = pg.game.play.get(n).getY();
		    	int x = pg.game.play.get(n).getX();
		    	if(i == y && j == x)
		    	{
		    		int state = pg.game.play.get(n).getPeople();
			    	String people = Integer.toString(state);
			    	mark = people.charAt(0);   									//for people different posture while they moving
		    	}
		    }
			paint(mark);
			}
		}
		map.validate();
		map.repaint();
	}
	
	/**
	 * To add the JLabel into the map panel
	 */
	private void paint(char mark){
		switch (mark){
		case '0':
			JLabel PEOPLE_S = new JLabel(new ImageIcon("src/image/people-s.png"));
			map.add(PEOPLE_S);
			break;
		case '1':
			JLabel PEOPLE_N = new JLabel(new ImageIcon("src/image/people-n.png"));
			map.add(PEOPLE_N);
			break;
		case '2':
			JLabel PEOPLE_E = new JLabel(new ImageIcon("src/image/people-e.png"));
			map.add(PEOPLE_E);
			break;	
		case '3':
			JLabel PEOPLE_W = new JLabel(new ImageIcon("src/image/people-w.png"));
			map.add(PEOPLE_W);
			break;
		case 'A' :
			JLabel ARMOUR = new JLabel(new ImageIcon("src/image/armour.png"));
			map.add(ARMOUR);
			break;
		case '#':
			JLabel WALL = new JLabel(new ImageIcon("src/image/wall.png"));
			map.add(WALL);
			break;
		case 'H':
			JLabel HEALTH = new JLabel(new ImageIcon("src/image/health.png"));
			map.add(HEALTH);
			break;
		case 'L':
			JLabel LANTERN = new JLabel(new ImageIcon("src/image/LANTERN.png"));
			map.add(LANTERN);
			break;
		case 'X':
			JLabel NOTSEE = new JLabel(new ImageIcon("src/image/notSee.png"));
			map.add(NOTSEE);
			break;
		case '.':
			JLabel ROAD = new JLabel(new ImageIcon("src/image/road.png"));
			map.add(ROAD);
			break;
		case 'G':
			JLabel GOLD = new JLabel(new ImageIcon("src/image/gold.png"));
			map.add(GOLD);
			break;
		case 'E':
			JLabel EXIT = new JLabel(new ImageIcon("src/image/door.png"));
			map.add(EXIT);
			break;
		case 'S':
			JLabel SWORD = new JLabel(new ImageIcon("src/image/sword.png"));
			map.add(SWORD);
			break;
		default:
			break;
			}
	}
	
	/**
	 * To show the game over image in the map panel if the one player win the game.
	 */
	public void gameOver(){
		map.removeAll();
		map.setLayout(new BorderLayout());
		JLabel over = new JLabel(new ImageIcon("src/image/The-End.jpg"));
		map.add(over);
		map.validate();
		map.repaint();
	}
}
