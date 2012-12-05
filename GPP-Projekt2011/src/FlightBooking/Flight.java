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
    private int key, startdes, enddes, numberofseats, bookedseats;
    private String timestamp;
    private Date date;
    public Flight(int s, int e, Date d, int ns, int bs, String t) {
        key = 0;
        startdes = s;
        enddes = e;
        date = d;
        numberofseats = ns;
        bookedseats = bs;
        timestamp = t;
    }
    
    // Initializes the ResultSet
    public void init(ResultSet rs) throws SQLException {
        key = rs.getInt("id");
        startdes = rs.getInt("startdestination");
        enddes = rs.getInt("enddestination");
        date = rs.getDate("date");
        numberofseats = rs.getInt("numberofseats");
        bookedseats = rs.getInt("bookedseats");
        timestamp = rs.getString("timestamp");
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
    
    // Deletes the entry with the specified primary key in the database.
    public void delete(Database db, int k) throws SQLException {
        db.execute("DELETE FROM Flights WHERE id = " + k);
    }
    
    // Selects the flights from the database for one specific date.
    public static void getSpecDateFlights(Database db, String year1, String month1, String day1) throws SQLException {
        ResultSet rs = db.execute("SELECT * FROM Flights WHERE date = " + year1 +"-"+ month1 +"-"+ day1);
    }
    
    // Selects the flights from the database between two specified dates.
    public static void getDateFlights(Database db, String year1, String month1, String day1, String year2, String month2, String day2) throws SQLException {
        ResultSet rs = db.execute("SELECT * FROM Flights WHERE date between " + year1 +"-"+ month1 +"-"+ day1 + " AND " + year2 +"-"+ month2 +"-"+ day2);
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
    
    public int getNumberOfSeats()
    {
        return numberofseats;
    }
    
    public int getBookedSeats() {
        return bookedseats;
    }
    
    public String timestamp() {
        return timestamp;
    }
}
