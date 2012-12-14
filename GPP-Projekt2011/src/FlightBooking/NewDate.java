package FlightBooking;
import java.sql.*;
import java.util.Calendar;

/**
 * This class creates Date objects that does not increment.
 * In other words, this class makes sure, that the correct date is created.
 * @author Nikolaj
 */
public class NewDate {

   private Date date;
   
   // Creates a date object given the year, month and date in integers.
   public NewDate(int year, int month, int day) {
       Calendar c = Calendar.getInstance();
       c.set(year, whatMonth(month), day);
       date = new Date(c.getTimeInMillis());
   }
   
   // Specifies the month for corresponding integers.
   public int whatMonth(int month) {
        int i = 0;
        if(month == 1) {
            i = Calendar.JANUARY;
        }
        if(month == 2) {
            i = Calendar.FEBRUARY;
        }
        if(month == 3) {
            i = Calendar.MARCH;
        }
        if(month == 4) {
            i = Calendar.APRIL;
        }
        if(month == 5) {
            i = Calendar.MAY;
        }
        if(month == 6) {
            i = Calendar.JUNE;
        }
        if(month == 7) {
            i = Calendar.JULY;
        }
        if(month == 8) {
            i = Calendar.AUGUST;
        }
        if(month == 9) {
            i = Calendar.SEPTEMBER;
        }
        if(month == 10) {
            i = Calendar.OCTOBER;
        }
        if(month == 11) {
            i = Calendar.NOVEMBER;
        }
        if(month == 12) {
            i = Calendar.DECEMBER;
        }
        return i;
    }
   
   // Return the date.
   public Date getDate() {
       return date;
   }
}