
package FlightBooking;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import java.sql.*;
import java.util.*;
import java.util.ArrayList;



/**
 * This is the graphical user interface of the program. It's this class
 * that takes care of representing the data to the user.
 * @author Mark
 */
public class GUI {
    
    private JFrame frame;
    private JPanel contentPane;
    private BorderLayout layout;
    private JComboBox routeComboBox, timeComboBox;
    private JComboBoxTimeAC timeAC;
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
        
        item = new JMenuItem("Make reservations");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { chooseDate(); }
            });
        menu.add(item);
        
        item = new JMenuItem("See flights");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { choosePeriod(); }
            });
        menu.add(item);
                
        item = new JMenuItem("Edit or delete reservation");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { editReservation(); }
            });
        menu.add(item);
    }
    

    /*
     * This method takes the input that is used for searching for flights.
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
        // ConfirmDateActionListener checks whenever the input is a valid date input or not. If so, it sends the input
        // to onwards to chooseFlight.
        ConfirmDate.addActionListener(new ConfirmDateActionListener(startYear, startMonth, startDay));
        
        contentPane.removeAll();
        contentPane.add(dateInput, layout.WEST);
        
        frame.pack();
        frame.setSize(500, 300);
        frame.setResizable(false);
    }
    
    /*
     * Is used when searching for flights in a period. Takes a total of 6 inputs that is made into two strings.
     */
    private void choosePeriod() {
        JPanel dateInput = new JPanel(new GridLayout(1,1));
        JPanel startDateInput = new JPanel(new GridLayout(0,1));
        JPanel endDateInput = new JPanel(new GridLayout(0,1));
        
        JTextField startYear = new JTextField();
        JTextField startMonth = new JTextField();
        JTextField startDay = new JTextField();
        JTextField endYear = new JTextField();
        JTextField endMonth = new JTextField();
        JTextField endDay = new JTextField();

        startDateInput.add(new JLabel("Please select start date"));
        startDateInput.add(new JLabel("Year:"));
        startDateInput.add(startYear);
        startDateInput.add(new JLabel("Month:"));
        startDateInput.add(startMonth);
        startDateInput.add(new JLabel("Day:"));
        startDateInput.add(startDay);
        
        endDateInput.add(new JLabel("Please select end date"));
        endDateInput.add(new JLabel("Year:"));
        endDateInput.add(endYear);
        endDateInput.add(new JLabel("Month:"));
        endDateInput.add(endMonth);
        endDateInput.add(new JLabel("Day:"));
        endDateInput.add(endDay);
        JButton panelFill = new JButton();
        startDateInput.add(panelFill);
        panelFill.setEnabled(false);
        
        JButton ConfirmPeriod = new JButton("Search for flights");
        endDateInput.add(ConfirmPeriod);
        // ConfirmDatePeriodListener checks whenever the inputs are valid dates input or not. If so, it sends the inputs
        // to onwards to chooseFlightInPeriod.
        ConfirmPeriod.addActionListener(new ConfirmPeriodActionListener(startYear, startMonth, startDay, endYear, endMonth, endDay));
        
        dateInput.add(startDateInput);
        dateInput.add(endDateInput);
        contentPane.removeAll();
        contentPane.add(dateInput, layout.WEST);
        
        frame.pack();
        frame.setSize(500, 300);
        frame.setResizable(false);
    }
    
    
    /*
     * From the given date, the flights are found and put into JComoboBoxes(dropdown menues). 
     * The 2nd JComboBox's content changes when a value is selected in the first JCombobox. This is
     * handled by our inner class JComboBoxFlightActionListener.
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
            Flight flightToCheck = allFlightsOnDate.get(i);
            if(i == 0) {
                flightsOnDate.add(flightToCheck);
            }
            if(i>0) {
                int k = 0;
                for(int j = 0; j<flightsOnDate.size(); j++) {
                    Flight existingFlight = flightsOnDate.get(j);
                    if(existingFlight.getStartDestination().equals(flightToCheck.getStartDestination()) == true &&
                        existingFlight.getEndDestination().equals(flightToCheck.getEndDestination()) == true) {
                        k++;
                    }
                }
                if(k == 0) {
                    flightsOnDate.add(flightToCheck);
                }
            }
        }

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
        routeComboBox.addActionListener(new JComboBoxFlightActionListener(flightsOnDate, year, month, day));
        
        String[] noTimestamp = {"Nothing Selected"};
        timeComboBox = new JComboBox(noTimestamp);
          
        flightChoice.add(new JLabel("What time of the day?"));
        flightChoice.add(timeComboBox);
        
        String[] noSeats = {"Nothing Selected"};
        remainingSeatsLabel = new JLabel("No Flight Selected.");
        flightChoice.add(remainingSeatsLabel);
        
        JButton confirmFlight = new JButton("Confirm chosen flight");
        flightChoice.add(confirmFlight);
        //The ConfirmFlightActionListener takes the chosen flight, and finds the correct Database entry that matches the chosen
        // flight, and then starts the booking of seats process on that flight.
        confirmFlight.addActionListener(new ConfirmFlightActionListener(year,month,day,flightsOnDate));
                
        contentPane.add(flightChoice, layout.EAST);
        
        frame.pack();
        frame.setSize(500, 300);
        frame.setResizable(false);
    }
    
    /*
     * From the given dates, the flights are found and put into JComoboBoxes(dropdown menues). 
     * The 2nd JComboBox's time changes when a value is selected in the first JCombobox. This is
     * handled by our inner class JComboBoxPeriodFlightActionListener.
     */
    private void chooseFlightInPeriod(String startYear, String startMonth, String startDay,
                              String endYear, String endMonth, String endDay) {
        if(layout.getLayoutComponent(BorderLayout.EAST) != null) {
            contentPane.remove(layout.getLayoutComponent(BorderLayout.EAST));
        }
        
        FlightSearch flightsearcher = new FlightSearch();
        ArrayList<Flight> allFlightsOnDates = new ArrayList<Flight>();
        try {
            allFlightsOnDates = (flightsearcher.getDateFlights(database, startYear, startMonth, startDay,
                                                                        endYear, endMonth, endDay));
            
        } catch (SQLException ex) {
            System.out.println("Error getting flights from server. Exception: " + ex);
        }
        ArrayList<Flight> flightsOnDate = new ArrayList<Flight>();
        //Here we make sure we only have the same route once.
        for(int i = 0; i<allFlightsOnDates.size(); i++) {
            Flight flightToCheck = allFlightsOnDates.get(i);
            if(i == 0) {
                flightsOnDate.add(flightToCheck);
            }
            if(i>0) {
                int k = 0;
                for(int j = 0; j<flightsOnDate.size(); j++) {
                    Flight existingFlight = flightsOnDate.get(j);
                    if(existingFlight.getStartDestination().equals(flightToCheck.getStartDestination()) == true &&
                        existingFlight.getEndDestination().equals(flightToCheck.getEndDestination()) == true) {
                        k++;
                    }
                }
                if(k == 0) {
                    flightsOnDate.add(flightToCheck);
                }
            }
        }

        //Creates a string array where the strings are startDes-endDes.
        String[] route = new String[flightsOnDate.size()+1];
        route[0] = "Nothing Selected";
        for(int i = 0; i <flightsOnDate.size(); i++) {
            route[i+1] = flightsOnDate.get(i).getStartDestination()+"-"+flightsOnDate.get(i).getEndDestination();
        }
        JPanel flightChoice = new JPanel(new GridLayout(0,1));
        
        flightChoice.add(new JLabel("Flights from: " +startDay+"-"+startMonth+"-"+startYear +" to: "
                                    +endDay+"-"+endMonth+"-"+endYear));
        routeComboBox = new JComboBox(route);
              
        flightChoice.add(new JLabel("Where do you wish to go?"));
        flightChoice.add(routeComboBox);
        //We send all flights in the period to our ActionListener, because even though
        //we only want to show the route once, we still wish all the timestamps for that route.
        //The ActionListener updates the timeComboBox with the correct timestamps.
        routeComboBox.addActionListener(new JComboBoxChosenPeriodFlightActionListener(allFlightsOnDates, startYear, startMonth, startDay,
                                                                                      endYear, endMonth, endDay));
        
        String[] noTimestamp = {"Nothing Selected"};
        timeComboBox = new JComboBox(noTimestamp);
          
        flightChoice.add(new JLabel("What date?"));
        flightChoice.add(timeComboBox);
 
        String noSeats = ("Nothing Selected");
        remainingSeatsLabel = new JLabel(noSeats);
        flightChoice.add(remainingSeatsLabel);
                
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
        //Adds the potentially two rows or name textfields to the window, and adds an empty panel for spacing.
        JPanel cusInput = new JPanel(new GridLayout(0,3));
        cusInput.add(cusInputWest);
        cusInput.add(new JPanel());
        cusInput.add(cusInputEast);
        
        //Makes the window with the textfields and OK and CANCEL buttons.
        JOptionPane inputDialog = new JOptionPane();
        int cusResult = inputDialog.showConfirmDialog(frame, cusInput, "Additional Travellers Names", inputDialog.OK_CANCEL_OPTION);
        if(cusResult == inputDialog.YES_OPTION) {
            
            //If any of the textfields havent been filled out properly, you will receive an error
            //bceAUSE anyError will be over 0.
            int anyErrors = 0;
            for(int k = 0; k<additionalCustomerNames.length; k++) {
                if(additionalCustomerNames[k].getText().equals("Full name") == true || additionalCustomerNames[k].getText().equals(null) == true || additionalCustomerNames[k].getText().equals("") == true) {
                anyErrors++;
                }
            }
            if(anyErrors > 0) {
                JOptionPane errorDialog = new JOptionPane();
                errorDialog.showMessageDialog(null, "Error! all the inputs were not names!");
                createAdditionalCustomers(seatsRemaining, numberOfSeats, customer, travellerNames, nameOfSeats, flightID, flight);
                anyErrors = 0;   
            }
            
            //For each traveller, the traveller's name is added to the string from createCustomer.
            for(int k = 0; k<additionalCustomerNames.length; k++) {
               travellerNames = travellerNames+(additionalCustomerNames[k].getText())+", ";
            }
            makeOrder(travellerNames, customer, nameOfSeats, numberOfSeats, flightID, flight);
        }
    }
   
    /*
     * Creates the order in the database and updates the different rows in the tables in the database.
     * This method also prints out the "ticket" in the main window.
     */
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
                    JLabel labelAllTravellers = new JLabel("Names of all travellers:");
                    JPanel ticketPrint = new JPanel(new GridLayout(0,1));
                    ticketPrint.add(labelID);
                    ticketPrint.add(labelName);
                    ticketPrint.add(labelAddress);
                    ticketPrint.add(labelPhone);
                    ticketPrint.add(labelFlight);
                    ticketPrint.add(labelSeats);
                    ticketPrint.add(labelAllTravellers);
                    
                    FlightScanner flightscanner = new FlightScanner();
                    ArrayList<String> travellerNames = flightscanner.nameAnalyser(travellers);
                    
                    for(int i = 0; i<travellerNames.size(); i++) {
                        ticketPrint.add(new JLabel(travellerNames.get(i)));
                    }
                    contentPane.add(ticketPrint);
        
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
     * You input customerID and phone number and then the order is chosen in the DB.
     * phonenumber is not actually used, except it is checked whenever or not it is indeed a number.
     * If you choose to delete your order, this is also done here, and the databse is updated.
     * Otherwise the chooseSeats is called and methodChecker is set to 1 instead of 0, so the method behaves
     * differently, and allows the option to do whatever the task you chose after inputting number and ID.
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
         String year;
         String month;
         String day;
         
        public JComboBoxFlightActionListener(ArrayList<Flight> flightsOnDate, String year, String month, String day) {
        
            this.flightsOnDate = flightsOnDate;
            this.year = year;
            this.month = month;
            this.day = day;
        }
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> flightsDes = new ArrayList<String>();
            String selectedValue = routeComboBox.getSelectedItem().toString();
            ArrayList<String> timestamps = new ArrayList<String>();
            if(selectedValue.equals("Nothing Selected") == false) {
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
            //Empties timeComboBox and adds timestamps to it instead.        
            DefaultComboBoxModel model = (DefaultComboBoxModel) timeComboBox.getModel();      
            model.removeAllElements();
            
            for(String value : timestampsArray){
                model.addElement(value);
            }
            String selectedTime = timeComboBox.getSelectedItem().toString();
            
            //Finds the flight that matches the route and timestamp.
            for(int i = 0; i<flightsOnDate.size();i++) {
                if(selectedTime.equals(flightsOnDate.get(i).timestamp()) &&
                   flightsDes.get(0).equals((flightsOnDate.get(i)).getStartDestination()) && 
                   flightsDes.get(1).equals((flightsOnDate.get(i)).getEndDestination())) {
                    
                    int totalSeatsOnFlight = (flightsOnDate.get(i).getNumberOfSeats());
                    int bookedSeatsOnFlight = (flightsOnDate.get(i).getBookedSeats());
                    int seatsRemaining = totalSeatsOnFlight - bookedSeatsOnFlight;
                    //Updates the label to show how many seats there remain on the flight.
                    remainingSeatsLabel.setText("Remaining seats on flight: "+ seatsRemaining);
                }
            }
            }
            //To avoid nullpointerexception, the choosFlight method is called again if you choose "nothing selected"
            //again after having chosen a flight already.
            else {
                chooseFlight(year,month,day);
            }
        }
    }
     
     //Checks if the date is actually a date, and calls chooseFlight.
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
     
     /*
      * Checks the chosen flight and sends it to FlightSeat so seats on the plane can be booked.
      */
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
             if(routeComboBox.getSelectedItem().equals("Nothing Selected") == true) {
              
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
                       
                        String flight = selectedRoute + ", " + flightsOnDate.get(i).getDate() + ", " + selectedTime;
                        FlightSeat flightseat = new FlightSeat(gui, database, flightScanner);
                        flightseat.chooseSeats(flightsOnDate.get(i).getKey(), flightsOnDate.get(i).getNumberOfSeats(),flight, 0, 0);
                    }
                }
            }
         }
     }
     
     /*
      * Updates the route comboBox with appropiate routes. If you have already searched once before, so there's
      * an ActionListener on the timeComboBox, this is removed to avoid errors. In the end, an actionListener is added
      * to the timeComboBox.
      */
     private class JComboBoxChosenPeriodFlightActionListener implements ActionListener {
        
         ArrayList<Flight> flightsOnDate; 
         String startYear;
         String startMonth;
         String startDay;
         String endYear;
         String endMonth;
         String endDay;
         
        public JComboBoxChosenPeriodFlightActionListener(ArrayList<Flight> flightsOnDate, String startYear, String startMonth, String startDay,
                                                         String endYear, String endMonth, String endDay) {
        
            this.flightsOnDate = flightsOnDate;
            this.startYear = startYear;
            this.startMonth = startMonth;
            this.startDay = startDay;
            this.endYear = endYear;
            this.endMonth = endMonth;
            this.endDay = endDay;
        }
        public void actionPerformed(ActionEvent e) {
            if(timeComboBox.getActionListeners().length == 1) {
                timeComboBox.removeActionListener(timeAC);
            }
            ArrayList<String> flightsDes = new ArrayList<String>();
            String selectedValue = routeComboBox.getSelectedItem().toString();
            ArrayList<String> timestamps = new ArrayList<String>();
            if(selectedValue.equals("Nothing Selected") == false) {
            flightsDes = flightScanner.destinationAnalyser(selectedValue);
            for(int i = 0; i<flightsOnDate.size();i++) {
                if(flightsDes.get(0).equals((flightsOnDate.get(i)).getStartDestination()) && 
                    flightsDes.get(1).equals((flightsOnDate.get(i)).getEndDestination())) {
                    timestamps.add((flightsOnDate.get(i).getDate())+","+(flightsOnDate.get(i)).timestamp());
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
            timeAC = new JComboBoxTimeAC(flightsOnDate);
            timeComboBox.addActionListener(timeAC);
            }
            else {
                chooseFlightInPeriod(startYear,startMonth,startDay,endYear,endMonth,endDay);
            }
        }
    }
     /*
      * Updates the JLabel with remaining seats on flight according to which specific flight that is chosen.
      */
     private class JComboBoxTimeAC implements ActionListener {
         
         ArrayList<Flight> flightsOnDate;
         
         public JComboBoxTimeAC(ArrayList<Flight> flightsOnDate) {
             this.flightsOnDate = flightsOnDate;
         }
         
         public void actionPerformed(ActionEvent e) {
             ArrayList<String> flightsDes = new ArrayList<String>();
            String selectedValue = routeComboBox.getSelectedItem().toString();
            if(selectedValue.equals("Nothing Selected") == false) {
            flightsDes = flightScanner.destinationAnalyser(selectedValue);
                
                FlightScanner flightScanner = new FlightScanner();
                ArrayList<String> timeAndDate = flightScanner.nameAnalyser(timeComboBox.getSelectedItem().toString());
                String selectedTime = timeAndDate.get(1);
                String selectedDate = timeAndDate.get(0);
                
                for(int i = 0; i<flightsOnDate.size();i++) {
                    String dateOfFlight = (flightsOnDate.get(i).getDate()+"");
                    if(selectedTime.equals(flightsOnDate.get(i).timestamp()) &&
                       selectedDate.equals(dateOfFlight) &&
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
    }
     
     /* 
      * Does pretty much the same as ConfirmDate, except there's one more date here and calls chooseFlightInPeriod
      * instead.
      */
     private class ConfirmPeriodActionListener implements ActionListener {
         JTextField startYear;
         JTextField startMonth;
         JTextField startDay;
         JTextField endYear;
         JTextField endMonth;
         JTextField endDay;
         
         public ConfirmPeriodActionListener(JTextField startYear, JTextField startMonth, JTextField startDay,
                                          JTextField endYear, JTextField endMonth, JTextField endDay) {
         this.startYear = startYear;
         this.startMonth = startMonth;
         this.startDay = startDay;
         this.endYear = endYear;
         this.endMonth = endMonth;
         this.endDay = endDay;
        }
        public void actionPerformed(ActionEvent e) {
            if(isIntNumber(startYear.getText()) == false || (startYear.getText()).length() != 4  ||
              isIntNumber(startMonth.getText()) == false || (startMonth.getText()).length() > 3 ||
              isIntNumber(startDay.getText()) == false ||(startDay.getText()).length() > 3 ||
              isIntNumber(endYear.getText()) == false || (endYear.getText()).length() != 4  ||
              isIntNumber(endMonth.getText()) == false || (endMonth.getText()).length() > 3 ||
              isIntNumber(endDay.getText()) == false ||(endDay.getText()).length() > 3) {
              
                JOptionPane errorDialog = new JOptionPane();
                errorDialog.showMessageDialog(null, "Error! input was not a date!");
                choosePeriod();
           }
           else { 
               //turns the input into integers after we've checked if they are indeed integers.
               String startMonthTemp = new String(startMonth.getText());
               String startDayTemp = new String(startDay.getText());
               int chosenStartYear = Integer.parseInt(startYear.getText());
               int chosenStartMonth = Integer.parseInt(startMonth.getText());
               int chosenStartDay = Integer.parseInt(startDay.getText());
               
               String endMonthTemp = new String(endMonth.getText());
               String endDayTemp = new String(endDay.getText());
               int chosenEndYear = Integer.parseInt(endYear.getText());
               int chosenEndMonth = Integer.parseInt(endMonth.getText());
               int chosenEndDay = Integer.parseInt(endDay.getText());
               
               //A check for whenever the month and day inputs are written in, for instance, 02 or 2. 
               //If the first is the case, it's changed to 2.
               if((startMonth.getText()).contains("0") == true) {
                   startMonthTemp.substring((startMonth.getText()).length()-1);
                   chosenStartMonth = Integer.parseInt(startMonthTemp);
               }
               if((startDay.getText()).contains("0") == true) {
                   startDayTemp.substring((startDay.getText()).length()-1);
                   chosenStartDay = Integer.parseInt(startDayTemp);
               }
               
               if((endMonth.getText()).contains("0") == true) {
                   endMonthTemp.substring((endMonth.getText()).length()-1);
                   chosenEndMonth = Integer.parseInt(endMonthTemp);
               }
               if((endDay.getText()).contains("0") == true) {
                   endDayTemp.substring((endDay.getText()).length()-1);
                   chosenEndDay = Integer.parseInt(endDayTemp);
               }
               chooseFlightInPeriod(startYear.getText(), startMonth.getText(), startDay.getText(), 
                            endYear.getText(), endMonth.getText(), endDay.getText()); 
          }
        }
     }
}