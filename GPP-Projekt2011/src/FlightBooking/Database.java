/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FlightBooking;

/**
 *
 * @author Lollike
 */
import java.sql.*;


public class Database {
    private final Connection connection;
    private final Statement dbStatement;
    
    public Database() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException exn) {
            System.out.println("Could not find the search path: " + exn);
        }
        connection = DriverManager.getConnection("jdbc:mysql://mysql.itu.dk/Flightbooking", "swufml", "swufml321");        
        dbStatement = connection.createStatement();
    }
    
    public ResultSet execute(String cmd) throws SQLException {
        System.out.println(cmd);
        boolean ok = dbStatement.execute(cmd);
        if (ok)
            return dbStatement.getResultSet();
        else
            return null;
    }
    
    public void close() throws SQLException {
        connection.close();
    }
    
    
}
