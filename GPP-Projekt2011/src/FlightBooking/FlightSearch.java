/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FlightBooking;

import java.sql.*;

/**
 *
 * @author Lollike
 */
public class FlightSearch {
    Flight[] flightArray;
    
    public FlightSearch() {
        flightArray = new Flight[9];
    }
    
    // Selects the flights from the database for one specific date.
    public static void getSpecDateFlights(Database db, String year1, String month1, String day1) throws SQLException {
        ResultSet rs = db.execute("SELECT * FROM Flights WHERE date = '" + year1 +"-"+ month1 +"-"+ day1 + "'");
    }
    
    public void getNumberOfFlights(Database db, String year1, String month1, String day1, String year2, String month2, String day2) throws SQLException {
        ResultSet rs = db.execute("SELECT COUNT(date) AS count FROM Flights WHERE date between '" + year1 +"-"+ month1 +"-"+ day1 + "' AND '" + year2 +"-"+ month2 +"-"+ day2 +"'");
    }
    
    // Selects the flights from the database between two specified dates.
    public static void getDateFlights(Database db, String year1, String month1, String day1, String year2, String month2, String day2) throws SQLException {
        ResultSet rs = db.execute("SELECT * FROM Flights WHERE date between '" + year1 +"-"+ month1 +"-"+ day1 + "' AND '" + year2 +"-"+ month2 +"-"+ day2 +"'");
        while(rs.next()) {
            System.out.println("Start destination: " + rs.getString("startdestination") + " - End destination: " + rs.getString("enddestination") + " - Date: " + rs.getDate("date"));
            for(int i = 0; i < 100; i++) {
                new Flight(rs.getString("startdestination"), rs.getString("enddestination"), rs.getDate("date"), rs.getInt("numberofseats"), rs.getString("timestamp"));
            }
        }
    }
    
}
