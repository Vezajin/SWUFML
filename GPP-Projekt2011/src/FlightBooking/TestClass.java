package FlightBooking;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;

/**
 * Write test related stuff in here GUYS!?!?
 * @author Nikolaj
 */
public class TestClass {
    
  
    
    public TestClass() {
    
    }
   
    public void testMakeOrder1() throws SQLException {
        try {
            Database db = new Database();
            Order o = new Order(4, 10, "1a, 2a", "mand1, mand2");
            o.insert(db);
        } catch (SQLException ex) {
            Logger.getLogger(TestClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void testMakeCustomer() throws SQLException {
        Database db = new Database();
        Customer c = new Customer("Michael", "Johansson", "Sweden", "Stockholm", "Testvejen", "59473625", "en@mail.se");
        c.insert(db);
    }
    
    public void testArrayStuff() throws SQLException {
        FlightSearch fs = new FlightSearch();
        Database db = new Database();
        fs.getSpecDateFlights(db, "2013", "01", "09");
    }
    
}

