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
public class OrderTest {
    
    private static Database db;
    private static Order o1;
    private static Order o2;
    
    public OrderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws SQLException {
        db = new Database();
        o1 = new Order(40, 702, "a1 a2", "Frederik Hoffmann, Mark Klitgaard");
        o2 = new Order(db, 14);
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
     * Test of insert method, of class Order.
     */
    @Test
    public void testInsert() throws SQLException {
        System.out.println("insert");
        o1.insert(db);
        ResultSet rs = db.execute("SELECT * FROM Orders");
        rs.last();
        assertEquals(rs.getInt("customerid"), 40);
        assertEquals(rs.getInt("flightid"), 702);
        assertEquals(rs.getString("seatstring"), "a1 a2");
        assertEquals(rs.getString("namestring"), "Frederik Hoffmann, Mark Klitgaard");
    }

    /**
     * Test of delete method, of class Order.
     */
    @Test
    public void testDelete() throws SQLException {
        System.out.println("delete");
        ResultSet rs = db.execute("SELECT * FROM Orders");
        rs.last();
        int id = rs.getInt("id");
        rs.close();
        
        ResultSet rs2 = db.execute("SELECT COUNT(*) AS rowcount FROM Orders");
        rs2.next();
        int rc = rs2.getInt("rowcount");
        rs2.close();
        
        o1.delete(db, id);
        
        ResultSet rs3 = db.execute("SELECT COUNT(*) AS rowcount2 FROM Orders");
        rs3.next();
        int rc2 = rs3.getInt("rowcount2");

        assertEquals(rc-1, rc2);
    }

    /**
     * Test of getCustomer method, of class Order.
     */
    @Test
    public void testGetCustomer() {
        System.out.println("getCustomer");
        assertEquals(o2.getCustomer(), 14);
    }

    /**
     * Test of getFlight method, of class Order.
     */
    @Test
    public void testGetFlight() {
        System.out.println("getFlight");
        assertEquals(o2.getFlight(), 2);
    }

    /**
     * Test of getSeat method, of class Order.
     */
    @Test
    public void testGetSeat() {
        System.out.println("getSeat");
        assertEquals(o2.getSeat(), "6a 6b 6c ");
    }

    /**
     * Test of getName method, of class Order.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        assertEquals(o2.getName(), "Nikolaj Lollike, Peder Mingo, Palle Balle Torp, ");
    }

    /**
     * Test of getKey method, of class Order.
     */
    @Test
    public void testGetKey() {
        System.out.println("getKey");
        assertEquals(o2.getKey(), 7);
    }
}
