
package FlightBooking;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import java.sql.*;
import java.util.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * This is the graphical user interface of the program. It's this class
 * that takes care of representing the data to the user.
 * @author Mark
 */
public class GUI {
    
    private JFrame frame;
    private JPanel contentPane;
    private BorderLayout layout;
    private JComboBox routeComboBox, timeComboBox, remainingSeatsComboBox;
    private JLabel remainingSeatsLabel;
    private Database database;
    private FlightScanner flightScanner;
    private GUI gui;
    
    
    public GUI() {
        try {
            database = new Database();
        } catch (SQLException ex) {
            System.out.println("Initialisation exception: " + ex);
        }
        gui = this;
        makeFrame();
        flightScanner = new FlightScanner();
    }
    
    
    /**
     * Creates the frame and calls makeMenuBar.
     */
    private void makeFrame() {
        frame = new JFrame("Flight Booking");       
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = (JPanel)frame.getContentPane();
        layout = new BorderLayout();
        contentPane.setLayout(layout);
        
        makeMenuBar(frame);
        pictureContent();
    }
    
    
    /**
     * Creates a menubar with submenus.
     * @param frame is the frame which the menubar is to be added to.
     */
    private void makeMenuBar(JFrame frame) {
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);
        
        JMenu menu;
        JMenuItem item;
        
        menu = new JMenu("Reservations");
        menubar.add(menu);
        
        item = new JMenuItem("Make Reservation");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { makeReservation(); }
            });
        menu.add(item);
                
        item = new JMenuItem("Edit or Delete Reservation");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { editReservation(); }
            });
        menu.add(item);
        
        item = new JMenuItem("TEST");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { chooseFlight("2013", "01", "09"); }
            });
        menu.add(item);
        
        menu = new JMenu("Flights");
        menubar.add(menu);
        
        item = new JMenuItem("Search for flights");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { chooseDate(); }
            });
        menu.add(item);
    }
    
    
    /**
     * Starts the process of booking seat(s) on a flight.
     */
    private void makeReservation() {
        chooseDate();
    }
    
    
    /*
     * This method is used for determining which date the customer wishes to depart.
     */
    private void chooseDate() {
        JPanel dateInput = new JPanel(new GridLayout(0,1));
        
        JTextField startYear = new JTextField();
        JTextField startMonth = new JTextField();
        JTextField startDay = new JTextField();

        dateInput.add(new JLabel("Please select date of departure."));
        dateInput.add(new JLabel("Year:"));
        dateInput.add(startYear);
        dateInput.add(new JLabel("Month:"));
        dateInput.add(startMonth);
        dateInput.add(new JLabel("Day:"));
        dateInput.add(startDay);
        JButton ConfirmDate = new JButton("Search for flights");
        dateInput.add(ConfirmDate);
        ConfirmDate.addActionListener(new ConfirmDateActionListener(startYear, startMonth, startDay));
        
        contentPane.removeAll();
        contentPane.add(dateInput, layout.WEST);
        
        frame.pack();
        frame.setSize(500, 300);
        frame.setResizable(false);
    }
    
    
    /*
     * From the given date, the flights are found and put into JComoboBoxes(dropdown menues). 
     * The 2nd JComboBox's content changes when a value is selected in the first JCombobox. This is
     * handled by our inner class JComboBoxActionListener.
     */
    private void chooseFlight(String year, String month, String day) {
        if(layout.getLayoutComponent(BorderLayout.EAST) != null) {
            contentPane.remove(layout.getLayoutComponent(BorderLayout.EAST));
        }
        
        FlightSearch flightsearcher = new FlightSearch();
        ArrayList<Flight> allFlightsOnDate = new ArrayList<Flight>();
        try {
            allFlightsOnDate = (flightsearcher.getSpecDateFlights(database, year, month, day));
            
        } catch (SQLException ex) {
            System.out.println("Error getting flights from server. Exception: " + ex);
        }
        ArrayList<Flight> flightsOnDate = new ArrayList<Flight>();
        //Here we make sure we only have the same route once.
        for(int i = 0; i<allFlightsOnDate.size(); i++) {
            flightsOnDate.add(allFlightsOnDate.get(i));
                for(int j = 0; j<flightsOnDate.size(); j++) {
                    // everytime j == i, it will be checking on itself, which would be stupid.
                    if(j != i) {
                        if(((flightsOnDate.get(i)).getStartDestination()).equals((allFlightsOnDate.get(j)).getStartDestination()) &&
                            ((flightsOnDate.get(i)).getEndDestination()).equals((allFlightsOnDate.get(j)).getEndDestination())) {
                            flightsOnDate.remove(i);
                        }
                    }
                }
        }
        flightsOnDate.trimToSize();
        //Creates a string array where the strings are startDes-endDes.
        String[] route = new String[flightsOnDate.size()+1];
        route[0] = "Nothing Selected";
        for(int i = 0; i <flightsOnDate.size(); i++) {
            route[i+1] = flightsOnDate.get(i).getStartDestination()+"-"+flightsOnDate.get(i).getEndDestination();
        }
        JPanel flightChoice = new JPanel(new GridLayout(0,1));
        
        flightChoice.add(new JLabel("Flights for the date: " +day+"-"+month+"-"+year));
        routeComboBox = new JComboBox(route);
              
        flightChoice.add(new JLabel("Where do you wish to go?"));
        flightChoice.add(routeComboBox);
        routeComboBox.addActionListener(new JComboBoxFlightActionListener(flightsOnDate));
        
        String[] noTimestamp = {"Nothing Selected"};
        timeComboBox = new JComboBox(noTimestamp);
          
        flightChoice.add(new JLabel("What time of the day?"));
        flightChoice.add(timeComboBox);
        
        String[] noSeats = {"Nothing Selected"};
        remainingSeatsLabel = new JLabel("No Flight Selected.");
        flightChoice.add(remainingSeatsLabel);
        
        JButton confirmFlight = new JButton("Confirm chosen flight");
        flightChoice.add(confirmFlight);
        confirmFlight.addActionListener(new ConfirmFlightActionListener(year,month,day,flightsOnDate));
                
        contentPane.add(flightChoice, layout.EAST);
        
        frame.pack();
        frame.setSize(500, 300);
        frame.setResizable(false);
    }
    
        
    /*
     * You create the traveller responsible for the tickets.
     */
    public void createCustomer(int numberOfSeats, String nameOfSeats, int flightID, String flight) {    
        
        int seatsRemaining = numberOfSeats;
        JTextField firstName = new JTextField();
        JTextField lastName = new JTextField();
        JTextField country = new JTextField();
        JTextField city = new JTextField();
        JTextField address = new JTextField();
        JTextField email = new JTextField();
        JTextField phoneNumber = new JTextField();
        JPanel cusInput = new JPanel(new GridLayout(0,1));
        
        cusInput.add(new JLabel("First name:"));
        cusInput.add(firstName);
        cusInput.add(new JLabel("Last name:"));
        cusInput.add(lastName);
        cusInput.add(new JLabel("Country:"));
        cusInput.add(country);
        cusInput.add(new JLabel("City:"));
        cusInput.add(city);
        cusInput.add(new JLabel("Street and house number:"));
        cusInput.add(address);
        cusInput.add(new JLabel("Email:"));
        cusInput.add(email);
        cusInput.add(new JLabel("Phone number"));
        cusInput.add(phoneNumber);
        
        JOptionPane inputDialog = new JOptionPane();
        int result = inputDialog.showConfirmDialog(frame, cusInput, "Customer Information", inputDialog.OK_CANCEL_OPTION);
        
        //Checks if the input is valid. If it isn't, error dialog appears and makes you try again.
        if(result == inputDialog.YES_OPTION) {
            
            if(firstName.getText() == "" || lastName.getText() == "" || 
               country.getText() == "" || city.getText() == "" || 
               address.getText() == "" || email.getText() == "" ||
               phoneNumber.getText() == "" || isIntNumber(phoneNumber.getText()) == false) {
                
                JOptionPane errorDialog = new JOptionPane();
                    errorDialog.showMessageDialog(frame, "Error! input was incorrect, try again.");
                    createCustomer(seatsRemaining, nameOfSeats, flightID, flight);
            }
            
            //If correct input, we proceed to creating additional customers if there's more than one customer.
            else {
                Customer customer = new Customer(firstName.getText(), lastName.getText(), country.getText(), city.getText(),
                        address.getText(), phoneNumber.getText(), email.getText());
                seatsRemaining--;
                //This string is used when saving the names of traveller(s) in the database.
                String traveller = firstName.getText() + " " + lastName.getText() + ", ";
                
                //If there's only one, we proceed directly to making the order.
                if(seatsRemaining == 0) {
                    makeOrder(traveller, customer, nameOfSeats, numberOfSeats, flightID, flight); 
                }
                //If there's more than one traveller, the remaining will be created in the next step.
                else{
                  createAdditionalCustomers(seatsRemaining, numberOfSeats, customer, traveller, nameOfSeats, flightID, flight);
                }
            }            
        }
        
        //If you choose cancel or just try to close the window a "are you sure"-box will appear.
        else {
            JOptionPane cancelDialog = new JOptionPane();
            int cancelResult = cancelDialog.showConfirmDialog(frame,"Are you sure you wish to quit?",
                                                              "Customer Information", inputDialog.OK_CANCEL_OPTION);
            if(cancelResult == cancelDialog.NO_OPTION) {
                createCustomer(numberOfSeats, nameOfSeats, flightID, flight);
            }
        }
    }
    
    
    /*
     * This method gets the names of the additional travellers if there are any. 
     * The names are added to the string travellerNames.
     * 
     */
    private void createAdditionalCustomers(int seatsRemaining, int numberOfSeats, Customer customer, String travellerNames, String nameOfSeats, int flightID, String flight) {
          
        JPanel cusInputWest = new JPanel(new GridLayout(0,1));
        JPanel cusInputEast = new JPanel(new GridLayout(0,1));
        
        //Creates as many textfields as there are remaining seats.
        JTextField[] additionalCustomerNames = new JTextField[seatsRemaining];
        for(int j = 0; j<seatsRemaining; j++) {
            
            JTextField name = new JTextField("Full name");
            additionalCustomerNames[j] = name;
            
            if(j%2 == 0) {
            cusInputWest.add(new JLabel("Traveller" + (j+1)+":"));
            cusInputWest.add(name);
            }
            else {
                cusInputEast.add(new JLabel("Traveller" + (j+1)+":"));
                cusInputEast.add(name);
            }
 
        }
        JPanel cusInput = new JPanel(new GridLayout(0,3));
        cusInput.add(cusInputWest);
        cusInput.add(new JPanel());
        cusInput.add(cusInputEast);
        
        JOptionPane inputDialog = new JOptionPane();
        int cusResult = inputDialog.showConfirmDialog(frame, cusInput, "Additional Travellers Names", inputDialog.OK_CANCEL_OPTION);
        if(cusResult == inputDialog.YES_OPTION) {
            
            for(int k = 0; k<additionalCustomerNames.length; k++) {
                if(additionalCustomerNames[k].getText() == "Full name") {
                    JOptionPane errorDialog = new JOptionPane();
                    errorDialog.showMessageDialog(null, "Error! all the inputs were not names!");
                    createAdditionalCustomers(seatsRemaining, numberOfSeats, customer, travellerNames, nameOfSeats, flightID, flight);
                }
            }
            
            //For each traveller, the traveller's name is added to the string from createCustomer.
            for(int k = 0; k<additionalCustomerNames.length; k++) {
               travellerNames = travellerNames+(additionalCustomerNames[k].getText())+", ";
            }
            makeOrder(travellerNames, customer, nameOfSeats, numberOfSeats, flightID, flight);
        }
    }
   
    
    private void makeOrder(String travellers, Customer customer, String nameOfSeats, int numberOfSeats, int flightID, String flight) {
                try {
                    customer.insert(database);
                    int customerID = customer.getKey();
                    
                    Order order = new Order(customerID, flightID, nameOfSeats, travellers);
                    order.insert(database);
                    
                    Flight bookedFlight = new Flight(database, flightID);
                    int bookedSeatsTotal = bookedFlight.getBookedSeats() + numberOfSeats;
                    database.execute("UPDATE Flights SET bookedseats = " + bookedSeatsTotal + " WHERE id = " + flightID);
                    
                    String customerName = customer.getFirstname() + " " + customer.getLastname();
                    String customerAddress = customer.getCountry() + ", " + customer.getCity() + ", " + customer.getAddress();
                    String customerPhone = customer.getPhonenumber();
                    
                    contentPane.removeAll();
                    JLabel labelID = new JLabel("Your Customer ID: " +customerID);
                    JLabel labelName = new JLabel("Person in charge of booking: " +customerName);
                    JLabel labelAddress = new JLabel("Address: " +customerAddress);
                    JLabel labelPhone = new JLabel("Phone number: " + customerPhone);
                    JLabel labelFlight = new JLabel("On flight: " + flightID + ", " + flight);
                    JLabel labelSeats = new JLabel("You have booked: " +numberOfSeats+ " seat(s)");
                    JLabel labelAllTravellers = new JLabel("Names of all travellers: " +travellers);
                    contentPane.add(labelID, BorderLayout.CENTER);
                    contentPane.add(labelName, BorderLayout.CENTER);
                    contentPane.add(labelAddress, BorderLayout.CENTER);
                    contentPane.add(labelPhone, BorderLayout.CENTER);
                    contentPane.add(labelFlight, BorderLayout.CENTER);
                    contentPane.add(labelSeats, BorderLayout.CENTER);
                    contentPane.add(labelAllTravellers, BorderLayout.CENTER);
        
                    frame.pack();
                    frame.setSize(500, 300);
                    frame.setResizable(false);
                    frame.setVisible(true);
                } 
                
                catch (SQLException ex) {
                    System.out.println("Saving order failed. Exception: " + ex);
                }
    }
    
    
    /**
     *
     */
    private void editReservation() {
        Object[] options = {"Edit Reservation", "Delete Reservation"};
        JTextField phoneNumber = new JTextField();
            JTextField cusID = new JTextField();
            JPanel cusInput = new JPanel(new GridLayout(0,1));
            
            cusInput.add(new JLabel("Customer phone number:"));
            cusInput.add(phoneNumber);
            cusInput.add(new JLabel("Customer ID:"));
            cusInput.add(cusID);
            
        int result = JOptionPane.showOptionDialog(frame, cusInput, 
                "Edit or Delete Reservation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
                null, options,options[0]);
        
        //Checks if the input is valid
        if(result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION) {
            if(isIntNumber(cusID.getText()) == true || isIntNumber(phoneNumber.getText()) == true) {
                try {                    
                    Order order = new Order(database, Integer.parseInt(cusID.getText()));
                    Flight flight = new Flight(database, order.getFlight());
                    Customer customer = new Customer(database, order.getCustomer());
                    
                    //Delete Option
                    if(result == JOptionPane.NO_OPTION) {
                        JOptionPane confirmDialog = new JOptionPane();
                        int confirmResult = confirmDialog.showConfirmDialog(gui.returnFrame(),"Are you sure you wish to delete your order?",
                                                            "Confirm your choice.", confirmDialog.OK_CANCEL_OPTION);
                        if(confirmResult == confirmDialog.YES_OPTION) {
                            FlightScanner flightscanner = new FlightScanner();
                            int numberOfSeats = (flightscanner.seatAnalyser(order.getSeat())).size();
                            int bookedSeatsTotal = flight.getBookedSeats() - numberOfSeats;
                            database.execute("UPDATE Flights SET bookedseats = " + bookedSeatsTotal + " WHERE id = " + order.getFlight());
                            customer.delete(database, customer.getKey());
                            order.delete(database, order.getKey());
                        }
                    }
                    //Edit Option
                    if(result == JOptionPane.YES_OPTION) {
                        FlightSeat flightseat = new FlightSeat(gui, database, flightScanner);
                        String thisFlight = flight.getStartDestination()+"-"+flight.getEndDestination();
                        flightseat.chooseSeats(flight.getKey(), flight.getNumberOfSeats(), thisFlight, 1, Integer.parseInt(cusID.getText())); 
                    }
                }
                catch (SQLException ex) {
                    System.out.println("finding order exception : " + ex);
                }
            
            }
            
            else {
                JOptionPane errorDialog = new JOptionPane();
                errorDialog.showMessageDialog(null, "Error! Incorrect input!");
                editReservation();
            }
        }
    }

    
    /**
     * Adds a picture to the contentpane.
     */
    private void pictureContent() {
        contentPane.removeAll();
        ImageIcon icon = new ImageIcon(getClass().getResource("fly.jpg"));
        JLabel pictureLabel = new JLabel(icon);
        contentPane.add(pictureLabel, BorderLayout.CENTER);
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setSize(500, 300);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    
     /**
     * Is used to check if a string is a integer. In our case this is used for checking
     * input data from textfields.
     * @param num is the string that is to be checked.
     */
     private boolean isIntNumber(String num){
        try{
            Integer.parseInt(num);
        } catch(Exception NumberFormatException) {
            return false;
        }
        return true;
     }
     
    public JFrame returnFrame() {
        return frame;
    }
     
     
     /**
      * When a flight is chosen, the route box will change to
      * display relevant flights. This is handled by our inner class 
      * JComboBoxFlightActionListener.
      */
     private class JComboBoxFlightActionListener implements ActionListener {
        
         ArrayList<Flight> flightsOnDate; 
         
        public JComboBoxFlightActionListener(ArrayList<Flight> flightsOnDate) {
        
            this.flightsOnDate = flightsOnDate;
        }
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> flightsDes = new ArrayList<String>();
            String selectedValue = routeComboBox.getSelectedItem().toString();
            ArrayList<String> timestamps = new ArrayList<String>();
            
            flightsDes = flightScanner.destinationAnalyser(selectedValue);
            for(int i = 0; i<flightsOnDate.size();i++) {
                if(flightsDes.get(0).equals((flightsOnDate.get(i)).getStartDestination()) && 
                    flightsDes.get(1).equals((flightsOnDate.get(i)).getEndDestination())) {
                    timestamps.add((flightsOnDate.get(i)).timestamp());
                }
            }
            String []timestampsArray = new String[timestamps.size()];
            for(int j = 0; j<timestamps.size(); j++) {
                timestampsArray[j] = timestamps.get(j);
            }
                    
            DefaultComboBoxModel model = (DefaultComboBoxModel) timeComboBox.getModel();      
            model.removeAllElements();
            
            for(String value : timestampsArray){
                model.addElement(value);
            }
            String selectedTime = timeComboBox.getSelectedItem().toString();
            
            for(int i = 0; i<flightsOnDate.size();i++) {
                if(selectedTime.equals(flightsOnDate.get(i).timestamp()) &&
                   flightsDes.get(0).equals((flightsOnDate.get(i)).getStartDestination()) && 
                   flightsDes.get(1).equals((flightsOnDate.get(i)).getEndDestination())) {
                    
                    int totalSeatsOnFlight = (flightsOnDate.get(i).getNumberOfSeats());
                    int bookedSeatsOnFlight = (flightsOnDate.get(i).getBookedSeats());
                    int seatsRemaining = totalSeatsOnFlight - bookedSeatsOnFlight;
                    remainingSeatsLabel.setText("Remaining seats on flight: "+ seatsRemaining);
                }
            }
        }
    }
     
     private class ConfirmDateActionListener implements ActionListener {
         JTextField startYear;
         JTextField startMonth;
         JTextField startDay;
         public ConfirmDateActionListener(JTextField startYear, JTextField startMonth, JTextField startDay) {
         this.startYear = startYear;
         this.startMonth = startMonth;
         this.startDay = startDay;
        }
        public void actionPerformed(ActionEvent e) {
            if(isIntNumber(startYear.getText()) == false || (startYear.getText()).length() != 4  ||
              isIntNumber(startMonth.getText()) == false || (startMonth.getText()).length() > 3 ||
              isIntNumber(startDay.getText()) == false ||(startDay.getText()).length() > 3) {
              
                JOptionPane errorDialog = new JOptionPane();
                errorDialog.showMessageDialog(null, "Error! input was not a date!");
                chooseDate();
           }
           else { 
               //turns the input into integers after we've checked if they are indeed integers.
               String startMonthTemp = new String(startMonth.getText());
               String startDayTemp = new String(startDay.getText());
               int year = Integer.parseInt(startYear.getText());
               int month = Integer.parseInt(startMonth.getText());
               int day = Integer.parseInt(startDay.getText());
               
               //A check for whenever the month and day inputs are written in, for instance, 02 or 2. 
               //If the first is the case, it's changed to 2.
               if((startMonth.getText()).contains("0") == true) {
                   startMonthTemp.substring((startMonth.getText()).length()-1);
                   month = Integer.parseInt(startMonthTemp);
               }
               if((startDay.getText()).contains("0") == true) {
                   startDayTemp.substring((startDay.getText()).length()-1);
                   day = Integer.parseInt(startDayTemp);
               }
               chooseFlight(startYear.getText(), startMonth.getText(), startDay.getText()); 
          }
        }
     }
     
     private class ConfirmFlightActionListener implements ActionListener {
         
         String year;
         String month;
         String day;
         ArrayList<Flight> flightsOnDate;

         public ConfirmFlightActionListener(String year, String month, String day, ArrayList<Flight> flightsOnDate) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.flightsOnDate = flightsOnDate;
         }
         
         public void actionPerformed(ActionEvent e) {
             if(routeComboBox.getSelectedItem() == "Nothing Selected") {
              
                JOptionPane errorDialog = new JOptionPane();
                errorDialog.showMessageDialog(null, "Error! Please choose a flight!");
                chooseFlight(year, month, day);
            }
            else {
                ArrayList<String> flightsDes = new ArrayList<String>();
                String selectedRoute = routeComboBox.getSelectedItem().toString();
                String selectedTime = timeComboBox.getSelectedItem().toString();
            
                flightsDes = flightScanner.destinationAnalyser(selectedRoute);
                for(int i = 0; i<flightsOnDate.size();i++) {
                    if(flightsDes.get(0).equals((flightsOnDate.get(i)).getStartDestination()) && 
                       flightsDes.get(1).equals((flightsOnDate.get(i)).getEndDestination()) &&
                       selectedTime.equals((flightsOnDate.get(i)).timestamp())) {
                       
                        String flight = selectedRoute + ", " + selectedTime;
                        FlightSeat flightseat = new FlightSeat(gui, database, flightScanner);
                        flightseat.chooseSeats(flightsOnDate.get(i).getKey(), flightsOnDate.get(i).getNumberOfSeats(),flight, 0, 0);
                    }
                }
            }
         }
     }
}