package FlightBooking;

import java.sql.*;
/**
 *
 * @author Lollike
 */
public class Order {
    private int key, customer, flight;
    private String seat, name;
    
    public Order(int c, int f, String s, String n) {
        key = 0;
        customer = c;
        flight = f;
        seat = s;
        name = n;
    }
    
    public void init(ResultSet rs) throws SQLException {
        key = rs.getInt("id");
        customer = rs.getInt("customerid");
        flight = rs.getInt("flightid");
        seat = rs.getString("seatstring");
        name = rs.getString("namestring");
    }
    
    public Order(Database db, int k) throws SQLException {
        ResultSet rs = db.execute("SELECT * FROM Orders WHERE id = " + k);
        rs.next();
        init(rs);
    }
    
    public void insert(Database db) throws SQLException {
        db.execute("INSERT INTO Orders (customerid, flightid, seatstring, namestring) VALUES ('" 
                + customer + "', '" + flight + "', '" + seat + "', '" + name + "')");
        ResultSet rs = db.execute("SELECT MAX(id) as 'max' FROM Orders");
        rs.next();
        key = rs.getInt("max");
    }
    
    public int getCostumer() {
        return customer;
    }
    
    public int getFlight() {
        return flight;
    }
    
    public String getSeat() {
        return seat;
    }
    public String getName() {
        return name;
    }
}
