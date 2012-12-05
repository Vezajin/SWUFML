
package FlightBooking;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;

public class Run {

   
    public static void main(String[] args) throws SQLException {
        new GUI();
        TestClass tc = new TestClass();
        tc.testCompareDates();
    }
}

