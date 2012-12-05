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
    public JDialog chooseSeats(int flightID, int numberOfSeats, int methodChecker, int customerID) {
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
            SaveActionListener SAC = new SaveActionListener(buttonFinished, chosenFlight, seats, finalMethodChecker, finalCustomerID);
            save.addActionListener(SAC);
            
            seatsContentPane.add(left, BorderLayout.WEST); 
            seatsContentPane.add(middle, BorderLayout.CENTER);
            seatsContentPane.add(right, BorderLayout.EAST);
            seatsContentPane.add(south, BorderLayout.SOUTH);
            
            if(methodChecker == 1 || customerID != 0) {
                ArrayList<String>customersBookedSeats = new ArrayList<String>();
                
                //Finds all the booked seats attached to the order that is to be changed.
                ResultSet rsBookedSeats = database.execute("SELECT seatstring FROM Orders WHERE customerid = " + customerID);
                if(rsBookedSeats.next()) {
                customersBookedSeats = (flightScanner.seatAnalyser(rsBookedSeats.getString("seatstring")));
                JButton[] chosenButtons = new JButton[customersBookedSeats.size()];
                
                for(int a = 0; a<customersBookedSeats.size(); a++) {
                    for(int b = 0; b <button.length; b++) {
                        //Makes the booked seats for the order Blue and clickable again. Also removes the ActionListener from the save button.
                        if((button[b].getText()).equals(customersBookedSeats.get(a))) {
                            button[b].setBackground(Color.BLUE);
                            button[b].setEnabled(true);
                            save.removeActionListener(SAC);
                            chosenButtons[b] = button[b];
                        }
                    }
                }
                
                Object[] options = {"Add Additional Seat(s)", "Delete Seat(s)", "Move Current Seats"};
                            int optionResult = JOptionPane.showOptionDialog(gui.returnFrame(),new JPanel(),"What do you wish to do?",
                                                                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                                                                            null, options, options[0]);
                            //If you wanna add more seats to the order.
                            if(optionResult == JOptionPane.YES_OPTION) {
                                for(int c = 0; c<chosenButtons.length; c++) {
                                    chosenButtons[c].setEnabled(false);
                                    
                                }
                                AddSeatsActionListener ASAC = new AddSeatsActionListener();
                                save.addActionListener(ASAC);
                                makeDialog(seatsDialog);
                                return seatsDialog;
                            }
                            if(optionResult == JOptionPane.NO_OPTION) {
                                for(int c = 0; c<button.length; c++) {
                                    if(button[c].getBackground() == Color.GREEN) {
                                        button[c].setEnabled(false);
                                    }
                                }
                                DeleteSeatsActionListener DSAC = new DeleteSeatsActionListener();
                                save.addActionListener(DSAC);
                                makeDialog(seatsDialog);
                                return seatsDialog;
                            }
                            if(optionResult == JOptionPane.CANCEL_OPTION) {
                                MoveSeatsActionListener MSAC = new MoveSeatsActionListener();
                                save.addActionListener(MSAC);
                                makeDialog(seatsDialog);
                                return seatsDialog;
                            }
                            else {

                            }
                }
            }
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
    
    
    private void saveChosenSeats(JButton[] button, int flightID, int numberOfSeats, int methodChecker, int customerID) {
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
                chooseSeats(flightID, numberOfSeats, methodChecker, customerID);
            }
        }
        //If you have chosen seats, you will be shown how many and which, whereafter it will continue til creating customers.
        else if(seats > 0) {
            JOptionPane succesDialog = new JOptionPane();
            int succesResult = succesDialog.showConfirmDialog(gui.returnFrame(),"You have chosen: " + seats + " seat(s). Name of chosen seat(s): "+ nameOfSeats,
                                                            "Confirm your choice", succesDialog.OK_CANCEL_OPTION);
            if(succesResult == succesDialog.YES_OPTION) {
                gui.createCustomer(seats, nameOfSeats);
            }
            //If you're not happy with your choice or try to close the window, you're sent back to choosing seats.
            else {
                chooseSeats(flightID, numberOfSeats, methodChecker, customerID);
            }
        }
    }
    
    
    
    
    
    
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
            chooseSeats(flightID, numberOfSeats, methodChecker, customerID);
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
                chooseSeats(flightID, numberOfSeats, methodChecker, customerID);
            }
        }
    }
    
    
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
                String travellerNames = order.getName();
                String originalNameOfSeats = order.getSeat();
                nameOfSeats = originalNameOfSeats + nameOfSeats;
            
            for(int k = 0; k<additionalTravellersNames.length; k++) {
               travellerNames = travellerNames+(additionalTravellersNames[k].getText())+", ";
               database.execute("UPDATE Ordes WHERE customerid = " + customerID + " SET namestring = " + travellerNames + 
                                " AND UPDATE Ordes WHERE customerid = " + customerID + " SET seatstring = " + nameOfSeats);
            }
            
            } catch (SQLException ex) {
                System.out.println("An error occured! Exception: " + ex);
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
    
    private class SaveActionListener implements ActionListener {
        
        private JButton[] buttonFinished;
        private int chosenFlight;
        private int seats;
        private int finalMethodChecker;
        private int finalCustomerID;
        
        public SaveActionListener(JButton[] buttonFinished, int chosenFlight, int seats, int finalMethodChecker, int finalCustomerID) {
            this.buttonFinished = buttonFinished;
            this.chosenFlight = chosenFlight;
            this.seats = seats;
            this.finalMethodChecker = finalMethodChecker;
            this.finalCustomerID = finalCustomerID;
        }
        public void actionPerformed(ActionEvent e) {
            saveChosenSeats(buttonFinished, chosenFlight, seats, finalMethodChecker, finalCustomerID);
        }
    }
    
    
    private class AddSeatsActionListener implements ActionListener {
         
        private JButton[] buttonFinished;
        private int chosenFlight;
        private int seats;
        private int finalMethodChecker;
        private int finalCustomerID;
        
        public void AddSeatsActionListener(JButton[] buttonFinished, int chosenFlight, int seats, int finalMethodChecker, int finalCustomerID) {
            this.buttonFinished = buttonFinished;
            this.chosenFlight = chosenFlight;
            this.seats = seats;
            this.finalMethodChecker = finalMethodChecker;
            this.finalCustomerID = finalCustomerID; 
        }
         
         public void actionPerformed(ActionEvent e) {   
             saveAdditionalSeats(buttonFinished, chosenFlight, seats, finalMethodChecker, finalCustomerID);
         }
    }
    
    
    private class DeleteSeatsActionListener implements ActionListener {
         public void DeleteSeatsActionListener() {
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
    
    
    private class MoveSeatsActionListener implements ActionListener {
         public void MoveSeatsActionListener() {
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



