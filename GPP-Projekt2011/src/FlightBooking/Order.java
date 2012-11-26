/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FlightBooking;

import java.sql.*;
/**
 *
 * @author Lollike
 */
public class Order {
    private int key;
    private int customer;
    private int flight;
    private int seat;
    
    public Order(int c, int f, int s) {
        key = 0;
        customer = c;
        flight = f;
        seat = s;
    }
    
    public void init(ResultSet rs) throws SQLException {
        key = rs.getInt("id");
        customer = rs.getInt("customerid");
        flight = rs.getInt("flightid");
        seat = rs.getInt("seatid");
    }
    
    public Order(Database db, int k) throws SQLException {
        ResultSet rs = db.execute("SELECT * FROM Orders WHERE id = " + k);
        rs.next();
        init(rs);
    }
    
    public void insert(Database db) throws SQLException {
        db.execute("INSERT INTO Orders (customerid, flightid, seatid) VALUES ('" 
                + customer + "', '" + flight + "', '" + seat + "')");
    }
    
    public int getCostumer() {
        return customer;
    }
    
    public int getFlight() {
        return flight;
    }
    
    public int getSeat() {
        return seat;
    }
}
