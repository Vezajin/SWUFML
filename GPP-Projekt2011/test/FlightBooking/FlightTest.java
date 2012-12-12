/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FlightBooking;

import java.sql.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Nikolaj
 */
public class FlightTest {
    
    private static Database db;
    private static Flight f1;
    private static Flight f2;
    private static Date d;
    
    public FlightTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws SQLException {
        db = new Database();
        d = (new NewDate(2013, 10, 1)).getDate();
        f1 = new Flight("Copenhagen", "London", d, 100, "14:00", 0);
        f2 = new Flight(db, 700);
    }
    
    @AfterClass
    public static void tearDownClass() throws SQLException {
        db.close();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of insert method, of class Flight.
     */
    @Test
    public void testInsert() throws Exception {
        f1.insert(db);
        ResultSet rs = db.execute("SELECT * FROM Flights");
        rs.last();
        assertEquals(rs.getString("startdestination"), "Copenhagen");
        assertEquals(rs.getString("enddestination"), "London");
        assertEquals(rs.getInt("numberofseats"), 100);
        assertEquals(rs.getString("timestamp"), "14:00");
    }

    /**
     * Test of delete method, of class Flight.
     */
    @Test
    public void testDelete() throws Exception {
        System.out.println("delete");
        ResultSet rs = db.execute("SELECT * FROM Flights");
        rs.last();
        int id = rs.getInt("id");
        rs.close();
        
        ResultSet rs2 = db.execute("SELECT COUNT(*) AS rowcount FROM Flights");
        rs2.next();
        int rc = rs2.getInt("rowcount");
        rs2.close();
        
        f1.delete(db, id);
        
        ResultSet rs3 = db.execute("SELECT COUNT(*) AS rowcount2 FROM Flights");
        rs3.next();
        int rc2 = rs3.getInt("rowcount2");

        assertEquals(rc-1, rc2);
    }

    /**
     * Test of getKey method, of class Flight.
     */
    @Test
    public void testGetKey() {
        System.out.println("getKey");
        assertEquals(f2.getKey(), 700);
    }

    /**
     * Test of getStartDestination method, of class Flight.
     */
    @Test
    public void testGetStartDestination() {
        System.out.println("getStartDestination");
        assertEquals(f2.getStartDestination(), "Copenhagen");
    }

    /**
     * Test of getEndDestination method, of class Flight.
     */
    @Test
    public void testGetEndDestination() {
        System.out.println("getEndDestination");
        assertEquals(f2.getEndDestination(), "Amsterdam");
    }

    /**
     * Test of getDate method, of class Flight.
     */
    @Test
    public void testGetDate() {
        System.out.println("getDate");
        assertEquals(f2.getDate(), (new NewDate(2013, 01, 25)).getDate());
    }
    
    /**
     * Test of getNumberOfSeats method, of class Flight.
     */
    @Test
    public void testGetNumberOfSeats() {
        System.out.println("getNumberOfSeats");
        assertEquals(f2.getNumberOfSeats(), 40);
    }

    /**
     * Test of getBookedSeats method, of class Flight.
     */
    @Test
    public void testGetBookedSeats() {
        System.out.println("getBookedSeats");
        assertEquals(f2.getBookedSeats(), 0);
    }

    /**
     * Test of timestamp method, of class Flight.
     */
    @Test
    public void testTimestamp() {
        System.out.println("timestamp");
        assertEquals(f2.timestamp(), "14:00");
    }
}