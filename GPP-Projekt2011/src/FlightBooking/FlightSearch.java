/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FlightBooking;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Lollike
 */
public class FlightSearch {
        
    public FlightSearch() {
        
    }
    
    // Selects the flights from the database for one specific date.
    public ArrayList getSpecDateFlights(Database db, String year1, String month1, String day1) throws SQLException {
        ResultSet rs = db.execute("SELECT * FROM Flights WHERE date = '" + year1 +"-"+ month1 +"-"+ day1 + "'");
        ArrayList<Flight> resultArray = new ArrayList<Flight>();
        while(rs.next()) {
            System.out.println("Start destination: " + rs.getString("startdestination") + " - End destination: " + rs.getString("enddestination") + " - Date: " + rs.getDate("date"));
            Flight f = new Flight(rs.getInt("id"), rs.getString("startdestination"), rs.getString("enddestination"), rs.getDate("date"), rs.getInt("numberofseats"), rs.getString("timestamp"), rs.getInt("bookedseats"));
            resultArray.add(f);
        }
        int arraySize = resultArray.size();
        for(int i = 0; i < arraySize; i++) {
            System.out.println("Entry number "+ i);
            }
        return resultArray;
    }
    
    // Creates a resultset containing the number of flights between two dates.
    public void getNumberOfFlights(Database db, String year1, String month1, String day1, String year2, String month2, String day2) throws SQLException {
        ResultSet rs = db.execute("SELECT COUNT(date) AS count FROM Flights WHERE date between '" + year1 +"-"+ month1 +"-"+ day1 + "' AND '" + year2 +"-"+ month2 +"-"+ day2 +"'");
    }
    
    // Selects the flights from the database between two specified dates.
    public ArrayList getDateFlights(Database db, String year1, String month1, String day1, String year2, String month2, String day2) throws SQLException {
        ResultSet rs = db.execute("SELECT * FROM Flights WHERE date between '" + year1 +"-"+ month1 +"-"+ day1 + "' AND '" + year2 +"-"+ month2 +"-"+ day2 +"'");
        ArrayList<Flight> resultArray = new ArrayList<Flight>();
        while(rs.next()) {
            System.out.println("Start destination: " + rs.getString("startdestination") + " - End destination: " + rs.getString("enddestination") + " - Date: " + rs.getDate("date"));
            Flight f = new Flight(rs.getInt("id"), rs.getString("startdestination"), rs.getString("enddestination"), rs.getDate("date"), rs.getInt("numberofseats"), rs.getString("timestamp"), rs.getInt("bookedseats"));
            resultArray.add(f);
        }
        return resultArray;
    }    
}