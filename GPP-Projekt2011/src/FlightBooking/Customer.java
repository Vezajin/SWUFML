package FlightBooking;

import java.sql.*;

/**
 * Contains the information about a customer to be submitted into the database.
 * @author Lollike
 */
public class Customer implements DatabaseTable {
    private int key; // Primary id key in the database.
    private String firstname, lastname, country, city, address, 
            phonenumber, email; // Fields storing information to be stored in db.
    
    /**
     * Constructor that creates a customer object with the required info.
     */
    public Customer(String f, String l, String c, String ci, String a, String p, String e) {
        key = 0;
        firstname = f;
        lastname = l;
        country = c;
        city = ci;
        address = a;
        phonenumber = p;
        email = e;
    }
    
    /**
     * Initializes the ResultSet, making the link between the fields of this class,
     * and the columns in the database.
     */
    @Override
    public void init(ResultSet rs) throws SQLException {
        key = rs.getInt("id");
        firstname = rs.getString("firstname");
        lastname = rs.getString("lastname");
        country = rs.getString("country");
        city = rs.getString("city");
        address = rs.getString("address");
        phonenumber = rs.getString("phonenumber");
        email = rs.getString("email");
    }
    
    /** Constructor for creating a customer object, using data from a specified
     *  row in the database
     */
    public Customer(Database db, int k) throws SQLException {
        ResultSet rs = db.execute("SELECT * FROM Customer WHERE id = " + k);
        rs.next();
        init(rs);
    }
    
    /** The insert method inserts the data from the fields into the database.    
     */
    @Override
    public void insert(Database db) throws SQLException {
        db.execute ("INSERT INTO Customer (firstname, lastname, country, city, address, phonenumber, email) VALUES ('" +
            firstname + "', '" + lastname + "', '" + country + "', '" + city + "', '" + address + "', '" + phonenumber + 
                "', '" + email + "')");
        ResultSet rs = db.execute("SELECT MAX(id) as 'max' FROM Customer");
        rs.next();
        key = rs.getInt("max");
    }
    
    // Deletes the entry with the specified primary key in the database.
    @Override
    public void delete(Database db, int k) throws SQLException {
        db.execute("DELETE FROM Customer WHERE id = " + k);
    }
    
    // Returns the key.
    public int getKey() {
        return key;
    }
    
    // Returns firstname.
    public String getFirstname() {
        return firstname;
    }
    
    // Returns lastname.
    public String getLastname() {
        return lastname;
    }
    
    // Returns country.
    public String getCountry() {
        return country;
    }
    
    // Returns city.
    public String getCity() {
        return city;
    }
    
    // Returns address.
    public String getAddress() {
        return address; 
    }
    
    // Returns phonenumber.
    public String getPhonenumber() {
        return phonenumber;
    }
    
    // Returns email.
    public String getEmail() {
        return email;
    }
}
