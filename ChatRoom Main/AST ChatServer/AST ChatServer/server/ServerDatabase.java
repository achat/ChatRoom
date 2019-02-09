package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

/** This class manages the connection to the database and performs all the
 * database related actions. We are using an Oracle Database. */
public class ServerDatabase{
	private Calendar calendar;										//For keeping time logs.
	private Connection connection=null; 							//Connection to Database.
	private Statement statement=null;								//The SQL statement to execute.
	private static int id;											//The primary key in the table chatroom_logs.
	
	public ServerDatabase(){
		connectToDatabase();
		//TODO Implement another table that hold user messages.
	}
	
	/** Establish a JDBC Connection to the Oracle Database.
 	 *	Then check if "LOGS" table exists, else create it.  */
	private void connectToDatabase(){
		final String JDBC_DRIVER="oracle.jdbc.driver.OracleDriver";	//JDBC driver name 
		final String DB_URL="jdbc:oracle:thin:@localhost:1521:xe";	//Database URL
		final String USER="System";									//Database credentials
		final String PASS="12345";			
		
		try{
			System.out.println("[System] Registering JDBC Driver...");		
			Class.forName(JDBC_DRIVER);
			
			System.out.println("[System] Registered. Connecting to database...");
			connection=DriverManager.getConnection(DB_URL,USER,PASS);
			connection.setAutoCommit(true);
			statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			System.out.println("[System] Connected. Checking if table \"chatroom_logs\" exists...");
			createLogsTable();
			
			System.out.println("[System] Database is ready.");
			//statement.close();
			//connection.close();
		}catch( ClassNotFoundException | SQLException exception){
			exception.printStackTrace();
		}
	}
	
	/** Check if table Logs already exists.
	 * 	If this is the case then set the id and counter according to the database table data. 
	 *	Else create the table.  */
	private void createLogsTable() throws SQLException{
		String[] types={"TABLE"};
		ResultSet resultSet=connection.getMetaData().getTables( null, null, "%", types);
		
		while( resultSet.next() ){							//Check if chatroom_logs already exists
			String tableName=resultSet.getString( 3); 		//Corresponds to table name.
			if (tableName.equalsIgnoreCase( "chatroom_logs") ){
				System.out.println("[System] Table: "+tableName+" already exists."
					+ " Retrieving the value of id from "+tableName);
				
				//Then set the id according to the database table data. 
				id=getValue("SELECT MAX(id) AS max_id FROM chatroom_logs","max_id");				
				return;
			}
		}
		
		//Table does not exist. Then create it.
		statement.execute( "CREATE TABLE chatroom_logs (id NUMBER(10) NOT NULL, ip VARCHAR2(16) NOT NULL,"
				+ " name VARCHAR2(25) NOT NULL, day NUMBER(10) NOT NULL, hour NUMBER(10) NOT NULL,"
				+ " operation VARCHAR2(16) NOT NULL, PRIMARY KEY(id) ) ");		
		System.out.println("[System] Table chatroom_logs created.");
	}
	
	/** Get the value of a variable as an integer from the database.
	 *  Notice that the String "sqlStatement" should contain the String "reference".
	 *  i.e. sqlStatement="SELECT MAX(id) AS max_id FROM charoom_logs" and reference="max_id".
	 * @param sqlStatement The SQL Statement to execute.
	 * @param reference The variable who's value should be returned. */
	private int getValue(String sqlStatement,String reference) throws SQLException{
		int value=0;
		
		PreparedStatement preparedStatement=connection.prepareStatement( sqlStatement);
		ResultSet resultSet=preparedStatement.executeQuery();
		while( resultSet.next() )
			value=resultSet.getInt( reference);				
		System.out.println("[System] "+reference+" is currently:"+value);
		return value;	
	}
	
	/** Update the Logs table in the Database according to the operation performed by the client.
  	Each operation stores the ip, day of year, hour of day, type of operation and the counter value.
  	@param operation The type of operation performed. (Increased/Decreased/Read)
  	@param clientIP The IP of the client. 
  	@param clientName the name of the client. */
	public synchronized void updateLogsTable(String operation, String clientIP, String clientName){
		System.out.println("[System] IP of "+clientName+": "+clientIP );	//Print client's ip.
		//System.out.println("[System] Client's name: "+clientName );
		
		calendar=Calendar.getInstance();
		int day=calendar.get( Calendar.DAY_OF_YEAR);
		int hour=calendar.get( Calendar.HOUR_OF_DAY);			
				
		//System.out.println("[System] Day of year: "+ day );
		//System.out.println("[System] Hour of day: "+ hour );

		id++;
		
		try{ //Insert log into the table.
			statement.execute("INSERT INTO chatroom_logs VALUES ("
				+id+","+"\'"+clientIP+"\'"+","+"\'"+clientName+"\'"+","+day+","+hour+","+"\'"+operation+"\'"+")" );
		}catch( SQLException exception){ exception.printStackTrace(); }	
	}
	
}