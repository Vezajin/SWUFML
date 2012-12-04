package FlightBooking;

import java.sql.*;
/**
 * This class inserts the data from an order into the Database.
 * @author Lollike
 */


public class Order {
    private int key, customer, flight; // Fields for corresponding schemas in the database for primary key, customerid and flightid.
    private String seat, name;

    
/**
 * The constructor takes the required information for an order, which is
 * customer ID, flight ID, a string of ordered seats and a string of names
 * of the travelers.
 */    
    public Order(int c, int f, String s, String n) {
        key = 0;
        customer = c;
        flight = f;
        seat = s;
        name = n;
    }

/**
 * Initializes the ResultSet, making the link between the fields of this class,
 * and the columns in the database.
 */    
    public void init(ResultSet rs) throws SQLException {
        key = rs.getInt("id");
        customer = rs.getInt("customerid");
        flight = rs.getInt("flightid");
        seat = rs.getString("seatstring");
        name = rs.getString("namestring");
    }

/**
 *  Initializes the ResultSet from the primary key id = k in the database.
 */    
    public Order(Database db, int k) throws SQLException {
        ResultSet rs = db.execute("SELECT * FROM Orders WHERE customerid = " + k);
        rs.next();
        init(rs);
    }

/**
 * Inserts the data of the fields into the corresponding columns in the order
 * table of the database.
 */    
    public void insert(Database db) throws SQLException {
        db.execute("INSERT INTO Orders (customerid, flightid, seatstring, namestring) VALUES ('" 
                + customer + "', '" + flight + "', '" + seat + "', '" + name + "')");
        ResultSet rs = db.execute("SELECT MAX(id) as 'max' FROM Orders");
        rs.next();
        key = rs.getInt("max");
    }
    
    // Returns customer id.
    public int getCostumer() {
        return customer;
    }
    
    // Returns flight id.
    public int getFlight() {
        return flight;
    }
    
    // Returns the string of ordered seats.
    public String getSeat() {
        return seat;
    }
    
    // Returns the string of names of the travelers.
    public String getName() {
        return name;
    }
}
