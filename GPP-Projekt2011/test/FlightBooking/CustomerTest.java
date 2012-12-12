/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FlightBooking;

import java.sql.ResultSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.*;

/**
 *
 * @author Nikolaj
 */
public class CustomerTest {
    
    private static Database db;
    private static Customer c1;
    private static Customer c2;
    
    public CustomerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws SQLException {
        db = new Database();
        c1 = new Customer("Tom", "Johansen", "Denmark", "Copenhagen", "Nørrebrogade 146", "88888887", "test@test.dk");
        c2 = new Customer(db, 2);
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
     * Test of insert method, of class Customer.
     */
    @Test
    public void testInsert() throws SQLException {
        c1.insert(db);
        ResultSet rs = db.execute("SELECT * FROM Customer");
        rs.last();
        assertEquals(rs.getString("firstname"), "Tom");
        assertEquals(rs.getString("lastname"), "Johansen");
        assertEquals(rs.getString("country"), "Denmark");
        assertEquals(rs.getString("city"), "Copenhagen");
    }

    /**
     * Test of delete method, of class Customer.
     * This method deletes the last row of the Customer table in the database, 
     * while counting the number of total rows before and after deletion.
     * The number of rows before and after are compared, and a successful
     * deletion should make the number of rows after deletion one less.
     */
    
    @Test
    public void testDelete() throws SQLException {
        System.out.println("delete");
        ResultSet rs = db.execute("SELECT * FROM Customer");
        rs.last();
        int id = rs.getInt("id");
        rs.close();
        
        ResultSet rs2 = db.execute("SELECT COUNT(*) AS rowcount FROM Customer");
        rs2.next();
        int rc = rs2.getInt("rowcount");
        rs2.close();
        
        c1.delete(db, id);
        
        ResultSet rs3 = db.execute("SELECT COUNT(*) AS rowcount2 FROM Customer");
        rs3.next();
        int rc2 = rs3.getInt("rowcount2");

        assertEquals(rc-1, rc2);
    }

    /**
     * Test of getKey method, of class Customer.
     */
    @Test
    public void testGetKey() {
        System.out.println("getKey");
        assertEquals(c2.getKey(), 2);
    }

    /**
     * Test of getFirstname method, of class Customer.
     */
    @Test
    public void testGetFirstname() {
        System.out.println("getFirstname");
        assertEquals(c1.getFirstname(), "Tom");
    }

    /**
     * Test of getLastname method, of class Customer.
     */
    @Test
    public void testGetLastname() {
        System.out.println("getLastname");
        assertEquals(c1.getLastname(), "Johansen");
    }

    /**
     * Test of getCountry method, of class Customer.
     */
    @Test
    public void testGetCountry() {
        System.out.println("getCountry");
        assertEquals(c1.getCountry(), "Denmark");
    }

    /**
     * Test of getCity method, of class Customer.
     */
    @Test
    public void testGetCity() {
        System.out.println("getCity");
        assertEquals(c1.getCity(), "Copenhagen");
    }

    /**
     * Test of getAddress method, of class Customer.
     */
    @Test
    public void testGetAddress() {
        System.out.println("getAddress");
        assertEquals(c1.getAddress(), "Nørrebrogade 146");
    }

    /**
     * Test of getPhonenumber method, of class Customer.
     */
    @Test
    public void testGetPhonenumber() {
        System.out.println("getPhonenumber");
        assertEquals(c1.getPhonenumber(), "88888887");
    }

    /**
     * Test of getEmail method, of class Customer.
     */
    @Test
    public void testGetEmail() {
        System.out.println("getEmail");
        assertEquals(c1.getEmail(), "test@test.dk");
    }
}
