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
 * @author FrederikTH
 */
public class FlightScannerTest {
    
    private     ArrayList<String> arrayTest = new ArrayList<String>();

    
    public FlightScannerTest() {
    }
    
    @After
    public void tearDown() {
        arrayTest.clear(); 
    }

    /**
     * Test of seatAnalyser method, of class FlightScanner.
     */
    @Test
    public void testSeatAnalyser() {
        arrayTest.add("1a"); 
        arrayTest.add("1b");
        arrayTest.add("1c");
        arrayTest.add("1d");
        
        
        System.out.println("seatAnalyser");
        String input = "1a 1b 1c 1d";
        FlightScanner instance = new FlightScanner();
        ArrayList expResult = arrayTest;
        ArrayList result = instance.seatAnalyser(input);
        assertEquals(expResult, result);
       
    }

    /**
     * Test of nameAnalyser method, of class FlightScanner.
     */
    @Test
    public void testNameAnalyser() {
        arrayTest.add("Nikolaj Lollike"); 
        arrayTest.add(" Mark Klitgaard");
        arrayTest.add(" Frederik Hoffmann");
       
       
        
        System.out.println("nameAnalyser");
        String input = "Nikolaj Lollike, Mark Klitgaard, Frederik Hoffmann";     
        FlightScanner instance = new FlightScanner();
        ArrayList expResult = arrayTest;
        ArrayList result = instance.nameAnalyser(input);
        assertEquals(expResult, result);
    }

    /**
     * Test of destinationAnalyser method, of class FlightScanner.
     */
    @Test
    public void testDestinationAnalyser() {
        arrayTest.add("Oslo"); 
        arrayTest.add("Copenhagen");
        
        
        System.out.println("destinationAnalyser");
        String input = "Oslo-Copenhagen";
        FlightScanner instance = new FlightScanner();
        ArrayList expResult = arrayTest;
        ArrayList result = instance.destinationAnalyser(input);
        assertEquals(expResult, result);
        
                
    }
}
