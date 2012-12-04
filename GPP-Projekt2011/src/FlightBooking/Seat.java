<<<<<<< HEAD
package FlightBooking;

import java.sql.*;
/**
 *
 * @author Nikolaj
 */
public class Seat {
    private int key, flightid, customerid;
    private String seatName;
    
    public Seat(int f, int o, int c, String sn) {
        key = 0;
        flightid = f;
        customerid = c;
        seatName = sn;
    }
    
    public void init(ResultSet rs) throws SQLException {
        key = rs.getInt("id");
        flightid = rs.getInt("flightid");
        customerid = rs.getInt("customerid");
    }
    
    public Seat(Database db, int k) throws SQLException {
        ResultSet rs = db.execute("SELECT * FROM Seats WHERE id = " + k);
        rs.next();
        init(rs);
    }
    
    public void insert(Database db) throws SQLException {
        db.execute ("INSERT INTO Seats (flightid, customerid) VALUES ('" + flightid + "', '" + customerid + "')");
        ResultSet rs = db.execute("SELECT MAX(id) as 'max' FROM Seats");
        rs.next();
        key = rs.getInt("max");
    }
    
    public int getKey() {
        return key;
    }
    
    public int getFlightid() {
        return flightid;
    }
    
    public int getCustomerid() {
        return customerid;
    }    
=======
package FlightBooking;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
/**
 *
 * @author Mark
 */
public class Seat extends GUI {
    GUI gui;
    
    public Seat(GUI g) {
        gui = g;
    }
    /*
     * Makes a window with all seats on the plain. Booked seats are red, available are green and chosen seats are blue.
     */
    protected void chooseSeats(String flightID, int numberOfSeats) {
        JDialog seatsDialog = new JDialog(gui.frame);
        
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
       final JDialog dialogFinal = seatsDialog;
       
       middle.add(new JButton()).setEnabled(false);
       JButton save = new JButton("Save");
       south.add(save);
       save.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) { saveChosenSeats(buttonFinished, chosenFlight, seats);
                                                                             dialogFinal.dispose();}
                              });
       
       seatsContentPane.add(left, BorderLayout.WEST); 
       seatsContentPane.add(middle, BorderLayout.CENTER);
       seatsContentPane.add(right, BorderLayout.EAST);
       seatsContentPane.add(south, BorderLayout.SOUTH);
       
       seatsDialog.pack();
       seatsDialog.setLocationRelativeTo(gui.frame);
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
            int errorResult = errorDialog.showConfirmDialog(gui.frame,"You did not choose any seats,"
                                                            + " do you wish to go back and select seats again?",
                                                            "No seats selected", errorDialog.OK_CANCEL_OPTION);
            if(errorResult == errorDialog.YES_OPTION) {
                chooseSeats(flightID, numberOfSeats);
            }
        }
        //If you have chosen seats, you will be shown how many and which, whereafter it will continue til creating customers.
        else if(seats > 0) {
            JOptionPane succesDialog = new JOptionPane();
            int succesResult = succesDialog.showConfirmDialog(gui.frame,"You have chosen: " + seats + " seat(s). Name of chosen seat(s): "+ nameOfSeats,
                                                            "Confirm your choice", succesDialog.OK_CANCEL_OPTION);
            if(succesResult == succesDialog.YES_OPTION) {
                gui.createCustomer(seats, nameOfSeats);
            }
            //If you're not happy with your choice or try to close the window, you're sent back to choosing seats.
            else {
                chooseSeats(flightID, numberOfSeats);
            }
        }
    }    
>>>>>>> 26th commit
}