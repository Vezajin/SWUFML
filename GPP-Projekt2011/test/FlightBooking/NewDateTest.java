/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FlightBooking;

import java.sql.Date;
import java.util.Calendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author FrederikTH
 */
public class NewDateTest {

    private Date date;

    public NewDateTest() {   
    }
    
    @Before
    public void setUp() {
        
        
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of getDate method, of class NewDate.
     */
    @Test
    public void testWhatMonth() {
        System.out.println("whatMonth");
        int month = 3; //Marts
        
        NewDate instance = new NewDate(2012, month, 12);
        int expResult = Calendar.MARCH;
        int result = instance.whatMonth(month);
        assertEquals(expResult, result);
    }
    /**
     * Not needed since testing this class only tests whether whatMonth() has 
     * any effect on getDate which return the date Object in milliseconds. It
     * should have no effect on getDate().
     */
    @Test
    public void testGetDate() { 
    }
}
