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
	}
}
		