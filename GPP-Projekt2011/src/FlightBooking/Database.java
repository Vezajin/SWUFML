
package FlightBooking;

/**
 * This class establishes a connection to the database, making it possible to
 * communicate with the database using commands.
 * @author Lollike
 */
import java.sql.*;


public class Database {
    private final Connection connection;
    private final Statement dbStatement;

 /**
 * The constructor attempts to establish a connection to the database.
 */    
    public Database() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException exn) {
            System.out.println("Could not find the search path: " + exn);
        }
        connection = DriverManager.getConnection("jdbc:mysql://mysql.itu.dk/Flightbooking", "swufml", "swufml321");        
        dbStatement = connection.createStatement();
    }
    
/**
 * Method to execute database commands.
 */
    public ResultSet execute(String cmd) throws SQLException {
        System.out.println(cmd);
        boolean ok = dbStatement.execute(cmd);
        if (ok)
            return dbStatement.getResultSet();
        else
            return null;
    }
    
/**
 *  Terminates the connection to the database. 
 */    
    public void close() throws SQLException {
        connection.close();
    }
}
