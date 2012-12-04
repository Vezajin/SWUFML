/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FlightBooking;

import java.sql.*;


/**
 * This class handles the information involving flights to be submitted into
 * the database.
 * @author Lollike
 */
public class Flight {
    private int key, startdes, enddes, numberOfSeats;
    private Date date;
    public Flight(int s, int e, Date d, int ns) {
        key = 0;
        startdes = s;
        enddes = e;
        date = d;
        
        Seat Seats[] = new Seat[ns];
    }
    
    // Initializes the ResultSet
    public void init(ResultSet rs) throws SQLException {
        key = rs.getInt("id");
        startdes = rs.getInt("startdestination");
        enddes = rs.getInt("enddestination");
        date = rs.getDate("date");
    }
    
    public Flight(Database db, int k) throws SQLException {
        ResultSet rs = db.execute("SELECT * FROM Flights WHERE id = " + k);
        rs.next();
        init(rs);
    }
    
    // Inserts the information in the fields into correspondong columns in db.
    public void insert(Database db) throws SQLException {
        db.execute("INSERT INTO Flights (startdestination, enddestination, date) VALUES ('" + startdes + "', '" + enddes + "', '" + date + "')");
    }
    
    // Returns primary key.
    public int getKey() {
        return key;
    }
    
    // Returns start destination ID.
    public int getStartDestination() {
        return startdes;
    }
    
    // Returns end destination ID.
    public int getEndDestination() {
        return enddes;
    }
    
    // Returns the date.
    public Date getDate() {
        return date;
    }
}
