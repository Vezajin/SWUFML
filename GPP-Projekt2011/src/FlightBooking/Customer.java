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
public class Customer {
    private int key;
    private String firstname;
    private String lastname;
    private String country;
    private String city;
    private String address;
    private String phonenumber;
    private String email;
    
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
    
    public Customer(Database db, int k) throws SQLException {
        ResultSet rs = db.execute("SELECT * FROM Customer WHERE id = " + k);
        rs.next();
        init(rs);
    }
    
    public void insert(Database db) throws SQLException {
        db.execute ("INSERT INTO Customer (firstname, lastname, country, city, address, phonenumber, email) VALUES ('" +
            firstname + "', '" + lastname + "', '" + country + "', '" + city + "', '" + address + "', '" + phonenumber + 
                "', '" + email + "')");
        ResultSet rs = db.execute("SELECT MAX(id) as 'max' FROM People");
        rs.next();
        key = rs.getInt("max");
    }
    
    public int getKey() {
        return key;
    }
    
    public String getFirstname() {
        return firstname;
    }
    
    public String getLastname() {
        return lastname;
    }
    
    public String getCountry() {
        return country;
    }
    
    public String getCity() {
        return city;
    }
    
    public String getAddress() {
        return address; 
    }
    
    public String getPhonenumber() {
        return phonenumber;
    }
    
    public String getEmail() {
        return email;
    }
}
