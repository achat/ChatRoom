package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import chatroominterface.ClientInterface;
import chatroominterface.ServerInterface;

/** User Interface code. Everything related to the GUI and the Logic behind it. */
public class ClientUserInterface{
	private static final String CONNECT="Connect";
	private ClientImplementation clientImplementation;
	private ServerInterface serverInterface;
	
	private JTextArea jTextArea;
	private JTextField messageTextfield, ipTextField, nameTextField;
	private JButton connectionButton;	//Connect/Disconnect Button
	private JList<String> jList;		//this is the list on the right that contains the users connected to the chatroom
	private JFrame jFrame;
	
	/** The constructor is used to set up the GUI and the action Listeners behind the GUI elements. */
	public ClientUserInterface(){
		jFrame=new JFrame( "Chatroom");
		JPanel panelMain=new JPanel();
		JPanel panelTop=new JPanel();
		JPanel panelCenter=new JPanel();
		JPanel panelBottom=new JPanel();
		
		ipTextField=new JTextField();
		messageTextfield=new JTextField();
		nameTextField=new JTextField();
		
		jTextArea=new JTextArea();
		connectionButton=new JButton( CONNECT);
		JButton sendButton=new JButton( "Send");
		jList=new JList<>();
		
		panelMain.setLayout( new BorderLayout( 5, 5) );
		panelTop.setLayout( new GridLayout( 1, 0, 5, 5) );
		panelCenter.setLayout( new BorderLayout( 5, 5) );
		panelBottom.setLayout( new BorderLayout( 5, 5) );
		
		panelTop.add( new JLabel( "User Name: ") );
		panelTop.add( nameTextField);
		panelTop.add( new JLabel( "Server Address: ") );
		panelTop.add( ipTextField);
		panelTop.add( connectionButton);
		
		panelCenter.add( new JScrollPane( jTextArea), BorderLayout.CENTER);
		panelCenter.add( jList, BorderLayout.EAST);
		
		panelBottom.add( messageTextfield, BorderLayout.CENTER);
		panelBottom.add( sendButton, BorderLayout.EAST);
		
		panelMain.add( panelTop, BorderLayout.NORTH);
		panelMain.add( panelCenter, BorderLayout.CENTER);
		panelMain.add( panelBottom, BorderLayout.SOUTH);
		panelMain.setBorder( new EmptyBorder( 10, 10, 10, 10) );
	
		//Action Listeners for Events.
		connectionButton.addActionListener( new ActionListener(){
			@Override
			/** When connect button pressed doConnect() */
			public void actionPerformed( ActionEvent actionEvent){
				doConnect();
			}
		});
	
		sendButton.addActionListener( new ActionListener(){
			@Override
			/** When send button pressed sendText() */
			public void actionPerformed( ActionEvent actionEvent){
				sendText();
			}
		} );
		
		messageTextfield.addActionListener( new ActionListener(){
			@Override
			/** When enter pressed in text field, sendText() */
			public void actionPerformed( ActionEvent actionEvent){
				sendText();
			}
		});
	
		jFrame.setContentPane( panelMain);
		jFrame.setSize( 600, 600);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//Terminate program when "X" is pressed.
		//TODO: Logout when "X" is pressed.
		jFrame.setLocationRelativeTo(null);						//Place the frame to the screen's center.
		jFrame.setVisible( true);
	}	

	/** The actual method to connect/disconnect to/from the server.
	 * 	When action to connect, checks if name and ip are typed, accesses the server's remote object,
	 *  logs in and updates the connected users list.
	 *  When action to disconnect, logs out and updates the connected users list. */
	public void doConnect(){
		if( connectionButton.getText().equals( CONNECT) ){
			//TODO More rigid name and IP length tests.
			if( nameTextField.getText().length()<2){			//Do not accept names with 1 or less characters.
				JOptionPane.showMessageDialog( jFrame, "You need to type a name with more than 1 characters.");
				return;
			}
			
			if( ipTextField.getText().length()<2){				//Do not accept ip with 1 or less characters.
				JOptionPane.showMessageDialog( jFrame, "You need to type an IP with more than 1 characters.");
				return;
			}

			try{
				clientImplementation=new ClientImplementation( nameTextField.getText() );
				clientImplementation.setGUI( this);
				
				serverInterface=( ServerInterface) Naming.lookup( "rmi://"+ipTextField.getText()+"/Chatroom");
				serverInterface.login( clientImplementation);	//try to connect to chatroom.
				
				updateConnectedUsersList( serverInterface.getConnectedClients() ); //add client to clients list.
				connectionButton.setText( "Disconnect");		//change button label to Disconnect.
			}catch( Exception exception){
				exception.printStackTrace();
				JOptionPane.showMessageDialog( jFrame, "ERROR, we couldn't connect....");
			}
		}else{
			try{ //try to disconnect from the chatroom.	
				serverInterface.logout( clientImplementation);
			}catch( RemoteException exception){
				exception.printStackTrace();
			}	
			updateConnectedUsersList( null);
			connectionButton.setText( CONNECT);
		}
	}

	/** The actual method to send the message written in the textArea.
	 * 	Checks if user connected, removes the text form the textArea, concatenates the user name and sends it. */
	public void sendText(){
		if( connectionButton.getText().equals( CONNECT) ){ 		//Can not send message unless first connected.
			JOptionPane.showMessageDialog( jFrame, "You need to connect first.");
			return;
		}
		//TODO Should not be able to send message when text is empty.
		String message=messageTextfield.getText();				//get message from textField.
		message="["+nameTextField.getText()+"] "+message; 		//add the user's name.
		messageTextfield.setText( "");							//set textField to empy again.

		try{ //Publish the message to everyone connected in the chatroom.
			serverInterface.publish( message);
		}catch( Exception exception){
			exception.printStackTrace();
		}
	}
	
	/** Write message to client's own textArea in the UI.
	 * @param message The message to display. */
	public void writeMessage( String message){
		jTextArea.setText( jTextArea.getText()+"\n"+message);	//Concatenate old text with the new message.
	}
	
	/** This method updates the user names shown on the right of the UI.
	 *  The names refer to the users connected to the chatroom.
	 *  @param vector The users connected to the chatroom according to the server. */
	public void updateConnectedUsersList( Vector<ClientInterface> vector){
		DefaultListModel<String> connectedUsersList=new DefaultListModel<String>();

		if( vector!=null) for( int i=0; i<vector.size(); i++){
			try{
				String userName=vector.get( i).getName();
				connectedUsersList.addElement( userName);
			}catch( Exception exception){
				exception.printStackTrace();
			}
		}
		jList.setModel( connectedUsersList);

	}
}