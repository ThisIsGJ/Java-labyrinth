import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.TitledBorder;


/**
 * This class is used for the client connecting to server and making the 
 * client interact with the server .
 * This class is used for createing the interface for the Cient and game.
 * @author Guanjun
 *
 */

public class ClientGUI extends JFrame implements ActionListener {
	
	public static void main(String[] args) throws IOException{
		System.out.println("start the game");
		ClientGUI c = new ClientGUI();
		c.makeFrame();
	}
	
	/**
	 * for connecting to server
	 */
	private PrintWriter out = null;
    private BufferedReader in = null;
    private String fromServer;
    private JTextField IP;
    private JTextField port;
    private JTextField name;
	
	/**
	 * JPanel
	 */
	 private JPanel pane;		//main pane
	 private JPanel pane1;		//use for "G","A","H","S","T"
	 private JPanel pane2;						//the play pane
	 private JPanel pane3;		//contain pane1 and pane 2
	 private JPanel pane5;		//control panel
	 private JPanel sub_Panel;
	 private JLabel lab_H, lab_S, lab_A, lab_L, lab_G,lab_T,lab_W; // the label for panel1
		
	 /*
	  * JLabel
	  */
	 private JLabel dungLabel;
	 private JLabel win;
	 private JLabel lose;
	 
	 private boolean startGame;
	 private boolean attack;
	 private int rows, cols;
	 private String s_Test,a_Test,h_Test,g_Test,t_Test,w_Test,l_Test;
	 
	 public int people = 0; 													//people's posture 
	 private Timer timer = new Timer();;										//update map per 2s.
	 
	 ClientGUI() throws IOException{
		 
		 pane = new JPanel();
		 pane1 = new JPanel();
		 pane2 = new JPanel();
		 pane3 = new JPanel();
		 pane5 = new JPanel();
		 
		 dungLabel = new JLabel(new ImageIcon("src/image/dungeonDoom.png"));
		 win = new JLabel(new ImageIcon("src/image/win.jpg"));
		 lose = new JLabel(new ImageIcon("src/image/lose.png"));
		 
		 startGame = false;														//if game have't start, cannot use the control room
		 attack = false;
		 this.rows = 5;
		 this.cols = 5;
		 in = null;
		 out = null;
		 
		 s_Test = "0";							//sword
		 a_Test = "0";							//armour
		 h_Test = "0";							//health
		 t_Test = "0";							//step remain
		 w_Test = "0";							//win Gold
		 l_Test = "0";							//lantern
		 g_Test = "0";							//gold picked up
		 
	}
	
	private void makeFrame() throws IOException{
		setTitle("Dungeon doom");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		 panel1();
		 subFrame();													//ip address, port number
		 panel2();
		 panel5();
		 
		 pane3.setLayout(new BorderLayout());
		 pane3.add(pane1, BorderLayout.NORTH);
		 pane3.add(pane2, BorderLayout.CENTER);
		 pane3.add(pane5,BorderLayout.SOUTH);
		 
		 pane.setLayout(new BorderLayout());
		 pane.add(pane3,BorderLayout.CENTER);
		 
		 Container con = getContentPane();
		 setSize(600,550);
		 con.add(pane);
		 setVisible(true);	
		 } 
	 
	private void panel1(){
		pane1.setBorder(new TitledBorder("Detail"));
		lab_A = new JLabel("Armour: " + a_Test + "    ");
		lab_S = new JLabel("Sword: " + s_Test + "    ");
		lab_G = new JLabel("Gold: " + g_Test + "    ");
		lab_H = new JLabel("Health: " + h_Test + "    ");
		lab_T = new JLabel("Step: " + t_Test + "    ");
		lab_W = new JLabel("Win: " + w_Test + "    ");
		lab_L = new JLabel("Lantern: " + l_Test + "    ");
		
		pane1.setLayout(new FlowLayout());
	    pane1.add(lab_T);
	    pane1.add(lab_W);
	    pane1.add(lab_G);
		pane1.add(lab_H);
		pane1.add(lab_A);
		pane1.add(lab_S);
		pane1.add(lab_L);
		
		 
	}
	
	private void subFrame(){
		sub_Panel = new JPanel();
		sub_Panel.setBorder(new TitledBorder("Connect To Server"));
		pane2.setLayout(new FlowLayout());
		IP = new JTextField(12);
		port = new JTextField(5);
		name = new JTextField(8);
		JLabel IP_word = new JLabel("IP:");
		JLabel port_word = new JLabel("PORT:");
		JLabel name_word = new JLabel("Your Name:");
		JButton OK = new JButton("Play");
		OK.addActionListener(this);
		sub_Panel.setSize(100,100);
		sub_Panel.add(name_word);
		sub_Panel.add(name);
		sub_Panel.add(IP_word);
		sub_Panel.add(IP);
		sub_Panel.add(port_word);
		sub_Panel.add(port);
		sub_Panel.add(OK);
	}
	
	private void panel2() throws IOException{
		
		//before the game start
 		pane2 = new JPanel();
		pane2.setPreferredSize(new Dimension(600,400));
		pane2.setLayout(new BorderLayout());
		pane2.add(sub_Panel,BorderLayout.NORTH);
		pane2.add(dungLabel,BorderLayout.CENTER);		
		
	}
	
	private void panel5(){
		pane5.setBorder(new TitledBorder("Control Room"));
		pane5.setPreferredSize(new Dimension(80,80));
		pane5 = new JPanel(new GridLayout(2,5));
		
		pane5.add(new JPanel());
		addButton(pane5,"Pick Up");
		addButton(pane5,"N");
		addButton(pane5,"Quit");
		pane5.add(new JPanel());
		pane5.add(new JPanel());
		addButton(pane5,"W");
		addButton(pane5,"S");
		addButton(pane5,"E");
		
	}
	
	//add button to panel
	private void addButton(Container p, String button){
		JButton btn = new JButton(button);
		btn.addActionListener(this);
		p.add(btn);
	}
	
	
	private void playerView(){
		pane2.removeAll();
		pane2.setBorder(new TitledBorder("Map"));
		String playerView = fromServer;
		int length = playerView.length();
		rows = cols = (int) Math.sqrt(length);
		pane2.setLayout(new GridLayout(rows, cols));

		//add label into the panel
		for(int i = 0; i < length; i++){
			char mark = playerView.charAt(i);
			if(i == (rows*rows-1)/2){
				// if the people stand on the exit door, test win here.
				if(mark == '4' || mark == '5' || mark == '6'||mark == '7'){
					testWin();
					String number0 = String.valueOf(mark);
					int number1 = Integer.parseInt(number0);
					String number2 = Integer.toString(number1 - 4);
					mark = number2.charAt(0);
					}
			}
			switch (mark){
			case '0':
				JLabel PEOPLE_S = new JLabel(new ImageIcon("src/image/people-s.png"));
				pane2.add(PEOPLE_S);
				break;
			case '1':
				JLabel PEOPLE_N = new JLabel(new ImageIcon("src/image/people-n.png"));
				pane2.add(PEOPLE_N);
				break;
			case '2':
				JLabel PEOPLE_E = new JLabel(new ImageIcon("src/image/people-e.png"));
				pane2.add(PEOPLE_E);
				break;	
			case '3':
				JLabel PEOPLE_W = new JLabel(new ImageIcon("src/image/people-w.png"));
				pane2.add(PEOPLE_W);
				break;
			case 'A' :
				JLabel ARMOUR = new JLabel(new ImageIcon("src/image/armour.png"));
				pane2.add(ARMOUR);
				break;
			case '#':
				JLabel WALL = new JLabel(new ImageIcon("src/image/wall.png"));
				pane2.add(WALL);
				break;
			case 'H':
				JLabel HEALTH = new JLabel(new ImageIcon("src/image/health.png"));
				pane2.add(HEALTH);
				break;
			case 'L':
				JLabel LANTERN = new JLabel(new ImageIcon("src/image/LANTERN.png"));
				pane2.add(LANTERN);
				break;
			case 'X':
				JLabel NOTSEE = new JLabel(new ImageIcon("src/image/notSee.png"));
				pane2.add(NOTSEE);
				break;
			case '.':
				JLabel ROAD = new JLabel(new ImageIcon("src/image/road.png"));
				pane2.add(ROAD);
				break;
			case 'G':
				JLabel GOLD = new JLabel(new ImageIcon("src/image/gold.png"));
				pane2.add(GOLD);
				break;
			case 'E':
				JLabel EXIT = new JLabel(new ImageIcon("src/image/door.png"));
				pane2.add(EXIT);
				break;
			case 'S':
				JLabel SWORD = new JLabel(new ImageIcon("src/image/sword.png"));
				pane2.add(SWORD);
				break;
			default:
				break;
			}
		}
		pane2.validate();
		pane2.repaint();
		
	}
	
	//update panel1(the deatil of player)
	private void repaintPanel1(){
		pane1.removeAll();
		panel1();																										//gold picked up	
		pane1.validate();
		pane1.repaint();
	}
	
	//test whether the player have win the game
	public void testWin(){
		if(Integer.parseInt(g_Test) >= Integer.parseInt(w_Test) && (!g_Test.equals("0"))){
			pane2.removeAll();
			pane2.setLayout(new BorderLayout());
			pane2.add(win);
			pane2.validate();
			pane2.repaint();
			startGame = false;
			timer.cancel();
			out.println("win");
		}
	}
	
	//connect to server
	public void connectToServer(String IP_ADRESS,int port){

	    Socket localSocket = null;

	    try
	    {
	      //IP adress and Port number of the server,connect to the server
	      localSocket = new Socket(IP_ADRESS, port);													
	      System.out.println("Player has connected");
	      //send to server
	      out = new PrintWriter(localSocket.getOutputStream(), true);
	      //received from server
	      in = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));
	      
	    } catch (IOException localIOException) {
	      System.err.println("Couldn't get I/O for the connection to: " + port);
	      //System.exit(1);
	    }
	    
	      timer.scheduleAtFixedRate(new TimerTask(){
	    	  	public void run(){
	    	  		sendToServer("updateMap");
	    	  	}
	      },2000,2000);
	    
	    sendToServer("look");
	}
	
	//send message to server and get message from the server
	private void sendToServer(String message){
		 String toServer = (people+"\n"); 
		 String firstLine = "";
		 toServer += message;
		 out.println(toServer);
		 fromServer = null;
		 try {
			 firstLine = in.readLine();
			 if(firstLine.equals("lose")){						//test whether lose the game
				 loseGame();
			 }else{ 
			  s_Test = firstLine;
			  a_Test = in.readLine();							//armour
			  h_Test = in.readLine();							//health
			  t_Test = in.readLine();							//step remain
			  w_Test = in.readLine();							//win Gold
			  l_Test = in.readLine();							//lantern
			  g_Test = in.readLine();							//gold have picked up
			  fromServer = in.readLine();
			  repaintPanel1();
			  playerView(); 
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//lose the game
	private void loseGame(){
			pane2.removeAll();
			pane2.setLayout(new BorderLayout());
			pane2.add(lose);
			pane2.validate();
			pane2.repaint();
			startGame = false;
			timer.cancel();
	}
	
	public void actionPerformed(ActionEvent e) {
		String choice = e.getActionCommand();
		if(choice.equals("Play")){
			startGame = true;
			String Ip = IP.getText();
			try{
			 int Port = Integer.parseInt(port.getText());
			 connectToServer(Ip, Port);
			}catch(NumberFormatException nfe){
				System.err.println("Port only could be number.");
			}
		}
		else if(choice.equals("Quit")){System.exit(0);}
		if(startGame){
			if(attack){}
			else if(choice.equals("N")){people = 1; sendToServer("MOVE N"); }
			else if(choice.equals("S")){people = 0; sendToServer("MOVE S"); }
			else if(choice.equals("E")){people = 2; sendToServer("MOVE E"); }
			else if(choice.equals("W")){ people = 3;sendToServer("MOVE W");}
			else if(choice.equals("Pick Up")){ sendToServer("PICKUP"); }
		}
	}
}
	
	

	
	
	
	
	
	
	
	
	

