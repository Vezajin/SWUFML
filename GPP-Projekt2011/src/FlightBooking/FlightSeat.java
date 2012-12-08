/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FlightBooking;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
/**
 *
 * @author Mark
 */
public class FlightSeat {
    
    private GUI gui;
    private Database database;
    private FlightScanner flightScanner;
            
    
    public FlightSeat(GUI gui, Database database, FlightScanner flightScanner) {
        this.gui = gui;
        this.database = database;
        this.flightScanner = flightScanner;
    }
    public JDialog chooseSeats(int flightID, int numberOfSeats, String flight, int methodChecker, int customerID) {
        JDialog seatsDialog = new JDialog(gui.returnFrame());
        
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
        
        ArrayList<String>bookedSeats = new ArrayList<String>(); 
        try {
            ResultSet rsSeats = database.execute("SELECT seatstring FROM Orders WHERE flightid = " + flightID);
            while(rsSeats.next()) {
                bookedSeats = (flightScanner.seatAnalyser(rsSeats.getString("seatstring")));
                for(int k = 0; k<bookedSeats.size(); k++) {
                    for(int m = 0; m <button.length; m++) {
                        if((button[m].getText()).equals(bookedSeats.get(k))) {
                            button[m].setBackground(Color.RED);
                            button[m].setEnabled(false);
                        }
                    }
                }
            }    
            
            //These fields are declared final at this point, so the ActionListener on the save button can access them.
            final JButton[] buttonFinished = button;
            final int chosenFlight = flightID;
            final int seats = numberOfSeats;
            final int finalMethodChecker = methodChecker;
            final int finalCustomerID = customerID;
       
            middle.add(new JButton()).setEnabled(false);
            JButton save = new JButton("Save");
            south.add(save);
            SaveActionListener SAC = new SaveActionListener(buttonFinished, chosenFlight, seats, flight, finalMethodChecker, finalCustomerID);
            save.addActionListener(SAC);
            
            seatsContentPane.add(new JLabel(flight), BorderLayout.NORTH);
            seatsContentPane.add(left, BorderLayout.WEST); 
            seatsContentPane.add(middle, BorderLayout.CENTER);
            seatsContentPane.add(right, BorderLayout.EAST);
            seatsContentPane.add(south, BorderLayout.SOUTH);
            
//------------------------------------------------------------------------------
            if(methodChecker == 1 || customerID != 0) {
                ArrayList<String>customersBookedSeats = new ArrayList<String>();
                ArrayList<String>customersBookedNames = new ArrayList<String>();
                //Finds all the booked seats attached to the order that is to be changed.
                ResultSet rsBookedSeats = database.execute("SELECT seatstring AND namestring FROM Orders WHERE customerid = " + customerID);
                if(rsBookedSeats.next()) {
                    //gets an ArrayList with all the booked seats. 
                    
                    customersBookedSeats = (flightScanner.seatAnalyser(rsBookedSeats.getString("seatstring")));
                    customersBookedNames = (flightScanner.nameAnalyser(rsBookedSeats.getString("namestring")));
                    JButton[] chosenButtons = new JButton[customersBookedSeats.size()];
                    
                    
                for(int a = 0; a<customersBookedSeats.size(); a++) {
                    for(int b = 0; b <button.length; b++) {
                        //Makes the booked seats for the order Blue and clickable again. 
                        if((button[b].getText()).equals(customersBookedSeats.get(a))) {
                            button[b].setBackground(Color.BLUE);
                            button[b].setEnabled(true);
                            chosenButtons[a] = button[b];
                        }
                    }
                }
                final JButton[] finalChosenSeats = chosenButtons;
                //Removes the ActionListener from the save button.
                save.removeActionListener(SAC);
                
                Object[] options = {"Add Additional Seat(s)", "Delete Seat(s)", "Move Current Seats"};
                            int optionResult = JOptionPane.showOptionDialog(gui.returnFrame(),new JPanel(),"What do you wish to do?",
                                                                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                                                                            null, options, options[0]);
                            //If you wanna add more seats to the order.
                            if(optionResult == JOptionPane.YES_OPTION) {
                                for(int c = 0; c<chosenButtons.length; c++) {
                                    chosenButtons[c].setEnabled(false);
                                    
                                }
                                // Adds new AL to the save button. when pressed it will now call saveAdditionalSeats.
                                AddSeatsActionListener ASAC = new AddSeatsActionListener(database, gui, this, buttonFinished, chosenFlight, 
                                                                                            seats, flight, finalMethodChecker, finalCustomerID);
                                save.addActionListener(ASAC);
                                makeDialog(seatsDialog);
                                return seatsDialog;
                            }
                            //If you wanna delete seats from the order.
                            if(optionResult == JOptionPane.NO_OPTION) {
                                for(int c = 0; c<button.length; c++) {
                                    if(button[c].getBackground() == Color.GREEN) {
                                        button[c].setEnabled(false);
                                    }
                                }
                                for(int c = 0; c<chosenButtons.length; c++) {
                                    chosenButtons[c].setEnabled(true);
                                    button[c].setToolTipText(customersBookedNames.get(c));
                                }
                                DeleteSeatsActionListener DSAC = new DeleteSeatsActionListener(finalChosenSeats, chosenFlight, seats, flight, 
                                                                                                finalMethodChecker, finalCustomerID, customersBookedSeats);
                                save.addActionListener(DSAC);
                                makeDialog(seatsDialog);
                                return seatsDialog;
                            }
                            //If you want to move your seats.
                            if(optionResult == JOptionPane.CANCEL_OPTION) {
                                MoveSeatsActionListener MSAC = new MoveSeatsActionListener(buttonFinished, finalChosenSeats, 
                                                                        chosenFlight, seats, flight, finalMethodChecker, finalCustomerID);
                                save.addActionListener(MSAC);
                                makeDialog(seatsDialog);
                                return seatsDialog;
                            }
                            else {

                            }
                }
            }
//------------------------------------------------------------------------------            
            else {
                makeDialog(seatsDialog);
                return seatsDialog;
            }
        }
        catch (SQLException ex) {
            System.out.println("finding order exception : " + ex);
        }
        //This is just to make the compiler happy. Also if you press the close button, when methodChecker == 1, this will happen.
        return null;
    }
    
    
    private void makeDialog(JDialog seatsDialog) {
       
        seatsDialog.pack();
        seatsDialog.setLocationRelativeTo(gui.returnFrame());
        seatsDialog.setResizable(false);
        seatsDialog.setVisible(true);
    }
    
    
    private void saveChosenSeats(JButton[] button, int flightID, int numberOfSeats, String flight, int methodChecker, int customerID) {
        int seats = 0;
        String nameOfSeats = new String();
        
        for(int i = 0; i<button.length; i++) {
            if(button[i].getBackground() == Color.BLUE) {
                nameOfSeats = nameOfSeats+((button[i]).getText() + " ");
                seats++;
            }
        }
        //If you havent chosen any seats, you're asked to go back, otherwise it will end the process.
        if(seats == 0) {
            JOptionPane errorDialog = new JOptionPane();
            int errorResult = errorDialog.showConfirmDialog(gui.returnFrame(),"You did not choose any seats,"
                                                            + " do you wish to go back and select seats again?",
                                                            "No seats selected", errorDialog.OK_CANCEL_OPTION);
            if(errorResult == errorDialog.YES_OPTION) {
                chooseSeats(flightID, numberOfSeats, flight, methodChecker, customerID);
            }
        }
        //If you have chosen seats, you will be shown how many and which, whereafter it will continue til creating customers.
        else if(seats > 0) {
            JOptionPane succesDialog = new JOptionPane();
            int succesResult = succesDialog.showConfirmDialog(gui.returnFrame(),"You have chosen: " + seats + " seat(s). Name of chosen seat(s): "+ nameOfSeats,
                                                            "Confirm your choice", succesDialog.OK_CANCEL_OPTION);
            if(succesResult == succesDialog.YES_OPTION) {
                gui.createCustomer(seats, nameOfSeats, flightID, flight);
            }
            //If you're not happy with your choice or try to close the window, you're sent back to choosing seats.
            else {
                chooseSeats(flightID, numberOfSeats, flight, methodChecker, customerID);
            }
        }
    }
    
   
    /*
     * Changes the color of the clicked button to green if its already blue or the other way around.
     */
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
    
    /*
     * When creating a new order, this save method is used.
     */
    private class SaveActionListener implements ActionListener {
        
        private JButton[] buttonFinished;
        private int chosenFlight;
        private int seats;
        private int finalMethodChecker;
        private int finalCustomerID;
        private String flight;
        
        public SaveActionListener(JButton[] buttonFinished, int chosenFlight, int seats, String flight, int finalMethodChecker, int finalCustomerID) {
            this.buttonFinished = buttonFinished;
            this.chosenFlight = chosenFlight;
            this.seats = seats;
            this.finalMethodChecker = finalMethodChecker;
            this.finalCustomerID = finalCustomerID;
            this.flight = flight;
        }
        public void actionPerformed(ActionEvent e) {
            saveChosenSeats(buttonFinished, chosenFlight, seats, flight, finalMethodChecker, finalCustomerID);
        }
    }
    
     
    private class DeleteSeatsActionListener implements ActionListener {
        private JButton[] finalChosenButtons;
        private int chosenFlight;
        private int seats;
        private int finalMethodChecker;
        private int finalCustomerID;
        private ArrayList<String> customerSeatsAL;
        private String flight;
        
        public DeleteSeatsActionListener(JButton[] finalChosenButtons, int chosenFlight, int seats, String flight, int finalMethodChecker, 
                                              int finalCustomerID, ArrayList<String> customerSeatsAL) {
            
            this.finalChosenButtons = finalChosenButtons;
            this.chosenFlight = chosenFlight;
            this.seats = seats;
            this.finalMethodChecker = finalMethodChecker;
            this.finalCustomerID = finalCustomerID;
            this.customerSeatsAL = customerSeatsAL;
            this.flight = flight;
         }
         
         public void actionPerformed(ActionEvent e) {   
            int remainingSeats = 0;
            String nameOfSeats = new String();
            String deletedSeats = new String();
            String travellerNames = new String();
            String removedNames = new String();
             for(int a = 0; a<customerSeatsAL.size(); a++) {
                for(int b = 0; b <finalChosenButtons.length; b++) {
                    //Makes the booked seats for the order Blue and clickable again. Also removes the ActionListener from the save button.
                    if((finalChosenButtons[b].getText()).equals(customerSeatsAL.get(a)) && finalChosenButtons[b].getBackground() == Color.BLUE) {
                            remainingSeats++;
                            nameOfSeats = nameOfSeats+((customerSeatsAL.get(a)) + " ");
                            travellerNames = finalChosenButtons[b].getToolTipText() + ", ";
                    }
                    else if(finalChosenButtons[b].getText().equals(customerSeatsAL.get(a)) && finalChosenButtons[b].getBackground() == Color.GREEN) {
                        deletedSeats = deletedSeats+((customerSeatsAL.get(a)) + " ");
                        removedNames = finalChosenButtons[b].getToolTipText() + ", ";
                    }
                }
            }
            if(remainingSeats == customerSeatsAL.size()) {
                JOptionPane errorDialog = new JOptionPane();
                errorDialog.showMessageDialog(null, "You did not delete any seats, please try again");
                 chooseSeats(chosenFlight, seats, flight, finalMethodChecker, finalCustomerID);
            }
            else {
                 try {
                     database.execute("UPDATE Orders WHERE customerid = " + finalCustomerID + " SET namestring = " + travellerNames +
                                     " AND UPDATE Orders WHERE customerid = " + finalCustomerID + " SET seatstring = " + nameOfSeats);
                 } catch (SQLException ex) {
                     System.out.println("ERROR! Could not delete seats from order. Exception: " + ex);
                 }
            }
         }
    }
    
    
    private class MoveSeatsActionListener implements ActionListener {
        private JButton[] buttonFinished;
        private JButton[] finalChosenButtons;
        private int chosenFlight;
        private int seats;
        private int finalMethodChecker;
        private int finalCustomerID;
        private String flight;
        
        public MoveSeatsActionListener(JButton[] buttonFinished, JButton[] finalChosenButtons, int chosenFlight, int seats, String flight, int finalMethodChecker, 
                                              int finalCustomerID) {
            this.buttonFinished = buttonFinished;
            this.finalChosenButtons = finalChosenButtons;
            this.chosenFlight = chosenFlight;
            this.seats = seats;
            this.finalMethodChecker = finalMethodChecker;
            this.finalCustomerID = finalCustomerID;
            this.flight = flight;
        }
        
        String nameOfSeats = new String();
        int howManySeatsChosen = 0;
        
        public void actionPerformed(ActionEvent e) {  
            for(int i = 0; i<buttonFinished.length; i++) {
                if(buttonFinished[i].getBackground() == Color.BLUE) {
                     howManySeatsChosen++;
                     nameOfSeats = nameOfSeats+((buttonFinished[i]).getText() + " ");
                }
            }
            if(howManySeatsChosen == finalChosenButtons.length) {
                try {
                    database.execute("UPDATE Orders WHERE customerid = " + finalCustomerID + " SET seatstring = " + nameOfSeats);
                } catch (SQLException ex) {
                    System.out.println("Error updating new seats, exception: " + ex);
                }
            }
            else {
                JOptionPane errorDialog = new JOptionPane();
                errorDialog.showMessageDialog(null, "You chose the wrong amount of seats, please try again.");
                 chooseSeats(chosenFlight, seats, flight, finalMethodChecker, finalCustomerID);
            }
         }
    }
}



