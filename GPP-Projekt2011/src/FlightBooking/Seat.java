package FlightBooking;

import java.sql.*;
/**
 *
 * @author Nikolaj
 */
public class Seat {
    private int key, flightid, customerid;
    private String seatName;
    
    public Seat(int f, int o, int c, String sn) {
        key = 0;
        flightid = f;
        customerid = c;
        seatName = sn;
    }
    
    public void init(ResultSet rs) throws SQLException {
        key = rs.getInt("id");
        flightid = rs.getInt("flightid");
        customerid = rs.getInt("customerid");
    }
    
    public Seat(Database db, int k) throws SQLException {
        ResultSet rs = db.execute("SELECT * FROM Seats WHERE id = " + k);
        rs.next();
        init(rs);
    }
    
    public void insert(Database db) throws SQLException {
        db.execute ("INSERT INTO Seats (flightid, customerid) VALUES ('" + flightid + "', '" + customerid + "')");
        ResultSet rs = db.execute("SELECT MAX(id) as 'max' FROM Seats");
        rs.next();
        key = rs.getInt("max");
    }
    
    public int getKey() {
        return key;
    }
    
    public int getFlightid() {
        return flightid;
    }
    
    public int getCustomerid() {
        return customerid;
    }    
}
