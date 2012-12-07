package FlightBooking;

import java.sql.*;


/**
 * This class handles the information involving flights to be submitted into
 * the database.
 * @author Lollike
 */
public class Flight {
    private int key, numberofseats, bookedseats;
    private String startdes, enddes, timestamp;
    private Date date;
    
    public Flight(String s, String e, Date d, int ns, String t) {
        key = 0;
        bookedseats = 0;
        startdes = s;
        enddes = e;
        date = d;
        numberofseats = ns;
        timestamp = t;
    }
    
    // Initializes the ResultSet
    public void init(ResultSet rs) throws SQLException {
        key = rs.getInt("id");
        startdes = rs.getString("startdestination");
        enddes = rs.getString("enddestination");
        date = rs.getDate("date");
        numberofseats = rs.getInt("numberofseats");
        bookedseats = rs.getInt("bookedseats");
        timestamp = rs.getString("timestamp");
    }
    
    public Flight(Database db, int k) throws SQLException {
        ResultSet rs = db.execute("SELECT * FROM Flights WHERE id = " + k);
        rs.next();
        init(rs);
    }
    
    // Inserts the information in the fields into correspondong columns in db.
    public void insert(Database db) throws SQLException {
        db.execute("INSERT INTO Flights (startdestination, enddestination, date, numberofseats, bookedseats, timestamp) VALUES ('" + startdes + "', '" + enddes + "', '" + date + "', '" + numberofseats + "', '" + bookedseats + "', '" + timestamp + "')");
    }
    
    // Deletes the entry with the specified primary key in the database.
    public void delete(Database db, int k) throws SQLException {
        db.execute("DELETE FROM Flights WHERE id = " + k);
    }
    

    
    // Returns primary key.
    public int getKey() {
        return key;
    }
    
    // Returns start destination ID.
    public String getStartDestination() {
        return startdes;
    }
    
    // Returns end destination ID.
    public String getEndDestination() {
        return enddes;
    }
    
    // Returns the date.
    public Date getDate() {
        return date;
    }
    
    public int getNumberOfSeats()
    {
        return numberofseats;
    }
    
    public int getBookedSeats() {
        return bookedseats;
    }
    
    public String timestamp() {
        return timestamp;
    }
    
    public static void insertManyFlights(String s, String e, int y, int m, int sd, int ed, String t, int ns) throws SQLException {
        Database db = new Database();
        for(int i = sd; i < ed; i=i+2) {
            Date d = new NewDate(y, m, i).getDate();
            Flight f = new Flight(s, e, d, ns, t);
            f.insert(db);
        }
        db.close();
    }    
}

