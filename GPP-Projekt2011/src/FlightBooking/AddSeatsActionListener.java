/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FlightBooking;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

/**
 *
 * @author Mark
 */
public class AddSeatsActionListener implements ActionListener {
         
        private JButton[] buttonFinished;
        private int chosenFlight;
        private int seats;
        private int finalMethodChecker;
        private int finalCustomerID;
        private GUI gui;
        private FlightSeat flightseat;
        private Database database;
        private String flight;
        private JDialog seatsDialog;
                
        public AddSeatsActionListener(Database database, GUI gui, FlightSeat flightseat, JButton[] buttonFinished, int chosenFlight, 
                                            int seats, String flight, int finalMethodChecker, int finalCustomerID, JDialog seatsDialog) {
            this.database = database;
            this.gui = gui;
            this.flightseat = flightseat;
            this.buttonFinished = buttonFinished;
            this.chosenFlight = chosenFlight;
            this.seats = seats;
            this.finalMethodChecker = finalMethodChecker;
            this.finalCustomerID = finalCustomerID; 
            this.flight = flight;
            this.seatsDialog = seatsDialog;
        }
         
         public void actionPerformed(ActionEvent e) {   
             saveAdditionalSeats(buttonFinished, chosenFlight, seats, finalMethodChecker, finalCustomerID);
         }
         
        /*
        * This method is used when editing an order, where you wish to add one or more seats to an existing order.
        */
        private void saveAdditionalSeats(JButton[] button, int flightID, int numberOfSeats, int methodChecker, int customerID) {
        int seats = 0;
        String nameOfSeats = new String();
        
        for(int i = 0; i<button.length; i++) {
            //Checks if the button is clicked and also if it wasnt booked to this order already.
            if(button[i].getBackground() == Color.BLUE && button[i].isEnabled() == true) {
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
            flightseat.chooseSeats(flightID, numberOfSeats, flight, methodChecker, customerID);
            }
        }
        //If you have chosen seats, you will be shown how many and which, whereafter it will continue to adding addtional travellers.
        else if(seats > 0) {
            JOptionPane succesDialog = new JOptionPane();
            int succesResult = succesDialog.showConfirmDialog(gui.returnFrame(),"You have chosen: " + seats + " seat(s). Name of chosen seat(s): "+ nameOfSeats,
                                                            "Confirm your choice", succesDialog.OK_CANCEL_OPTION);
            if(succesResult == succesDialog.YES_OPTION) {
                addAdditionalTravellers(seats, nameOfSeats, customerID);
            }
            //If you're not happy with your choice or try to close the window, you're sent back to choosing seats.
            else {
                flightseat.chooseSeats(flightID, numberOfSeats, flight, methodChecker, customerID);
            }
        }
    }
    
    /*
    * This method is used for adding the names of the extra traveller(s) to the existing namestring in the db. Also the seatstring is updated here.
    */
    private void addAdditionalTravellers(int seats, String nameOfSeats, int customerID) {
          
        JPanel cusInputWest = new JPanel(new GridLayout(0,1));
        JPanel cusInputEast = new JPanel(new GridLayout(0,1));
        
        //Creates as many textfields as there are remaining seats.
        JTextField[] additionalTravellersNames = new JTextField[seats];
        for(int j = 0; j<seats; j++) {
            
            JTextField name = new JTextField("Full name");
            additionalTravellersNames[j] = name;
            
            //Adds the textfields to two rows.
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
        int cusResult = inputDialog.showConfirmDialog(gui.returnFrame(), cusInput, "Additional Travellers Names", inputDialog.OK_CANCEL_OPTION);
        if(cusResult == inputDialog.YES_OPTION) {
            
            for(int k = 0; k<additionalTravellersNames.length; k++) {
                if(additionalTravellersNames[k].getText() == "Full name") {
                    JOptionPane errorDialog = new JOptionPane();
                    errorDialog.showMessageDialog(null, "Error! all the inputs were not names!");
                    addAdditionalTravellers(seats, nameOfSeats, customerID);
                }
            }
            try {
                //For each traveller, the traveller's name is added to the string existing names string on the order.
                Order order = new Order(database, customerID);
                Flight flight = new Flight(database, order.getFlight());
                int bookedSeatsTotal = flight.getBookedSeats() + seats;
                String travellerNames = order.getName();
                
                String originalNameOfSeats = order.getSeat();
                nameOfSeats = originalNameOfSeats + nameOfSeats;
            
            for(int k = 0; k<additionalTravellersNames.length; k++) {
               travellerNames = travellerNames+(additionalTravellersNames[k].getText())+", ";
               
               database.execute("UPDATE Orders SET namestring = '" + travellerNames + "' WHERE customerID = " + customerID); 
               database.execute("UPDATE Orders SET seatstring = '" + nameOfSeats + "' WHERE customerID = " + customerID);
               database.execute("UPDATE Flights SET bookedseats = " + bookedSeatsTotal + " WHERE id = " + order.getFlight());
               seatsDialog.dispose();
            }
            
            } catch (SQLException ex) {
                System.out.println("An error occured! Exception: " + ex);
            }
        }
    }
}