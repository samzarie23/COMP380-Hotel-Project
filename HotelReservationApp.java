import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//CODE TO GET TO SQL DATABASE
/*
String connectionUrl = "jdbc:sqlserver://hotelsjas.database.windows.net:1433;"
            + "database=HummingBirdHotel;"
            + "user=samzarie;"
            + "password=hotelproj123!;"
            + "encrypt=true;"
            + "trustServerCertificate=true;"
            + "logLevel=4;"
            + "loginTimeout=30;";
            
            try (Connection connection = DriverManager.getConnection(connectionUrl)) 
        {
            System.out.println("Connected to the database");
    
            // Create a query to insert a new person into the users table
            String insertQuery = "INSERT INTO users (user_id, username, email, accesslevel) VALUES (?, ?, ?, ?)";
    
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                // Set the values for the parameters
                preparedStatement.setString(1, "101");
                preparedStatement.setString(2, "SamZarieSeyd");
                preparedStatement.setString(3, "Samzarie@gmail.com");
                preparedStatement.setString(4, "super admin");   
                // Execute the insert query
                int rowsAffected = preparedStatement.executeUpdate();
    
                if (rowsAffected > 0) {
                    System.out.println("New person inserted successfully.");
                } else {
                    System.out.println("Failed to insert new person.");
                }
            }
    
            // Your booking logic here
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */


public class HotelReservationApp {
    private JFrame frame;
    private JTextField checkInField;
    private JTextField checkOutField;
    private JTextField howManyField;
    private JTextField confirmationField; // New field to enter confirmation number

    public HotelReservationApp() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Hotel Reservation Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());

        // Set a fixed size for the frame and prevent resizing
        frame.setPreferredSize(new Dimension(1365, 750));
        frame.setResizable(false);

        // Outer Panel with Background Image
        JPanel outerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("C:/Users/samza/Dropbox/SZ_Work/SZ_CSUN/comp380/HawaiiPhoto.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Left Panel for Welcome Message
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(new Color(0, 0, 0, 100)); // Semi-transparent black background
        leftPanel.setPreferredSize(new Dimension(650, 0)); // Set preferred width for left panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

    

        // Welcome Message (Rendered using HTML)
        JEditorPane welcomeLabel = new JEditorPane("text/html", "<html><div style='text-align: center; font-size: 36pt; color: white;'>Welcome to<br>HummingBird Hotel and Resort</div></html>");
        welcomeLabel.setEditable(false);
        welcomeLabel.setBackground(new Color(0, 0, 0, 0));
        leftPanel.add(welcomeLabel, gbc);

        // Right Panel for Reservation Details
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent white background

        // Create custom border for reservation details panel
        Border innerBorder = BorderFactory.createLineBorder(Color.WHITE, 2);
        Border outerBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        Border compoundBorder = BorderFactory.createCompoundBorder(innerBorder, outerBorder);
        rightPanel.setBorder(compoundBorder);

        // Check In Section
        JLabel checkInLabel = new JLabel("Check In Date");
        checkInLabel.setForeground(Color.BLACK);
        checkInLabel.setFont(new Font("Arial", Font.BOLD, 24));
        checkInField = new JTextField(15); // Increased width for the text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(checkInLabel, gbc);
        gbc.gridy = 1;
        rightPanel.add(checkInField, gbc);

        // Check Out Section
        JLabel checkOutLabel = new JLabel("Check Out Date");
        checkOutLabel.setForeground(Color.BLACK);
        checkOutLabel.setFont(new Font("Arial", Font.BOLD, 24));
        checkOutField = new JTextField(15); // Increased width for the text field
        gbc.gridy = 2;
        rightPanel.add(checkOutLabel, gbc);
        gbc.gridy = 3;
        rightPanel.add(checkOutField, gbc);

        // How Many Section
        JLabel howManyLabel = new JLabel("Number of Travelers");
        howManyLabel.setForeground(Color.BLACK);
        howManyLabel.setFont(new Font("Arial", Font.BOLD, 24));
        howManyField = new JTextField(5); // Reduced width for the text field
        gbc.gridy = 4;
        rightPanel.add(howManyLabel, gbc);
        gbc.gridy = 5;
        rightPanel.add(howManyField, gbc);

        // Add some empty space between the "Number of Travelers" section and the "Book Now!" button
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPanel.add(Box.createRigidArea(new Dimension(0, 40)), gbc);

        // Book Now Button
        JButton bookNowButton = new JButton("Book Now!");
        bookNowButton.setBackground(new Color(255, 215, 0));
        bookNowButton.setForeground(Color.BLACK);
        bookNowButton.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        rightPanel.add(bookNowButton, gbc);

        // Add some empty space between the Book Now! button and the "Need to Update An Existing Reservation" section
        gbc.gridy = 8;
        rightPanel.add(Box.createRigidArea(new Dimension(0, 80)), gbc); // Increased spacing

        // Need to Update An Existing Reservation Section
        JLabel updateReservationLabel = new JLabel("Need to Update An Existing Reservation? Enter Confirmation # Here:");
        updateReservationLabel.setForeground(Color.BLACK);
        updateReservationLabel.setFont(new Font("Arial", Font.BOLD, 20));
        confirmationField = new JTextField(15); // Increased width for the text field
        gbc.gridy = 9;
        rightPanel.add(updateReservationLabel, gbc);
        gbc.gridy = 10;
        rightPanel.add(confirmationField, gbc);

        // View Reservation Button
        JButton viewReservationButton = new JButton("View Reservation");
        viewReservationButton.setBackground(new Color(255, 215, 0));
        viewReservationButton.setForeground(Color.BLACK);
        viewReservationButton.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy = 11;
        rightPanel.add(viewReservationButton, gbc);

        // Add action listeners to the buttons
        bookNowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the bookNow method with the input data
                String checkIn = checkInField.getText();
                String checkOut = checkOutField.getText();
                int howMany = Integer.parseInt(howManyField.getText());
                boolean isValidBooking = bookNow(checkIn, checkOut, howMany);
        
                if (isValidBooking == true) {
                    // Close the current GUI window
                    frame.dispose();
                    
                    // Create and show a new GUI window for entering guest information
                    createAndShowBookingFormGUI();
                }
            }
        });
        

        viewReservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the method to view the reservation with the confirmation number
                String confirmationNumber = confirmationField.getText();
                viewReservation(confirmationNumber);
            }
        });

        // Add the left and right panels to the outer panel
        outerPanel.add(leftPanel, BorderLayout.WEST);
        outerPanel.add(rightPanel, BorderLayout.CENTER);

        // Add the outer panel to the frame
        frame.add(outerPanel);

        // Pack the frame to set the preferred size, and then make it visible
        frame.pack();
        frame.setVisible(true);
    }

    private boolean bookNow(String checkIn, String checkOut, int howMany) {
        // Check if the date format is valid (month/day/year)
        if (!isValidDateFormat(checkIn) || !isValidDateFormat(checkOut)) {
            showErrorMessage("Invalid date format. Please use the format: month/day/year");
            return false; // Exit the method
        }
    
        // Check if the number of travelers is within the allowed limit (max 4 people)
        if (howMany > 4) {
            showErrorMessage("Maximum of 4 travelers allowed.");
            return false; // Exit the method
        }

        return true;
    }
    
    // Helper method to check if a date is in valid format (month/day/year)
    private boolean isValidDateFormat(String date) {
        String dateFormatRegex = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\\d{4}$";
        return date.matches(dateFormatRegex);
    }

    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Method to handle viewing an existing reservation
    private void viewReservation(String confirmationNumber) {
        // Implement this method to handle viewing the reservation based on the confirmation number
        // You can use the provided JDBC code from previous responses to interact with the database.
        // Retrieve the reservation details from the database and display them to the user.

    }

    private void createAndShowBookingFormGUI() {
    JFrame bookingFormFrame = new JFrame("Guest Information");
    bookingFormFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    bookingFormFrame.setSize(800, 500);
    bookingFormFrame.setLayout(new BorderLayout());

    // Create panels, labels, text fields, buttons, and add them to the bookingFormFrame
    // ... (Code to create the new GUI for entering guest information)

    // Pack the frame to set the preferred size, and then make it visible
    bookingFormFrame.pack();
    bookingFormFrame.setVisible(true);
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelReservationApp());
    }
}

