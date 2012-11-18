
package FlightBooking;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;


/**
 * This is the graphical user interface of the program. It's this class
 * that takes care of representing the data to the user.
 * @author Mark
 */
public class GUI {
    
    private JFrame frame;
    private JPanel contentPane;
    
    public GUI() {
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
        contentPane.setBorder(new EmptyBorder(0, 0, 20, 20));
        
        makeMenuBar(frame);
        seeReservations();
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
        
        item = new JMenuItem("See Reservations");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { seeReservations(); }
            });
        menu.add(item);
        
        menu = new JMenu("Cars");
        menubar.add(menu);
        
        item = new JMenuItem("Search for available cars");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { carSearch(); }
            });
        menu.add(item);
    }
    
    
    /**
     * SKAL OMSKRIVES TOTALT.
     */
    private void makeReservation() {
        
        
        Object[] options = {"New Costumer", "Existing Customer"};
        int result = JOptionPane.showOptionDialog(frame, "Is it an existing costumer?", "Costumer Information",
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,options[0]);
        
        if(result == JOptionPane.NO_OPTION) {
            //TODO, choose costumer.
            
            chooseFlight();

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
            inputDialog.showMessageDialog(frame, cosInput, "Create Costumer", inputDialog.QUESTION_MESSAGE);
            
            //if you then press ok, the phone number is checked if it is actually a number.
            //It is assumed the program is to be used in countries with phone numbers if at least 8 characters.
                if(name.getText() == null || isIntNumber(number.getText()) == false || 
                        number.getText() == null || 8 > number.getText().length()) {
                    
                    JOptionPane errorDialog = new JOptionPane();
                    errorDialog.showMessageDialog(frame, "Error! input was incorrect, try again.");
                    
                }
                    //Create Costumer object here probably
                
                else {
                    chooseFlight();
                }
        }
    }
       
    
    /**
     * A method for choosing a type of car.
     * @return the chosen type of car. KAN FORMENTLIG BRUGES IGEN.
     */
    private void chooseFlight() {
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
    }
    
    
    /**
     * Used for selecting car type.
     * @param choice is the car type chosen.
     */
    private void carTypeChosen(String choice) {
        
        JTextField startDate = new JTextField("ddmmyy");
            JTextField endDate = new JTextField("ddmmyy");
            JPanel cosInput = new JPanel(new GridLayout(0,1));
            
            cosInput.add(new JLabel("Start Date:"));
            cosInput.add(startDate);
            cosInput.add(new JLabel("End Date:"));
            cosInput.add(endDate);
            
            JOptionPane inputDialog = new JOptionPane();
            inputDialog.showMessageDialog(frame, cosInput, "Choose Time Period", inputDialog.QUESTION_MESSAGE);
            
            //Checks if the dates are null, not numbers or either too long or too short.
            if(startDate.getText() == null || isIntNumber(startDate.getText()) == false ||
                     6 != startDate.getText().length() || endDate.getText() == null || 
                    isIntNumber(endDate.getText()) == false || 6 != endDate.getText().length()) {
                    
                    JOptionPane errorDialog = new JOptionPane();
                    errorDialog.showMessageDialog(frame, "Error! input was incorrect, try again.");
            }
            else {
                bookCar(choice, Integer.parseInt(startDate.getText()), Integer.parseInt(endDate.getText()));
            }
    }
    
    
    /**
     * Books a car of the given type in a given period. WELL UHM... JA.
     */
    private void bookCar(String carType, int startDate, int endDate) {
        
    }
    
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
     * Shows the reservation schedule. DELE KAN BRUGES.
     */
    private void seeReservations() {
        contentPane.removeAll();
        //Creates a table to list cars and if they're booked on certain dates.
        Object [] date = {"dec 1", "dec 2"};
        int numberOfDates = date.length+1;
        Object[] columnNames = new String[numberOfDates];
        columnNames[0] = "Cars";
        for(int i=0; i <date.length; i++) {
            columnNames[i+1] = date[i];
        }

        Object[][] data = {
            {"Van1", "booked", "not booked"},
            {"4-door Car1", "not booked", "not booked"},
            {"2-door Car1", "booked", "booked"},
            {"2-door Car2", "not booked", "booked"},
            {"Boat1","not booked", "not booked"}        
        };
        
        //Creates the table and makes the cells uneditable.
        JTable table = new JTable(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };    
        contentPane.add(table);
        contentPane.add(table.getTableHeader(), BorderLayout.PAGE_START);
        contentPane.add(table, BorderLayout.CENTER);
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setSize(500, 300);
        frame.setVisible(true);
    }
    
    
    /**
     * When "Cars" -> "Search for available cars" is activated, a input dialogue
     * for what type of car you want to search for, followed by a
     * start date input dialogue, and then a end date inut dialogue. DELE KAN BRUGES.
     */
    private void carSearch() {
        JPanel inputs = new JPanel(new GridLayout(0,1));
        
        String[] carTypes = {"type1", "type2", "type3", "type4", "type5"};
        JComboBox comboBox = new JComboBox(carTypes);
        JTextField startDate = new JTextField("ddmmyy");
        JTextField endDate = new JTextField("ddmmyy");
        
        inputs.add(new JLabel("What type of car?"));
        inputs.add(comboBox);
        inputs.add(new JLabel());
        inputs.add(new JLabel("Start:"));
        inputs.add(startDate);
        inputs.add(new JLabel());
        inputs.add(new JLabel("End:"));
        inputs.add(endDate);
        
        JOptionPane inputDialog = new JOptionPane();
        int result = inputDialog.showConfirmDialog(frame, inputs, "Search Option", inputDialog.OK_CANCEL_OPTION);
        
        if(result == inputDialog.YES_OPTION) {
           if(isIntNumber(startDate.getText()) == false || isIntNumber(endDate.getText()) == false) {
               JOptionPane errorDialog = new JOptionPane();
               errorDialog.showMessageDialog(null, "Error! input was not a date!");
               carSearch();
           }
           else {
               String carTypeChosen = comboBox.getSelectedItem().toString();
               showCars(carTypeChosen, Integer.parseInt(startDate.getText()), Integer.parseInt(endDate.getText()));
           } 
        }
            
    }
    
    
    /**
     * Shows a list of available cars of a given type in the given time period. WELL... 
     */
    private void showCars(String carTypeChosen, int startDate, int endDate) {
        //Will poop out the list
    }
    
    
    /**
     * Is used to check if a string is a integer. In our case this is used for checking
     * input data from textfields.
     * @param num is the string that is to be checked. WIN!
     */
     private boolean isIntNumber(String num){
        try{
            Integer.parseInt(num);
        } catch(Exception NumberFormatException) {
            return false;
        }
        return true;
    }
            
}