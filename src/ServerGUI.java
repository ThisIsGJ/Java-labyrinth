import java.net.*;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
 
/**
 * This class is the server interface class.This class is used to 
 * make interface for the Server and show the ip address and port number
 * in the server interface. Server also will get port number address from
 * this class. 
 * 
 * @author Guanjun
 *
 */
public class ServerGUI extends JFrame implements ActionListener {
	
	private JPanel pane1;     											//ip and port 
	private JTextField port_text;
	private JTextField ip_text;
	private JRadioButton listen;
	public boolean listening = false;									//turn on listen
	private JRadioButton notListen; 									//turn off listen
	private JComboBox mapChoice;										//choose map
	private int port = 01;
	private Server server;
	private Thread se;
	private String mapName = "";
	
	
	private static final String[] maps = {	"Default Map", "simple.txt", "complex.txt"};	//maps' name
	
	public static void main(String[] args){
    	new ServerGUI();
    }
    
	/**
	 * To make frame for the server to get the port number and the map name.
	 *You could also reset the port number and change the port number any you want.
	 */
	 ServerGUI() {
		
		setTitle("Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pane1 = new JPanel();
		pane1.setBorder(new TitledBorder("Server"));
		pane1.setLayout(new FlowLayout());
		pane1.setPreferredSize(new Dimension(100,100));
		
		JLabel IP = new JLabel("IP:");
		JLabel PORT = new JLabel("PORT:");
		JLabel map = new JLabel("Map Name:");
		port_text = new JTextField(4);
		port_text.setText("6666");														//default port number 6666 
		ip_text = new JTextField(15);
		try {
			ip_text.setText(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		ip_text.setEditable(false);
		mapChoice = new JComboBox( maps );
		mapChoice.setMaximumRowCount(3);
		
		ButtonGroup bg = new ButtonGroup();
		listen = new JRadioButton("Listen ON", false);
		listen.addActionListener(this);
		notListen = new JRadioButton("Listen OFF", true);
		notListen.addActionListener(this);
		bg.add(listen);
		bg.add(notListen);
		JButton stop = new JButton("Stop Game");
		stop.addActionListener(this);
		
		
		pane1.add(IP);
		pane1.add(ip_text);
		pane1.add(PORT);
		pane1.add(port_text);
		pane1.add(map);
		pane1.add(mapChoice);
		pane1.add(listen);
		pane1.add(notListen);
		pane1.add(stop);
		
		Container con = getContentPane();
		setSize(900,80);
		con.add(pane1);
		setVisible(true);
	}
    
	 
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		if(command.equals("Listen ON")){
			
			mapName = (String) mapChoice.getSelectedItem();
			
			port = Integer.parseInt(port_text.getText());
			if(port > 52799){
				port = 52799;										//the maximum port number is 52799
				System.out.println("Sorry, The maximum port number is 52799.");
			}
			port_text.setEditable(false);							//if the server start listening, user cannot change the port number.
			
			if(port == 01){port = 6666;}							//set for default port number if the user forget press the ON button 
			notListen.setEnabled(false);							//disable the notListen radio button if server start listening
			server = new Server(port,mapName);
			se = new Thread(server);
			se.start();												//server start listening
		}
		else if(command.equals("Stop Game")){
			notListen.setEnabled(true);
			server.closeGodView();									//close the godView if user stop the game
			se.stop();												
			notListen.setSelected(true);
			port_text.setEditable(true);							// make the port text field editable if the user stop the game. 
		}
	
	}
}




