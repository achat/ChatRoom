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

/** User Interface code. */
public class ClientUserInterface{
	private ClientImplementation clientImplementation;
	private ServerInterface serverInterface;
	
	JTextArea jTextArea;
	JTextField messageTextfield, ipTextField, nameTextField;
	JButton connectButton;	//Connect Button
	JList<String> jList;	//this is the list on the right that contains the users connected to the chatroom
	JFrame jFrame;
	
	public ClientUserInterface(){
		jFrame=new JFrame( "Chatroom");
		JPanel main=new JPanel();
		JPanel top=new JPanel();
		JPanel center=new JPanel();
		JPanel bottom=new JPanel();
		
		ipTextField=new JTextField();
		messageTextfield=new JTextField();
		nameTextField=new JTextField();
		
		jTextArea=new JTextArea();
		connectButton=new JButton( "Connect");
		JButton sendButton=new JButton( "Send");
		jList=new JList<String>();
		
		main.setLayout( new BorderLayout( 5, 5) );
		top.setLayout( new GridLayout( 1, 0, 5, 5) );
		center.setLayout( new BorderLayout( 5, 5) );
		bottom.setLayout( new BorderLayout( 5, 5) );
		
		top.add( new JLabel( "Your name: ") );
		top.add( nameTextField);
		top.add( new JLabel( "Server Address: ") );
		top.add( ipTextField);
		top.add( connectButton);
		
		center.add( new JScrollPane( jTextArea), BorderLayout.CENTER);
		center.add( jList, BorderLayout.EAST);
		
		bottom.add( messageTextfield, BorderLayout.CENTER);
		bottom.add( sendButton, BorderLayout.EAST);
		
		main.add( top, BorderLayout.NORTH);
		main.add( center, BorderLayout.CENTER);
		main.add( bottom, BorderLayout.SOUTH);
		main.setBorder( new EmptyBorder( 10, 10, 10, 10) );
		
		//Action Listeners for Events.
		connectButton.addActionListener( new ActionListener(){
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
	
		jFrame.setContentPane( main);
		jFrame.setSize( 600, 600);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//Terminate program when "X" is pressed.
		jFrame.setLocationRelativeTo(null);						//Place the frame to the screen's center.
		jFrame.setVisible( true);
	}	
	
	public void doConnect(){
		if( connectButton.getText().equals( "Connect") ){
			if( nameTextField.getText().length()<2){	//Do not accept names with 1 or less characters.
				JOptionPane.showMessageDialog( jFrame, "You need to type a name with more than 1 characters.");
				return;
			}
			
			if( ipTextField.getText().length()<2){		//Do not accept ip with 1 or less characters.
				JOptionPane.showMessageDialog( jFrame, "You need to type an IP with more than 1 characters.");
				return;
			}

			try{
				clientImplementation=new ClientImplementation( nameTextField.getText() );
				clientImplementation.setGUI( this);

				serverInterface=( ServerInterface) Naming.lookup( "rmi://"+ipTextField.getText()+"/Chatroom");
				serverInterface.login( clientImplementation);	//try to connect to chatroom.
				
				updateConnectedUsersList( serverInterface.getConnectedClients() ); //add client to clients list.
				connectButton.setText( "Disconnect");			//change button label to Disconnect.
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
			connectButton.setText( "Connect");
			//TODO: Implement Logout ....
		}
	}

	public void sendText(){
		if( connectButton.getText().equals( "Connect") ){ //Can not send message unless first connected.
			JOptionPane.showMessageDialog( jFrame, "You need to connect first.");
			return;
		}
		
		String message=messageTextfield.getText();			//get message from textField.
		message="["+nameTextField.getText()+"] "+message; 	//add the user's name.
		messageTextfield.setText( "");						//set textField to empy again.

		//Remove if you are going to implement for remote invocation
		try{
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
	public void updateConnectedUsersList( Vector<?> vector){
		DefaultListModel<String> connectedUsersList=new DefaultListModel<String>();

		if( vector!=null) for( int i=0; i<vector.size(); i++){
			try{
				String userName=( ( ClientInterface) vector.get( i) ).getName();
				connectedUsersList.addElement( userName);
			}catch( Exception exception){
				exception.printStackTrace();
			}
		}
		jList.setModel( connectedUsersList);
	}
}