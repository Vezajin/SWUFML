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
       c.set(year, whatMonth(month), day);
       date = new Date(c.getTimeInMillis());
   }
   
   private int whatMonth(int month) {
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
   
   public Date getDate() {
       return date;
   }
}

