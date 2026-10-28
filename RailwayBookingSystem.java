import javax.swing.*;
import java.io.*;
import java.util.*;

// Interface
interface Ticket {
    String getType();
}

// General Ticket
class General implements Ticket {
    public String getType() {
        return "General";
    }
}

// Sleeper Ticket
class Sleeper implements Ticket {
    public String getType() {
        return "Sleeper";
    }
}

// Passenger Class
class Passenger {
    int id;
    String name;
    int age;

    Passenger(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}

// Train Class
class Train {
    int seats = 10;


    synchronized boolean bookSeat() {
        if (seats > 0) {
            seats--;
            return true;
        }
        return false;
    }

    synchronized void cancelSeat() {
        if (seats < 10) {
            seats++;
        }
    }
}

// Main Class
public class RailwayBookingSystem extends JFrame {

    JTextField idField;
    JTextField nameField;
    JTextField ageField;

    JComboBox<String> ticketBox;
    JTextArea output;

    Train train = new Train();

    // Collections
    ArrayList<Passenger> passengers =
            new ArrayList<Passenger>();

    HashMap<Integer, Passenger> records =
            new HashMap<Integer, Passenger>();

    RailwayBookingSystem() {

        setTitle("Railway Ticket Booking System");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new java.awt.FlowLayout());


        add(new JLabel("Passenger ID"));
        idField = new JTextField(15);
        add(idField);

        
        add(new JLabel("Name"));
        nameField = new JTextField(15);
        add(nameField);

       
        add(new JLabel("Age"));
        ageField = new JTextField(15);
        add(ageField);

        
        add(new JLabel("Ticket Type"));

        ticketBox = new JComboBox<String>();
        ticketBox.addItem("General");
        ticketBox.addItem("Sleeper");
        add(ticketBox);

        // Buttons
        JButton bookButton =
                new JButton("Book Ticket");

        JButton cancelButton =
                new JButton("Cancel Ticket");

        JButton seatButton =
                new JButton("Check Seats");

        add(bookButton);
        add(cancelButton);
        add(seatButton);

        
        output = new JTextArea(15, 35);
        add(new JScrollPane(output));

        
        bookButton.addActionListener(e -> bookTicket());

       
        cancelButton.addActionListener(e -> cancelTicket());

        
        seatButton.addActionListener(e -> {

            output.append(
                    "\nAvailable Seats: "
                    + train.seats + "\n"
            );
        });

        setVisible(true);
    }

    // Book Ticket
    void bookTicket() {

        try {

            int id =
                    Integer.parseInt(idField.getText());

            String name =
                    nameField.getText();

            int age =
                    Integer.parseInt(ageField.getText());

            // Check duplicate ID
            if (records.containsKey(id)) {

                output.append(
                        "\nID already exists!"
                        + " Please use another ID.\n"
                );

                return;
            }

            // Check invalid details
            if (name.equals("") || age <= 0) {

                output.append(
                        "\nInvalid Passenger Details!\n"
                );

                return;
            }

            String ticket =
                    ticketBox.getSelectedItem().toString();

            Passenger p =
                    new Passenger(id, name, age);

            // Create Thread
            Thread t =
                    new Thread(new Runnable() {

                public void run() {

                    // Book seat
                    if (train.bookSeat()) {

                        passengers.add(p);
                        records.put(id, p);

                        String data =
                                "Passenger ID: " + id +
                                "\nName: " + name +
                                "\nAge: " + age +
                                "\nTicket: " + ticket +
                                "\n------------------------\n";

                        output.append(
                                "\nTicket Booked Successfully!\n"
                                + data
                        );

                        // Save to file
                        try {

                            FileWriter file =
                                    new FileWriter(
                                            "bookings.txt",
                                            true
                                    );

                            file.write(data);

                            file.close();

                        } catch (IOException ex) {

                            output.append(
                                    "\nFile Error!\n"
                            );
                        }

                    } else {

                        output.append(
                                "\nNo Seats Available!\n"
                        );
                    }
                }
            });

            t.start();

        } catch (NumberFormatException e) {

            output.append(
                    "\nPlease enter valid ID and Age!\n"
            );
        }
    }

    // Cancel Ticket
    void cancelTicket() {

        try {

            int id =
                    Integer.parseInt(idField.getText());

            // Check ID
            if (records.containsKey(id)) {

                records.remove(id);

                train.cancelSeat();

                output.append(
                        "\nTicket Cancelled Successfully!\n"
                        + "Available Seats: "
                        + train.seats + "\n"
                );

            } else {

                output.append(
                        "\nPassenger ID not found!\n"
                );
            }

        } catch (NumberFormatException e) {

            output.append(
                    "\nEnter a valid Passenger ID!\n"
            );
        }
    }

    // Main Method
    public static void main(String[] args) {

        new RailwayBookingSystem();
    }
}