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
public interface DatabaseTable {
    public void init(ResultSet rs) throws SQLException;
    public void insert(Database db) throws SQLException;
    public void delete(Database db, int k) throws SQLException;
}
