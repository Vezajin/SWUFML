package FlightBooking;

import java.util.Scanner;
import java.util.ArrayList;

/**
 * This is the class that scans Strings retrieved from the database. These 
 * Strings contain names (first name and last name) and seats per order. 
 * @author FrederikTH
 */
public class FlightScanner {
    
    public FlightScanner() {}
        
    /**
     * This method scans and breaks String input into smaller Strings and stores 
     * them in an Array. Only usable with seat-Strings.
     * @param input Is a String retrieved from the database. It contains all the 
     * seats in a given order (seats only). 
     * @return Returns an ArrayList comprising of Strings where each index equals 
     * a seat which is booked. 
     */
    public ArrayList<String> seatAnalyser(String input) {
        Scanner scan = new Scanner(input);
        ArrayList<String> seatScan = new ArrayList<String>();
        
            // Returns true if the String has a token. If false the String is empty. 
        while(scan.hasNext()==true) {
            //Scans the token and adds it to the arraylist.
            seatScan.add(scan.next());                       
            }    
        return seatScan;
    }
    
    /**
     * This method scans and breaks String input into smaller Strings and stores 
     * them in an Array. Only usable with name-Strings.
     * @param input Is a String retrieved from the database. It contains the 
     * all first names and last names in a given order. 
     * @return Returns an ArrayList comprising of Strings where each index equals 
     * a seat which is booked. 
     */   
    public ArrayList<String> nameAnalyser (String input) {
        Scanner scan = new Scanner(input);
        ArrayList<String> nameScan = new ArrayList<String>();        
            // Returns true if the String has a token. If false the String is empty.
        scan.useDelimiter(",");    
        while(scan.hasNext()==true) {               
                // Returns true if the String has a token. If false the String is empty.                
                nameScan.add(scan.next());               
            }
        return nameScan;
    }
    
    public ArrayList<String> destinationAnalyser (String input) {
        Scanner scan = new Scanner(input);
        ArrayList<String> desScan = new ArrayList<String>();  
            // Returns true if the String has a token. If false the String is empty.
        scan.useDelimiter("-");    
        while(scan.hasNext()==true) {               
                // Returns true if the String has a token. If false the String is empty.                
                desScan.add(scan.next());               
            }
        return desScan;
    }
 }   


