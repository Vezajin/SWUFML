/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FlightBooking;
import java.sql.*;
import java.util.Calendar;
/**
 *
 * @author Nikolaj
 */
public class NewDate {

   private Date date;
   
   public NewDate(int year, int month, int day) {
       Calendar c = Calendar.getInstance();
       c.set(year, month, day);
       date = new Date(c.getTimeInMillis());
   }
   
   public Date getDate() {
       return date;
   }
}

