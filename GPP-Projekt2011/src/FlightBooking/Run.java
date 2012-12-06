
package FlightBooking;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;

public class Run {

   
    public static void main(String[] args) throws SQLException {
        new GUI();
        FlightSearch fs = new FlightSearch();
        Database db = new Database();
        fs.getNumberOfFlights(db, "2013", "01", "08", "2013", "01", "14");
    }
}

