
package FlightBooking;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.*;
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
    private JComboBox startComboBox, endComboBox;
    private Database database;
    
    
    public GUI() {
        try {
            database = new Database();
        } catch (SQLException ex) {
            System.out.println("Initialisation exception: " + ex);
        }
        makeFrame();
    }
    
    
    /**
     * Creates the frame and calls makeMenuBar.
     */
    private void makeFrame() {
        frame = new JFrame("Flight Booking");       
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = (JPanel)frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        
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
        
        /*item = new JMenuItem("See Reservations");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { seeResevations(); }
            });
        menu.add(item);*/
        
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
        
        JOptionPane inputDialog = new JOptionPane();
        int result = inputDialog.showConfirmDialog(frame, dateInput, "Search Option", inputDialog.OK_CANCEL_OPTION);
        
        if(result == inputDialog.YES_OPTION) {
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
               NewDate date = new NewDate(year, month, day);
               chooseFlight(startYear.getText(), startMonth.getText(), startDay.getText()); 
          }
    
       }
    }
    
    
    /*
     * From the given date, the flights are found and put into JComoboBoxes(dropdown menues). 
     * The 2nd JComboBox's content changes when a value is selected in the first JCombobox. This is
     * handled by our inner class JComboBoxActionListener.
     */
    private void chooseFlight(String year, String month, String day) {
        JPanel flightChoice = new JPanel(new GridLayout(0,1));
        
        String[] Flights = {"Nothing Selected", "type1", "type2", "type3", "type4", "type5"};
        startComboBox = new JComboBox(Flights);
              
        flightChoice.add(new JLabel("From what airport?"));
        flightChoice.add(startComboBox);
        startComboBox.addActionListener(new JComboBoxActionListener(this));
        
        String[] destinations = {"Nothing Selected"};
        endComboBox = new JComboBox(destinations);
              
        flightChoice.add(new JLabel("Where to?"));
        flightChoice.add(endComboBox);
        
        JOptionPane inputDialog = new JOptionPane();
        int result = inputDialog.showConfirmDialog(frame, flightChoice, "Search Options", inputDialog.OK_CANCEL_OPTION);
        if(result == inputDialog.YES_OPTION) {
            if(startComboBox.getSelectedItem() == "Nothing Selected") {
              
                JOptionPane errorDialog = new JOptionPane();
                errorDialog.showMessageDialog(null, "Error! Please choose a flight!");
                chooseFlight(year, month, day);
            }
            else {
                //Send den valgtes flightID til chooseSeats.
                chooseSeats("123432", 40);
            }
        }
    }
    
    
    /*
     * Makes a window with all seats on the plain. Booked seats are red, available are green and chosen seats are blue.
     */
    private void chooseSeats(String flightID, int numberOfSeats) {
        JDialog seatsDialog = new JDialog(frame);
        
       Container seatsContentPane = seatsDialog.getContentPane();
       
       int seatsInColumn = numberOfSeats/4;
       JButton seat = new JButton();
       JPanel left = new JPanel(new GridLayout(seatsInColumn,2));
       JPanel right = new JPanel(new GridLayout(seatsInColumn,2));
       JPanel middle = new JPanel(new GridLayout(1,1));
       JPanel south = new JPanel(new GridLayout(1,1));
       
       int i = 1;
       int j = 0;
       String seatName;
       JButton[] button = new JButton[numberOfSeats]; 
         while(i<=seatsInColumn) {
            seatName = i+"a";
            seat = new JButton(seatName);
            seat.setBackground(Color.GREEN);
            seat.addActionListener(new SeatsActionListener());
            left.add(seat);
            button[j] = seat;
            j++;
            
            seatName = i+"b";
            seat = new JButton(seatName);
            seat.setBackground(Color.GREEN);
            seat.addActionListener(new SeatsActionListener());
            left.add(seat);
            button[j] = seat;
            j++;
            
            seatName = i+"c";
            seat = new JButton(seatName);
            seat.setBackground(Color.GREEN);
            seat.addActionListener(new SeatsActionListener());
            right.add(seat);
            button[j] = seat;
            j++;
            
            seatName = i+"d";
            seat = new JButton(seatName);
            seat.setBackground(Color.GREEN);
            seat.addActionListener(new SeatsActionListener());
            right.add(seat);
            button[j] = seat;
            j++;
            
            i++;
         }
         
       
         //These fields are declared final at this point, so the ActionListener on the save button can access them.
       final JButton[] buttonFinished = button;
       final String chosenFlight = flightID;
       final int seats = numberOfSeats;
       
       middle.add(new JButton()).setEnabled(false);
       JButton save = new JButton("Save");
       south.add(save);
       save.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) { saveChosenSeats(buttonFinished, chosenFlight, seats); }
                              });
       
       seatsContentPane.add(left, BorderLayout.WEST); 
       seatsContentPane.add(middle, BorderLayout.CENTER);
       seatsContentPane.add(right, BorderLayout.EAST);
       seatsContentPane.add(south, BorderLayout.SOUTH);
       
       seatsDialog.pack();
       seatsDialog.setLocationRelativeTo(frame);
       seatsDialog.setResizable(false);
       seatsDialog.setVisible(true);
    }
    
    private void saveChosenSeats(JButton[] button, String flightID, int numberOfSeats) {
        int seats = 0;
        String nameOfSeats = new String();
        
        for(int i = 0; i<button.length; i++) {
            if(button[i].getBackground() == Color.BLUE) {
                nameOfSeats = nameOfSeats+((button[i]).getText() + " ");
                seats++;
            }
        }
        //If you have chosen any seats, you're asked to go back, otherwise it will end the process.
        if(seats == 0) {
            JOptionPane errorDialog = new JOptionPane();
            int errorResult = errorDialog.showConfirmDialog(frame,"You did not choose any seats,"
                                                            + " do you wish to go back and select seats again?",
                                                            "No seats selected", errorDialog.OK_CANCEL_OPTION);
            if(errorResult == errorDialog.YES_OPTION) {
                chooseSeats(flightID, numberOfSeats);
            }
        }
        //If you have chosen seats, you will be shown how many and which, whereafter it will continue til creating customers.
        else if(seats > 0) {
            JOptionPane succesDialog = new JOptionPane();
            int succesResult = succesDialog.showConfirmDialog(frame,"You have chosen: " + seats + " seat(s). Name of chosen seat(s): "+ nameOfSeats,
                                                            "Confirm your choice", succesDialog.OK_CANCEL_OPTION);
            if(succesResult == succesDialog.YES_OPTION) {
                createCustomer(seats, nameOfSeats);
            }
            //If you're not happy with your choice or try to close the window, you're sent back to choosing seats.
            else {
                chooseSeats(flightID, numberOfSeats);
            }
        }
    }
    
    
    /*
     * You create the traveller responsible for the tickets.
     */
    private void createCustomer(int numberOfSeats, String nameOfSeats) {    
        
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
                    createCustomer(seatsRemaining, nameOfSeats);
            }
            
            //If correct input, we proceed to creating additional customers if there's more than one customer.
            else {
                Customer customer = new Customer(firstName.getText(), lastName.getText(), country.getText(), city.getText(),
                        address.getText(), phoneNumber.getText(), email.getText());
                seatsRemaining--;
                //This string is used when saving the names of traveller(s) in the database.
                String traveller = firstName.getText() + " " + lastName.getText() + " ";
                
                //If there's only one, we proceed directly to making the order.
                if(seatsRemaining == 0) {
                    makeOrder(traveller, customer, nameOfSeats); 
                }
                //If there's more than one traveller, the remaining will be created in the next step.
                else{
                  createAdditionalCustomers(seatsRemaining, customer, traveller, nameOfSeats);
                }
            }            
        }
        
        //If you choose cancel or just try to close the window a "are you sure"-box will appear.
        else {
            JOptionPane cancelDialog = new JOptionPane();
            int cancelResult = cancelDialog.showConfirmDialog(frame,"Are you sure you wish to quit?",
                                                              "Customer Information", inputDialog.OK_CANCEL_OPTION);
            if(cancelResult == cancelDialog.NO_OPTION) {
                createCustomer(numberOfSeats, nameOfSeats);
            }
        }
    }
    
    
    /*
     * This method gets the names of the additional travellers if there are any. 
     * The names are added to the string travellerNames.
     * 
     */
    private void createAdditionalCustomers(int seatsRemaining, Customer customer, String travellerNames, String nameOfSeats) {
          
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
                    errorDialog.showMessageDialog(null, "Error! all the inputs was not a name!");
                    createAdditionalCustomers(seatsRemaining, customer, travellerNames, nameOfSeats);
                }
            }
            
            //For each traveller, the traveller's name is added to the string from createCustomer.
            for(int k = 0; k<additionalCustomerNames.length; k++) {
               travellerNames = travellerNames+(additionalCustomerNames[k].getText())+" ";
            }
            makeOrder(travellerNames, customer, nameOfSeats);
        }
    }
   
    
    private void makeOrder(String travellers, Customer customer, String nameOfSeats) {
                try {
                    customer.insert(database);
                    int customerID = customer.getKey();
                    Order order = new Order(customerID, 2, nameOfSeats, travellers);
                    order.insert(database);
                } 
                
                catch (SQLException ex) {
                    System.out.println("Saving order failed. Exception: " + ex);
                }
    }
    
    
    /**
     * SER UD TIL AT KUNNE BRUGES IGEN. MANGLER DOG SELVE EDIT/DELETE DELEN.
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
                    //ResultSet rs = database.execute("SELECT * FROM Customer WHERE id = " + cusID.getText() +
                    //      " AND phonenumber = " + phoneNumber.getText());
                    Order order = new Order(database, Integer.parseInt(cusID.getText()));
                } 
                catch (SQLException ex) {
                    System.out.println("finding order exception : " + ex);
                }
            
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
     
     
     /**
      * When a Start Airport is chosen, the Destination box will change to
      * display relevant flights. This is handled by our inner class 
      * JComboBoxActionListener.
      */
     private class JComboBoxActionListener implements ActionListener {
         
        public JComboBoxActionListener(GUI gui) {
         
     }
        public void actionPerformed(ActionEvent e) {
            String selectedValue = startComboBox.getSelectedItem().toString();
            String[] destinations = null;

            DefaultComboBoxModel model = (DefaultComboBoxModel) endComboBox.getModel();      
            model.removeAllElements();

            if(selectedValue.equals("type1")){
                destinations = new String[]{"val11", "val12", "val13"};
            } 
            else if(selectedValue.equals("type2")){
                destinations = new String[]{"val21", "val22", "val23"};
            } 
            else if(selectedValue.equals("type3")){
                destinations = new String[]{"val31", "val32", "val33"};
            }

            for(String val : destinations){
                model.addElement(val);
            }
        }
     }
     
     private class SeatsActionListener implements ActionListener {
         public void SeatsActionListener() {
         }
         
         public void actionPerformed(ActionEvent e) {   
            if(e.getSource() instanceof JButton) {
             
                if(((JButton)e.getSource()).getBackground() == Color.GREEN) {
                    ((JButton) e.getSource()).setBackground(Color.BLUE);
                }
             
                else if(((JButton)e.getSource()).getBackground() == Color.BLUE) {
                ((JButton)e.getSource()).setBackground(Color.GREEN);
                }
            }
         }
    }
}