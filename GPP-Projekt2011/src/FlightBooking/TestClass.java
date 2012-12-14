package FlightBooking;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;

/**
 * TestClass contains tests for various methods that were not intuitive to test
 * with JUnit.
 * @author Nikolaj
 */
public class TestClass {

    public TestClass() {

    }
    
    
    /** Testing that the method getSpecDateFlights finds the correct flights in the database.
     *  Verified manually by comparing system.out.println with database info.
     */
    public void testFlightSearch1() throws SQLException {
        FlightSearch fs = new FlightSearch();
        Database db = new Database();
        fs.getSpecDateFlights(db, "2013", "01", "09");
    }
    
    /** Testing that the method getSpecDateFlights finds the correct flights in the database.
     *  Verified manually by comparing system.out.println with database info.
     */
    public void testFlightSearch2() throws SQLException {
        FlightSearch fs = new FlightSearch();
        Database db = new Database();
        fs.getDateFlights(db, "2013", "01", "04", "2013", "01", "14");
    }
}