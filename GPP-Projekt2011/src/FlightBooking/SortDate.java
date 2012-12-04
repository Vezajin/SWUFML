/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FlightBooking;

import java.sql.*;

/**
 *
 * @author Nikolaj
 */

public class SortDate {
    private int key;
    private Date date;
    
    
    public void init(ResultSet rs) throws SQLException {
        key = rs.getInt("id");
        date = rs.getDate("date");
    }
    
    public void getDates(Database db, int d) throws SQLException {
        ResultSet rs = db.execute("SELECT * FROM Flights WHERE date < ");
    }
}
