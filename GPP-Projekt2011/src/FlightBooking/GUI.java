
package FlightBooking;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.*;


/**
 * This is the graphical user interface of the program. It's this class
 * that takes care of representing the data to the user.
 * @author Mark
 */
public class GUI{
    
    private JFrame frame;
    private JPanel contentPane;
    private JComboBox startComboBox;
    private JComboBox endComboBox;
    private Database database;
    public GUI() {
        try {
            database = new Database();
        } catch (SQLException ex) {
            System.out.println("Initialisation action exception : " + ex);
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
     * 
     */
    private void makeReservation() {
    chooseFlight("year", "month", "day");
    }
    
    private void chooseDate() {
        JPanel dateInput = new JPanel(new GridLayout(0,1));

        JTextField startYear = new JTextField("yyyy");
        JTextField startMonth = new JTextField("mm");
        JTextField startDay = new JTextField("dd");

        dateInput.add(new JLabel("Please select date of departure."));
        dateInput.add(startYear);
        dateInput.add(startMonth);
        dateInput.add(startDay);
        
        JOptionPane inputDialog = new JOptionPane();
        int result = inputDialog.showConfirmDialog(frame, dateInput, "Search Option", inputDialog.OK_CANCEL_OPTION);
        
        if(result == inputDialog.YES_OPTION) {
           if(isIntNumber(startYear.getText()) == false || 
              isIntNumber(startMonth.getText()) == false || 
              isIntNumber(startDay.getText()) == false) {
              
                JOptionPane errorDialog = new JOptionPane();
                errorDialog.showMessageDialog(null, "Error! input was not a date!");
                chooseDate();
           }
           else {
               chooseFlight(startYear.getText(), startMonth.getText(), startDay.getText());
           }
        }
    }
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
            }
        }
    }
    
    private void chooseSeats(String flightID) {
        //display ledige sæder på den valgte afgang, ala minestryger.
    }
        
    private void CostumerInput() {    
        Object[] options = {"New Costumer", "Existing Customer"};
        int result = JOptionPane.showOptionDialog(frame, "Is it an existing costumer?", "Costumer Information",
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,options[0]);
        
        if(result == JOptionPane.NO_OPTION) {
            //Skal måske dø.
        }
        
        //If you choose new costumer:
        if(result == JOptionPane.YES_OPTION) {
        
            JTextField name = new JTextField();
            JTextField number = new JTextField();
            JPanel cosInput = new JPanel(new GridLayout(0,1));
            
            cosInput.add(new JLabel("Costumer name:"));
            cosInput.add(name);
            cosInput.add(new JLabel("Costumer's phone number:"));
            cosInput.add(number);
            
            JOptionPane inputDialog = new JOptionPane();
            Object[] CostumerOptions = {"Next", "Abort"};
            int CostumerResult = inputDialog.showOptionDialog(frame, cosInput, "Create Costumer",JOptionPane.YES_NO_OPTION, inputDialog.QUESTION_MESSAGE, null, CostumerOptions, CostumerOptions[0]);
            
            //if you then press ok, the phone number is checked if it is actually a number.
            //It is assumed the program is to be used in countries with phone numbers if at least 8 characters.
            if(CostumerResult == inputDialog.YES_OPTION)   { 
                if(name.getText() == null || isIntNumber(number.getText()) == false || 
                   number.getText() == null || 8 > number.getText().length()) {
                    
                    JOptionPane errorDialog = new JOptionPane();
                    errorDialog.showMessageDialog(frame, "Error! input was incorrect, try again.");
                    
                }
                
                else {
                    
                }
            }
        }
    }
    
    private void makeOrder() {
        JPanel orderInput = new JPanel(new GridLayout(0,1));

        JTextField costumerID = new JTextField("costumer ID");
        JTextField flightID = new JTextField("flight ID");
        JTextField seatID = new JTextField("seat ID");

        orderInput.add(new JLabel("Mark er Gud, fuck hoveder!"));
        orderInput.add(costumerID);
        orderInput.add(flightID);
        orderInput.add(seatID);
        
        JOptionPane inputDialog = new JOptionPane();
        int result = inputDialog.showConfirmDialog(frame, orderInput, "Search Option", inputDialog.OK_CANCEL_OPTION);
        
        if(result == inputDialog.YES_OPTION) {
           if(isIntNumber(costumerID.getText()) == false || 
              isIntNumber(flightID.getText()) == false || 
              isIntNumber(seatID.getText()) == false) {
              
                JOptionPane errorDialog = new JOptionPane();
                errorDialog.showMessageDialog(null, "Error! input was not a date!");
                chooseDate();
           }
           else { 
                try {
                    Order order = new Order(stringConverter(costumerID.getText()),
                                            stringConverter(flightID.getText()),
                                            stringConverter(seatID.getText()));
                    order.insert(database);
                } catch (SQLException ex) {
                    System.out.println("Order action exception: " + ex);
                }
           }
        }
    }
       
    
    /**
     * A method for choosing a type of car.
     * @return the chosen type of car. KAN FORMENTLIG BRUGES IGEN.
     */
    /* private void chooseFlightTestMethod() {
        String[] carTypes = {"type1", "type2", "type3", "type4", "type5"};
        
        final JFrame carFrame = new JFrame("Choose Flight");       
       
        JPanel carContentPane = (JPanel)carFrame.getContentPane();
        JPanel buttonRow1 = new JPanel(new GridLayout(0,1));
        JPanel buttonRow2 =new JPanel (new GridLayout(0,1));
        
        carContentPane.setBorder(new EmptyBorder(6, 6, 6, 6));
        carContentPane.add(buttonRow1, BorderLayout.WEST);
        carContentPane.add(buttonRow2, BorderLayout.EAST);
        
        int firstRow = carTypes.length/2;
        int secondRow = carTypes.length - carTypes.length/2;
        
        for(int i = 0; i <= firstRow; i++) {
            final JButton button = new JButton(carTypes[i]);
            button.addActionListener(new ActionListener() {
                
                public void actionPerformed(ActionEvent e)
                {
                    carFrame.dispose();
                    carTypeChosen(button.getText());
                }
            });    
            buttonRow1.add(button);
        }
        
        for (int i = carTypes.length-1; i >= secondRow; i--) {
            final JButton button = new JButton(carTypes[i]);
            button.addActionListener(new ActionListener() {
                
                public void actionPerformed(ActionEvent e)
                {
                    carFrame.dispose();
                    carTypeChosen(button.getText());
                }
            });    
            buttonRow2.add(button);
        }
        
        carFrame.pack();
        carFrame.setVisible(true);
    }*/
    
    /**
     * SER UD TIL AT KUNNE BRUGES IGEN. MANGLER DOG SELVE EDIT/DELETE DELEN.
     */
    private void editReservation() {
        Object[] options = {"Edit Reservation", "Delete Reservation"};
        JTextField phoneNumber = new JTextField();
            JTextField resID = new JTextField();
            JPanel cosInput = new JPanel(new GridLayout(0,1));
            
            cosInput.add(new JLabel("Only enter in one field please."));
            cosInput.add(new JLabel("Costumer phone number:"));
            cosInput.add(phoneNumber);
            cosInput.add(new JLabel("Reservation ID:"));
            cosInput.add(resID);
            
        int result = JOptionPane.showOptionDialog(frame, cosInput, 
                "Edit or Delete Reservation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
                null, options,options[0]);
        
        //Checks if the input is valid
        if(result == JOptionPane.NO_OPTION) {
            if(resID.getText() == null && phoneNumber.getText() == null || resID.getText() == null &&
                    8 > phoneNumber.getText().length() || resID.getText() == null &&
                    isIntNumber(phoneNumber.getText()) == false) {
                JOptionPane errorDialog = new JOptionPane();
                errorDialog.showMessageDialog(frame, "Error! input was incorrect, try again.");
            }
            else {
                //Delete Reservation
            }
        }
        
        //Checks if the input is valid
        if(result == JOptionPane.YES_OPTION) {
            if(resID.getText() == null && phoneNumber.getText() == null || resID.getText() == null &&
                    8 > phoneNumber.getText().length() || resID.getText() == null &&
                    isIntNumber(phoneNumber.getText()) == false) {
                JOptionPane errorDialog = new JOptionPane();
                errorDialog.showMessageDialog(frame, "Error! input was incorrect, try again.");
            }
            else {
                //Open window for editing reservation.
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
     
     private int stringConverter(String string) {
         int integer = Integer.parseInt(string);
         return integer;
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
            //Hvorfor virker jeg?
            for(String val : destinations){
                model.addElement(val);
            }
        }
     }
            
}