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
    public void testMakeOrder() throws SQLException {
        try {
            Database db = new Database();
            
            Customer customer = new Customer("Michael", "Johansson", "Sweden", "Stockholm", "Testvejen", "59473625", "en@mail.se");
            customer.insert(db);
            
            NewDate d = new NewDate(2012, "12", 9);
            Flight flight = new Flight(1, 2, d.getDate(), 100);
            flight.insert(db);
            
            Seat s = new Seat(3, 7);
            s.insert(db);
            
        } catch (SQLException ex) {
            Logger.getLogger(TestClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void testMakeOrder1() throws SQLException {
        try {
            Database db = new Database();
            Order o = new Order(4, 10, 5);
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
    
    public void testMakeFlight() throws SQLException {
        Database db = new Database();
        Flight f = new Flight(4, 4, new NewDate(2012, "12", 12).getDate(), 100);
        f.insert(db);
    }
}

