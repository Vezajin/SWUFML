/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FlightBooking;

import java.util.ArrayList;
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
public class FlightSearchTest {
    
    public FlightSearchTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getSpecDateFlights method, of class FlightSearch.
     */
    @Test
    public void testGetSpecDateFlights() throws Exception {
        System.out.println("getSpecDateFlights");
        Database db = null;
        String year1 = "";
        String month1 = "";
        String day1 = "";
        FlightSearch instance = new FlightSearch();
        ArrayList expResult = null;
        ArrayList result = instance.getSpecDateFlights(db, year1, month1, day1);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNumberOfFlights method, of class FlightSearch.
     */
    @Test
    public void testGetNumberOfFlights() throws Exception {
        System.out.println("getNumberOfFlights");
        Database db = null;
        String year1 = "";
        String month1 = "";
        String day1 = "";
        String year2 = "";
        String month2 = "";
        String day2 = "";
        FlightSearch instance = new FlightSearch();
        instance.getNumberOfFlights(db, year1, month1, day1, year2, month2, day2);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDateFlights method, of class FlightSearch.
     */
    @Test
    public void testGetDateFlights() throws Exception {
        System.out.println("getDateFlights");
        Database db = null;
        String year1 = "";
        String month1 = "";
        String day1 = "";
        String year2 = "";
        String month2 = "";
        String day2 = "";
        FlightSearch instance = new FlightSearch();
        ArrayList expResult = null;
        ArrayList result = instance.getDateFlights(db, year1, month1, day1, year2, month2, day2);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
