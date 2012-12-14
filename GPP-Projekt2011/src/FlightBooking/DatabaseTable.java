package FlightBooking;

import java.sql.*;

/**
 * This interface contains abstract methods that needs to be implemented in
 * all the database classes.
 * @author Nikolaj
 */
public interface DatabaseTable {
    public void init(ResultSet rs) throws SQLException;
    public void insert(Database db) throws SQLException;
    public void delete(Database db, int k) throws SQLException;
}
