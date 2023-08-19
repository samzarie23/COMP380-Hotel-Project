import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.MaskFormatter;
import javax.swing.text.StyledDocument;
import java.util.Date;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;
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
public class HotelReservationApp 
{
    private JFrame frame;
    private JFrame bookingFormFrame;
    private JFormattedTextField checkInField;
    private JFormattedTextField checkOutField;
    private JTextField howManyField;
    private JTextField confirmationField; // New field to enter confirmation number
    private JPanel summaryPanel;
    private JFrame contactPaymentFrame;

    public HotelReservationApp() {
        createAndShowGUI();
    }

    private void createAndShowGUI() 
    {
        frame = new JFrame("Hotel Reservation Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());
        // Set a fixed size for the frame and prevent resizing
        frame.setPreferredSize(new Dimension(1500, 750));
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
        MaskFormatter dateMask = null;
        try {
            dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
        }
        checkInField = new JFormattedTextField(dateMask); // Increased width for the text field
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
        MaskFormatter dateMaskOut = null;
        try {
            dateMaskOut = new MaskFormatter("##/##/####");
            dateMaskOut.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
        }
        checkOutField = new JFormattedTextField(dateMaskOut); // Increased width for the text field
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
                    createAndShowBookingFormGUI(checkIn, checkOut);
                }
            }
        });
        viewReservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve the confirmation number from the text field
                String confirmationNumber = confirmationField.getText();
                // Close the existing GUI
                frame.dispose();
                // Create a new JFrame for the reservation details
                JFrame reservationDetailsFrame = new JFrame("Reservation Details");
                reservationDetailsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                reservationDetailsFrame.setLayout(new BorderLayout());
                reservationDetailsFrame.setPreferredSize(null);
                reservationDetailsFrame.setPreferredSize(new Dimension(1500, 750));
                reservationDetailsFrame.setResizable(false);
                // Create a JPanel for displaying the reservation details
                JPanel detailsPanel = new JPanel();
                detailsPanel.setLayout(new BorderLayout());
                detailsPanel.setBackground(Color.WHITE);
                detailsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
                JTextPane detailsTextPane = new JTextPane();
                detailsTextPane.setFont(new Font("Arial", Font.PLAIN, 14));
                detailsTextPane.setMargin(new Insets(10, 10, 10, 10));
                detailsTextPane.setEditable(false);
                detailsTextPane.setBackground(Color.WHITE);
                StyledDocument detailsDoc = detailsTextPane.getStyledDocument();
                // Retrieve reservation details from the database using the confirmationNumber
                String connectionUrl = "jdbc:sqlserver://hotelsjas.database.windows.net:1433;" + "database=HummingBirdHotel;" + "user=samzarie;" + "password=hotelproj123!;" + "encrypt=true;" + "trustServerCertificate=true;" + "logLevel=4;" + "loginTimeout=30;";
                try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                    // Create a query to retrieve reservation details based on confirmationNumber
                    String retrieveQuery = "SELECT * FROM SZ_Reservations WHERE reservationumber = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(retrieveQuery)) {
                        preparedStatement.setString(1, confirmationNumber);
                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            if (resultSet.next()) {
                                // Retrieve and display reservation details in the detailsTextPane
                                String fullNameFromDb = resultSet.getString("fullname");
                                String emailFromDb = resultSet.getString("email");
                                String phoneNumberFromDb = resultSet.getString("phonenumber");
                                int roomNumberFromDb = resultSet.getInt("roomnumber");
                                String checkInDateFromDb = resultSet.getString("checkindate");
                                String checkOutDateFromDb = resultSet.getString("checkoutdate");
                                detailsDoc.insertString(detailsDoc.getLength(), "Reservation Details:\n", null);
                                detailsDoc.insertString(detailsDoc.getLength(), "Full Name: " + fullNameFromDb + "\n", null);
                                detailsDoc.insertString(detailsDoc.getLength(), "Email: " + emailFromDb + "\n", null);
                                detailsDoc.insertString(detailsDoc.getLength(), "Phone Number: " + phoneNumberFromDb + "\n", null);
                                detailsDoc.insertString(detailsDoc.getLength(), "Room Number: " + roomNumberFromDb + "\n", null);
                                detailsDoc.insertString(detailsDoc.getLength(), "Reservation Number: " + confirmationNumber + "\n", null);
                                detailsDoc.insertString(detailsDoc.getLength(), "Check-In Date: " + checkInDateFromDb + "\n", null);
                                detailsDoc.insertString(detailsDoc.getLength(), "Check-Out Date: " + checkOutDateFromDb + "\n", null);
                                // Create an "Update" button
                                JButton updateButton = new JButton("Update");
                                updateButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        int result = JOptionPane.showConfirmDialog(null, "Do you want to update the reservation? An additional $1000 charge will be made on your card. Do you agree?", "Update Reservation", JOptionPane.YES_NO_OPTION);
                                        if (result == JOptionPane.YES_OPTION) {
                                            String updateQuery = "UPDATE SZ_Reservations SET checkindate = ?, checkoutdate = ? WHERE reservationumber = ?";
                                            try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                                                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                                                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); // Use desired format
                                                    // Ask for new check-in date
                                                    String newCheckInDateString = JOptionPane.showInputDialog(null, "Enter new Check-In Date (MM/dd/yyyy):");
                                                    Date newCheckInDate = dateFormat.parse(newCheckInDateString);
                                                    // Ask for new check-out date
                                                    String newCheckOutDateString = JOptionPane.showInputDialog(null, "Enter new Check-Out Date (MM/dd/yyyy):");
                                                    Date newCheckOutDate = dateFormat.parse(newCheckOutDateString);
                                                    // Set the new dates to the prepared statement
                                                    preparedStatement.setDate(1, new java.sql.Date(newCheckInDate.getTime()));
                                                    preparedStatement.setDate(2, new java.sql.Date(newCheckOutDate.getTime()));
                                                    preparedStatement.setString(3, confirmationNumber);
                                                    int rowsAffected = preparedStatement.executeUpdate();
                                                    if (rowsAffected > 0) {
                                                        JOptionPane.showMessageDialog(null, "Reservation updated successfully. An additional $1000 charge has been made on your card.", "Success", JOptionPane.INFORMATION_MESSAGE);
                                                        // Create a timer to dispose of the frame after 3 seconds
                                                        Timer timer = new Timer(1500, new ActionListener() {
                                                            @Override
                                                            public void actionPerformed(ActionEvent e) {
                                                                reservationDetailsFrame.dispose(); // Close the details frame after canceling
                                                                createAndShowGUI();
                                                            }
                                                        });
                                                        timer.setRepeats(false); // Only execute once
                                                        timer.start();
                                                    } else {
                                                        JOptionPane.showMessageDialog(null, "Failed to update reservation.", "Error", JOptionPane.ERROR_MESSAGE);
                                                    }
                                                }
                                            } catch (SQLException | ParseException ex) {
                                                ex.printStackTrace();
                                                JOptionPane.showMessageDialog(null, "An error occurred while updating the reservation.", "Error", JOptionPane.ERROR_MESSAGE);
                                            }
                                        }
                                    }
                                });
                                // Create a "Cancel" button
                                JButton cancelButton = new JButton("Cancel Reservation");
                                cancelButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        int cancelResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel your reservation? This action cannot be undone.", "Cancel Reservation", JOptionPane.YES_NO_OPTION);
                                        if (cancelResult == JOptionPane.YES_OPTION) {
                                            String deleteQuery = "DELETE FROM SZ_Reservations WHERE reservationumber = ?";
                                            try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                                                try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                                                    deleteStatement.setString(1, confirmationNumber);
                                                    int rowsAffected = deleteStatement.executeUpdate();
                                                    if (rowsAffected > 0) {
                                                        JOptionPane.showMessageDialog(null, "Reservation canceled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                                                        // Create a timer to dispose of the frame after 3 seconds
                                                        Timer timer = new Timer(1500, new ActionListener() {
                                                            @Override
                                                            public void actionPerformed(ActionEvent e) {
                                                                reservationDetailsFrame.dispose(); // Close the details frame after canceling
                                                                createAndShowGUI();
                                                            }
                                                        });
                                                        timer.setRepeats(false); // Only execute once
                                                        timer.start();
                                                    } else {
                                                        JOptionPane.showMessageDialog(null, "Failed to cancel reservation.", "Error", JOptionPane.ERROR_MESSAGE);
                                                    }
                                                }
                                            } catch (SQLException ex) {
                                                ex.printStackTrace();
                                                JOptionPane.showMessageDialog(null, "An error occurred while canceling the reservation.", "Error", JOptionPane.ERROR_MESSAGE);
                                            }
                                        }
                                    }
                                });
                                // Add the "Update" button to the detailsTextPane
                                detailsDoc.insertString(detailsDoc.getLength(), "Click 'Update' to change the reservation.\n", null);
                                detailsPanel.add(detailsTextPane, BorderLayout.CENTER);
                                detailsPanel.add(updateButton, BorderLayout.SOUTH);
                                detailsPanel.add(cancelButton, BorderLayout.EAST);
                                // Add the detailsPanel to the reservationDetailsFrame
                                reservationDetailsFrame.add(detailsPanel, BorderLayout.CENTER);
                                // Display the reservation details frame
                                reservationDetailsFrame.pack();
                                reservationDetailsFrame.setVisible(true);
                            } else {
                                // Confirmation number not found, show error message
                                JOptionPane.showMessageDialog(null, "Confirmation number does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                                createAndShowGUI();
                            }
                        }
                    }
                } catch (SQLException | BadLocationException ex) {
                    ex.printStackTrace();
                }
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


    private void createAndShowBookingFormGUI(String checkin, String checkout) 
    {
        bookingFormFrame = new JFrame("Available Rooms");
        bookingFormFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bookingFormFrame.setSize(1000, 800); // Adjust the size as needed
        bookingFormFrame.setLayout(new BorderLayout());
        bookingFormFrame.setPreferredSize(new Dimension(1365, 750));
        bookingFormFrame.setResizable(false);
        bookingFormFrame.setBackground(Color.BLACK);
        // Create a panel for the left side (rooms list)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.BLACK);
        JPanel roomsPanel = new JPanel();
        roomsPanel.setLayout(new BoxLayout(roomsPanel, BoxLayout.Y_AXIS)); // Vertical layout
        // Create the summaryPanel
        JPanel summaryPanel = new JPanel(); // Initialize the summaryPanel
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(Color.BLACK);
        int numPeople = Integer.parseInt(howManyField.getText());
        // Create room cards based on the number of people
        if (numPeople >= 1 && numPeople <= 4) {
            createRoomCards(roomsPanel, numPeople, checkin, checkout);
        } else {
            JLabel noRoomsLabel = new JLabel("No rooms available for the selected number of people.");
            noRoomsLabel.setFont(new Font("Arial", Font.BOLD, 16));
            roomsPanel.add(noRoomsLabel);
        }
        JScrollPane scrollPane = new JScrollPane(roomsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Increase scroll speed
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        // Create a panel for the right side (summary and book now)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.BLACK);
        summaryPanel.add(Box.createVerticalStrut(20)); // Add some spacing
        rightPanel.add(summaryPanel, BorderLayout.CENTER);
        // Create a panel for the title "Rooms Available" on top
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.BLACK);
        // Create and add the "Back" button to the titlePanel
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOriginalGUI(); // Call the method to show the original GUI
            }
        });
        titlePanel.add(backButton, BorderLayout.WEST); // Align to the left
        // Create a label for the "Rooms Available" text and center it
        JLabel roomsAvailableLabel = new JLabel("Rooms Available");
        roomsAvailableLabel.setFont(new Font("Arial", Font.BOLD, 32));
        roomsAvailableLabel.setForeground(Color.WHITE); // Set text color to white
        roomsAvailableLabel.setHorizontalAlignment(JLabel.CENTER); // Center the text
        titlePanel.add(roomsAvailableLabel, BorderLayout.CENTER);
        // Add the title panel to the bookingFormFrame
        bookingFormFrame.add(titlePanel, BorderLayout.NORTH);
        // Add left and right panels to the bookingFormFrame
        bookingFormFrame.add(leftPanel, BorderLayout.WEST);
        bookingFormFrame.add(rightPanel, BorderLayout.CENTER);
        // Pack the frame to set the preferred size, and then make it visible
        bookingFormFrame.pack();
        bookingFormFrame.setVisible(true);
    }

    private void createRoomCards(JPanel roomsPanel, int numPeople, String checkin, String checkout) 
    {
        int roomNumberBase = (numPeople * 100);
        // Add mock room data based on the number of people
        for (int i = 1; i <= 4; i++) {
            int roomNumber = roomNumberBase + i;
            JPanel roomCard = createRoomCard(i, numPeople, roomNumber, checkin, checkout);
            roomCard.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add black border
            roomsPanel.add(roomCard);
            roomsPanel.add(Box.createVerticalStrut(60)); // Add spacing between room cards
        }
    }

    private JPanel createRoomCard(int roomNumber, int numPeople, int roomNumberOfficial, String checkin, String checkout) 
    {
        JPanel roomCard = new JPanel();
        roomCard.setLayout(new BorderLayout()); // Use BorderLayout for overall layout
        roomCard.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 3)); // Gold border
        roomCard.setPreferredSize(new Dimension(1350, 200)); // Adjust size as needed
        Color textColor = Color.WHITE; // White text color
        JPanel imagePanel = new JPanel(new BorderLayout());
        JLabel imageLabel = new JLabel();
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        roomCard.add(imagePanel, BorderLayout.WEST);
        Timer timer = new Timer(2000, new ActionListener() {
            int imageIndex = 0;
            String[][] imagePaths = {
                {
                    "C:/Users/samza/Dropbox/SZ_Work/SZ_CSUN/comp380/beachroom.jpg",
                    "C:/Users/samza/Dropbox/SZ_Work/SZ_CSUN/comp380/download.jpg",
                    "C:/Users/samza/Dropbox/SZ_Work/SZ_CSUN/comp380/view.jpg"
                    // ... (add more image paths for room 1)
                },
                {
                    "C:/Users/samza/Dropbox/SZ_Work/SZ_CSUN/comp380/download.jpg",
                    "C:/Users/samza/Dropbox/SZ_Work/SZ_CSUN/comp380/classy.jpg",
                    "C:/Users/samza/Dropbox/SZ_Work/SZ_CSUN/comp380/huts.jpg",
                    // ... (add more image paths for room 2)
                },
                {
                    "C:/Users/samza/Dropbox/SZ_Work/SZ_CSUN/comp380/nightsky.jpg",
                    "C:/Users/samza/Dropbox/SZ_Work/SZ_CSUN/comp380/OIP.jpg",
                    "C:/Users/samza/Dropbox/SZ_Work/SZ_CSUN/comp380/outside.jpg",
                    // ... (add more image paths for room 3)
                },
                {
                    "C:/Users/samza/Dropbox/SZ_Work/SZ_CSUN/comp380/roompic.jpg",
                    "C:/Users/samza/Dropbox/SZ_Work/SZ_CSUN/comp380/skyview.jpg",
                    "C:/Users/samza/Dropbox/SZ_Work/SZ_CSUN/comp380/view.jpg",
                    // ... (add more image paths for room 4)
                }
            };
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] currentRoomImages = imagePaths[roomNumber - 1];
                ImageIcon image = new ImageIcon(currentRoomImages[imageIndex]);
                imageLabel.setIcon(image);
                imageIndex = (imageIndex + 1) % currentRoomImages.length;
            }
        });
        timer.start();
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(new Color(0, 0, 0, 150));
        // Randomize room details for demonstration
        String view = getRandomView();
        int squareFeet = (int)(Math.random() * 200) + 200;
        String features = getRandomFeatures();
        JLabel viewLabel = new JLabel("View: " + view);
        addComponent(detailsPanel, viewLabel, 0, 0, 2, 1, GridBagConstraints.LINE_START);
        JLabel sqftLabel = new JLabel("Square Feet: " + squareFeet);
        addComponent(detailsPanel, sqftLabel, 0, 1, 2, 1, GridBagConstraints.LINE_START);
        JLabel featuresLabel = new JLabel("Features: " + features);
        addComponent(detailsPanel, featuresLabel, 0, 2, 2, 1, GridBagConstraints.LINE_START);
        roomCard.add(detailsPanel, BorderLayout.CENTER);
        // Summary Panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(Color.BLACK);
        // Randomize summary details for demonstration
        int price = (int)(Math.random() * 300) + 100;
        String bedsLabel = getBedsLabel(numPeople);
        String bathroomsLabel = getBathroomsLabel(numPeople);
        JLabel priceLabel = new JLabel("Price per Night: $" + price);
        summaryPanel.add(priceLabel);
        JLabel bedsAndBathroomsLabel = new JLabel(bedsLabel + " | " + bathroomsLabel);
        summaryPanel.add(bedsAndBathroomsLabel);
        JTextField roomNumberField = new JTextField();
        roomNumberField.setEditable(false);
        roomNumberField.setBackground(Color.BLACK);
        roomNumberField.setForeground(textColor);
        roomNumberField.setFont(new Font("Arial", Font.BOLD, 18));
        roomNumberField.setText("Room " + roomNumberOfficial);
        summaryPanel.add(roomNumberField);
        viewLabel.setForeground(textColor);
        sqftLabel.setForeground(textColor);
        featuresLabel.setForeground(textColor);
        JButton bookNowButton = new JButton("Book Now");
        bookNowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the booking logic for the selected room
                String roomInfo = "Room " + roomNumber + "\nView: " + viewLabel.getText() + "\n" + sqftLabel.getText() + "\n" + featuresLabel.getText() + "\n" + priceLabel.getText() + "\n" + bedsAndBathroomsLabel.getText();
                boolean isRoomBooked = bookRoom(roomInfo); // Implement this method
                if (isRoomBooked) {
                    // Get the selected check-in and check-out dates, number of travelers, and room number
                    String checkInDate = checkin;
                    String checkOutDate = checkout;
                    int numTravelers = numPeople;
                    int selectedRoomNumber = roomNumberOfficial;
                    String connectionUrl = "jdbc:sqlserver://hotelsjas.database.windows.net:1433;" + "database=HummingBirdHotel;" + "user=samzarie;" + "password=hotelproj123!;" + "encrypt=true;" + "trustServerCertificate=true;" + "logLevel=4;" + "loginTimeout=30;";
                    String query = "SELECT COUNT(*) AS reservationCount FROM SZ_Reservations WHERE roomnumber = ? " + "AND checkindate <= ? AND checkoutdate >= ?";
                    try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                            preparedStatement.setInt(1, roomNumberOfficial);
                            preparedStatement.setString(2, checkout);
                            preparedStatement.setString(3, checkin);
                            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                                if (resultSet.next()) {
                                    int reservationCount = resultSet.getInt("reservationCount");
                                    if (reservationCount > 0) {
                                        JOptionPane.showMessageDialog(null, "Room is sold out for the selected dates.", "Sold Out", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        // Call your method for available rooms here
                                        // For example: createRoomCard(roomNumber, numPeople, roomNumber, checkin, checkout);
                                        // Call the method to create and show the contact/payment information GUI
                                        createAndShowContactPaymentGUI(checkInDate, checkOutDate, numTravelers, selectedRoomNumber, priceLabel.getText());
                                        // Close the current frame
                                        bookingFormFrame.dispose();
                                    }
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to book the room. Please try again.");
                }
            }
        });
        summaryPanel.add(bookNowButton);
        // Price and Beds/Bathrooms labels
        priceLabel.setForeground(textColor);
        bedsAndBathroomsLabel.setForeground(textColor);
        roomCard.add(summaryPanel, BorderLayout.EAST);
        // Add an EmptyBorder to the left side to adjust the position of the text
        int leftPadding = 20; // Adjust the padding as needed
        int topPadding = 10; // Adjust the padding as needed
        int rightPadding = 20; // Adjust the padding as needed
        int bottomPadding = 10; // Adjust the padding as needed
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(topPadding, leftPadding, bottomPadding, rightPadding));
        return roomCard;
    }

    private void addComponent(JPanel panel, JComponent component, int gridx, int gridy, int gridwidth, int gridheight, int anchor) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.anchor = anchor;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(component, gbc);
    }

    private String getRandomView() 
    {
        String[] views = {
            "Ocean View",
            "Mountain View",
            "City View",
            "Garden View"
        };
        int randomIndex = (int)(Math.random() * views.length);
        return views[randomIndex];
    }

    // Helper method to generate random features
    private String getRandomFeatures() 
    {
        String[] features = {
            "Balcony",
            "Mini Bar",
            "Jacuzzi",
            "Kitchenette"
        };
        int numFeatures = (int)(Math.random() * (features.length - 1)) + 1;
        StringBuilder selectedFeatures = new StringBuilder();
        for (int i = 0; i < numFeatures; i++) {
            int randomIndex = (int)(Math.random() * features.length);
            if (i > 0) {
                selectedFeatures.append(", ");
            }
            selectedFeatures.append(features[randomIndex]);
        }
        return selectedFeatures.toString();
    }

    private void showOriginalGUI() {
        bookingFormFrame.dispose(); // Close the booking form GUI
        frame.setVisible(true); // Show the original GUI
    }

    private String getBedsLabel(int numPeople) 
    {
        // Determine and return the beds label based on the number of people
        if (numPeople == 1) {
            return "Beds: 1";
        } else if (numPeople == 2) {
            return "Beds: 2";
        } else if (numPeople == 3) {
            return "Beds: 2";
        } else {
            return "Beds: 4 (Villa)";
        }
    }

    private String getBathroomsLabel(int numPeople)
    {
        // Determine and return the bathrooms label based on the number of people
        if (numPeople == 1) {
            return "Bathrooms: 1";
        } else if (numPeople == 2) {
            return "Bathrooms: 1";
        } else if (numPeople == 3) {
            return "Bathrooms: 2";
        } else {
            return "Bathrooms: 3 (Villa)";
        }
    }

    private boolean bookRoom(String roomInfo) 
    {
        // Implement your booking logic here
        // You can use JDBC code to insert the reservation into the database
        // Return true if booking is successful, false otherwise
        return true; // Placeholder, update as needed
    }

    private void createAndShowContactPaymentGUI(String checkInDate, String checkOutDate, int numTravelers, int roomNumber, String priceLabel) 
    {
        // Create the JFrame
        JFrame contactPaymentFrame = new JFrame("Contact and Payment Information");
        contactPaymentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contactPaymentFrame.setSize(800, 600);
        contactPaymentFrame.setPreferredSize(new Dimension(1365, 750));
        contactPaymentFrame.setLayout(new BorderLayout());
        contactPaymentFrame.getContentPane().setBackground(Color.WHITE);
        // Create a panel for the left side (contact and payment information)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 2)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel contactLabel = new JLabel("Contact and Payment Information");
        contactLabel.setForeground(Color.BLACK);
        contactLabel.setFont(new Font("French Script MT", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        leftPanel.add(contactLabel, gbc);
        JTextField fullNameField = new JTextField(20); // Define the JTextField
        addLabelAndEditableField(leftPanel, gbc, "Full Name:", fullNameField, 1);
        // Add editable text fields for customer information
        JTextField emailFieldField = new JTextField(20); // Define the JTextField
        addLabelAndEditableField(leftPanel, gbc, "Email:", emailFieldField, 2);
        JTextField phoneNumberField = new JTextField(20); // Define the JTextField
        addLabelAndEditableField(leftPanel, gbc, "Phone Number:", phoneNumberField, 3);
        JTextField cardHolderField = new JTextField(20); // Define the JTextField
        // Add editable text fields for payment information
        addLabelAndEditableField(leftPanel, gbc, "Cardholder Name:", cardHolderField, 4);
        JTextField cardNumberField = new JTextField(20); // Define the JTextField
        addLabelAndEditableField(leftPanel, gbc, "Card Number:", cardNumberField, 5);
        JTextField expDateField = new JTextField(20); // Define the JTextField
        addLabelAndEditableField(leftPanel, gbc, "Exp Date:", expDateField, 5);
        JTextField cvvField = new JTextField(20); // Define the JTextFieldaddLabelAndEditableField(leftPanel, gbc, "Expiration Date:", "", 6);
        addLabelAndEditableField(leftPanel, gbc, "CVV:", cvvField, 7);
        // Create a panel for the right side (total breakdown)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GREEN, 2)));
        JLabel reservationDetailsTitle = new JLabel("Reservation Details");
        reservationDetailsTitle.setFont(new Font("Arial", Font.BOLD, 24));
        reservationDetailsTitle.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 7; // Set this to the row just before the first field
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(reservationDetailsTitle, gbc);
        // Add check-in, check-out, travelers, and room number information
        addLabelAndField(rightPanel, gbc, "Check-In Date:", checkInDate, 8);
        addLabelAndField(rightPanel, gbc, "Check-Out Date:", checkOutDate, 9);
        addLabelAndField(rightPanel, gbc, "Number of Travelers:", String.valueOf(numTravelers), 10);
        addLabelAndField(rightPanel, gbc, "Room Number:", String.valueOf(roomNumber), 11);
        int dollarIndex = priceLabel.indexOf("$");
        int pricePerNight = 0;
        // Extract text after "$"
        if (dollarIndex != -1 && dollarIndex < priceLabel.length() - 1) {
            String textAfterDollar = priceLabel.substring(dollarIndex + 1);
            pricePerNight = Integer.parseInt(textAfterDollar); // Replace with the actual price per night
        } else {}
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        // Parse the dates
        LocalDate date1 = LocalDate.parse(checkInDate, formatter);
        LocalDate date2 = LocalDate.parse(checkOutDate, formatter);
        // Calculate the difference in days between the two dates
        long daysBetween = ChronoUnit.DAYS.between(date1, date2);
        int numNights = (int) daysBetween; // Replace with the actual number of nights
        double subtotal = pricePerNight * numNights;
        double taxRate = 0.10; // 10% tax rate
        double taxAmount = subtotal * taxRate;
        double totalCost = subtotal + taxAmount;
        JLabel breakdownLabel = new JLabel("Total Breakdown");
        breakdownLabel.setForeground(Color.BLACK);
        breakdownLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0; // Set this to the top row
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(breakdownLabel, gbc);
        addTotalBreakdownItem(rightPanel, "Subtotal: $", String.format("%.2f", subtotal));
        addTotalBreakdownItem(rightPanel, "Tax (10%): $", String.format("%.2f", taxAmount));
        addTotalBreakdownItem(rightPanel, "Total Cost: $", String.format("%.2f", totalCost));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED, 2)));
        JButton completeBookingButton = new JButton("Complete Booking");
        buttonPanel.add(completeBookingButton);
        completeBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve data from text fields
                String fullName = fullNameField.getText();
                String email = emailFieldField.getText();
                String phoneNumber = phoneNumberField.getText();
                String cardholderName = cardHolderField.getText();
                String cardNumber = cardNumberField.getText();
                String expirationDate = expDateField.getText();
                String cvv = cvvField.getText();
                String reservationNumber = Integer.toString(ThreadLocalRandom.current().nextInt(100000, 1000000000 + 1));
                // Insert reservation into the database
                String connectionUrl = "jdbc:sqlserver://hotelsjas.database.windows.net:1433;" + "database=HummingBirdHotel;" + "user=samzarie;" + "password=hotelproj123!;" + "encrypt=true;" + "trustServerCertificate=true;" + "logLevel=4;" + "loginTimeout=30;";
                try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                    System.out.println("Connected to the database");
                    String insertQuery = "INSERT INTO SZ_Reservations (fullname, email, phonenumber, cardholdername, cardnumber, expdate, cvv, reservationumber, checkindate, checkoutdate,numtravelers, roomnumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                        preparedStatement.setString(1, fullName);
                        preparedStatement.setString(2, email);
                        preparedStatement.setString(3, phoneNumber);
                        preparedStatement.setString(4, cardholderName);
                        preparedStatement.setString(5, cardNumber);
                        preparedStatement.setString(6, expirationDate);
                        preparedStatement.setString(7, cvv);
                        preparedStatement.setString(8, reservationNumber);
                        preparedStatement.setString(9, checkInDate);
                        preparedStatement.setString(10, checkOutDate);
                        preparedStatement.setInt(11, numTravelers);
                        preparedStatement.setInt(12, roomNumber);
                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("New reservation inserted successfully.");
                            JOptionPane.showMessageDialog(null, "Room booked successfully!");
                            // Close the current GUI
                            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(buttonPanel);
                            currentFrame.dispose();
                            // Create a new JFrame for the confirmation page
                            JFrame confirmationFrame = new JFrame("Booking Confirmation");
                            confirmationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            confirmationFrame.setLayout(new BorderLayout());
                            confirmationFrame.setSize(800, 600);
                            confirmationFrame.setPreferredSize(new Dimension(1365, 750));
                            // Create a JPanel for the top section
                            JPanel topPanel = new JPanel();
                            topPanel.setBackground(Color.WHITE);
                            topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                            // Create a bigger and bold font for the congratulations message
                            Font congratsFont = new Font("Arial", Font.BOLD, 24);
                            JLabel congratsLabel = new JLabel("Congratulations! Your Room Has Been Reserved");
                            congratsLabel.setFont(congratsFont);
                            topPanel.add(congratsLabel);
                            // Load and add background image
                            ImageIcon backgroundImage = new ImageIcon("C:/Users/samza/Dropbox/SZ_Work/SZ_CSUN/comp380/download.jpg"); // Change to your image path
                            JLabel backgroundLabel = new JLabel(backgroundImage);
                            backgroundLabel.setLayout(new BorderLayout());
                            // Add the top panel to the background label
                            backgroundLabel.add(topPanel, BorderLayout.NORTH);
                            // Create a JPanel for the main content
                            JPanel contentPanel = new JPanel();
                            contentPanel.setLayout(new GridLayout(1, 2));
                            // Retrieve reservation details from the database using the reservationNumber
                            String retrieveQuery = "SELECT * FROM SZ_Reservations WHERE reservationumber = ?";
                            try (PreparedStatement retrieveStatement = connection.prepareStatement(retrieveQuery)) {
                                retrieveStatement.setString(1, reservationNumber);
                                try (ResultSet resultSet = retrieveStatement.executeQuery()) {
                                    if (resultSet.next()) {
                                        // Create a JPanel for displaying reservation details
                                        JPanel detailsPanel = new JPanel();
                                        detailsPanel.setLayout(new BorderLayout());
                                        detailsPanel.setBackground(Color.WHITE);
                                        detailsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4)); // Add a thick border
                                        String fullNameFromDb = resultSet.getString("fullname");
                                        String emailFromDb = resultSet.getString("email");
                                        String phoneNumberFromDb = resultSet.getString("phonenumber");
                                        int roomNumberFromDb = resultSet.getInt("roomnumber");
                                        JTextArea detailsTextArea = new JTextArea();
                                        detailsTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
                                        detailsTextArea.setMargin(new Insets(10, 10, 10, 10));
                                        detailsTextArea.setEditable(false);
                                        // Enable text wrapping
                                        detailsTextArea.setLineWrap(true); // Enable line wrap
                                        detailsTextArea.setWrapStyleWord(true); // Enable word wrap
                                        detailsTextArea.append("Reservation Details:\n");
                                        detailsTextArea.append("Full Name: " + fullNameFromDb + "\n");
                                        detailsTextArea.append("Email: " + emailFromDb + "\n");
                                        detailsTextArea.append("Phone Number: " + phoneNumberFromDb + "\n");
                                        detailsTextArea.append("Room Number: " + roomNumberFromDb + "\n");
                                        // Apply bold and gold styling to the reservation number
                                        Color goldColor = new Color(255, 215, 0);
                                        // Apply bold and gold styling to the reservation number
                                        detailsTextArea.append("Reservation Number: ");
                                        detailsTextArea.setForeground(goldColor); // Use the custom gold color
                                        detailsTextArea.setFont(detailsTextArea.getFont().deriveFont(Font.BOLD));
                                        detailsTextArea.append(reservationNumber + "\n");
                                        detailsPanel.add(detailsTextArea, BorderLayout.CENTER);
                                        contentPanel.add(detailsPanel);
                                        // Create a JPanel for displaying images with overlay
                                        JPanel imagePanel = new JPanel();
                                        imagePanel.setLayout(new GridLayout(2, 1));
                                        // Load and add images with overlay
                                        ImageIcon image1 = new ImageIcon("C:/Users/samza/Dropbox/SZ_Work/SZ_CSUN/comp380/download.jpg"); // Change to your image path
                                        ImageIcon image2 = new ImageIcon("C:/Users/samza/Dropbox/SZ_Work/SZ_CSUN/comp380/classy.jpg"); // Change to your image path
                                        JLabel imageLabel1 = new JLabel(image1);
                                        JLabel imageLabel2 = new JLabel(image2);
                                        JPanel overlayPanel = new JPanel();
                                        overlayPanel.setBackground(new Color(0, 0, 0, 100)); // Dark overlay color
                                        overlayPanel.setLayout(new BorderLayout());
                                        JLabel overlayLabel = new JLabel("A Special Thank You!");
                                        overlayLabel.setForeground(Color.WHITE);
                                        overlayLabel.setHorizontalAlignment(JLabel.CENTER);
                                        overlayLabel.setVerticalAlignment(JLabel.CENTER);
                                        overlayPanel.add(overlayLabel);
                                        // Create a JPanel for the check-in details
                                        JPanel checkInPanel = new JPanel();
                                        checkInPanel.setLayout(new BorderLayout());
                                        checkInPanel.setBackground(Color.WHITE);
                                        checkInPanel.setBorder(BorderFactory.createTitledBorder("Check-In Details"));
                                        // Create a JTextPane for the check-in details
                                        JTextPane checkInTextPane = new JTextPane();
                                        checkInTextPane.setFont(new Font("Arial", Font.PLAIN, 14));
                                        checkInTextPane.setMargin(new Insets(10, 10, 10, 10));
                                        checkInTextPane.setEditable(false);
                                        checkInTextPane.setBackground(Color.WHITE);
                                        StyledDocument checkInDoc = checkInTextPane.getStyledDocument();
                                        try {
                                            checkInDoc.insertString(checkInDoc.getLength(), "Check-In Details:\n", null);
                                            checkInDoc.insertString(checkInDoc.getLength(), "Gate Code: " + generateRandomCode() + "\n", null);
                                            checkInDoc.insertString(checkInDoc.getLength(), "Pool Code: " + generateRandomCode(), null);
                                            // ... (continue adding other details)
                                        } catch (BadLocationException example) {
                                            example.printStackTrace();
                                        }
                                        // Add the check-in text pane to the check-in panel
                                        checkInPanel.add(checkInTextPane, BorderLayout.CENTER);
                                        // Add the check-in panel to the content panel
                                        contentPanel.add(checkInPanel);
                                        // Create a JPanel for local attractions
                                        JPanel attractionsPanel = new JPanel();
                                        attractionsPanel.setLayout(new BorderLayout());
                                        attractionsPanel.setBackground(Color.WHITE);
                                        attractionsPanel.setBorder(BorderFactory.createTitledBorder("Local Attractions"));
                                        // Generate random local attraction ideas (you can modify this)
                                        String[] localAttractions = {
                                            "Explore the historic downtown area and its charming boutiques.",
                                            "Visit the scenic botanical gardens for a leisurely stroll.",
                                            "Experience local cuisine at renowned restaurants nearby.",
                                            "Discover cultural landmarks and museums showcasing the city's history.",
                                            "Enjoy outdoor activities like hiking, biking, and water sports at the nearby parks."
                                        };
                                        // Create a JTextArea to display local attractions
                                        JTextArea attractionsTextArea = new JTextArea();
                                        attractionsTextArea.setBackground(Color.WHITE);
                                        attractionsTextArea.setEditable(false);
                                        attractionsTextArea.setWrapStyleWord(true);
                                        attractionsTextArea.setLineWrap(true);
                                        attractionsTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
                                        // Select a random attraction from the array
                                        String randomAttraction = localAttractions[ThreadLocalRandom.current().nextInt(localAttractions.length)];
                                        attractionsTextArea.setText(randomAttraction);
                                        // Add the attractions text area to the attractions panel
                                        attractionsPanel.add(attractionsTextArea, BorderLayout.CENTER);
                                        // Add the attractions panel to the content panel
                                        contentPanel.add(attractionsPanel);
                                        // Create a JPanel for hotel policies
                                        JPanel policiesPanel = new JPanel();
                                        policiesPanel.setLayout(new BorderLayout());
                                        policiesPanel.setBackground(Color.WHITE);
                                        policiesPanel.setBorder(BorderFactory.createTitledBorder("Hotel Policies"));
                                        // Create a JTextArea to display hotel policies
                                        JTextArea policiesTextArea = new JTextArea();
                                        policiesTextArea.setBackground(Color.WHITE);
                                        policiesTextArea.setEditable(false);
                                        policiesTextArea.setWrapStyleWord(true);
                                        policiesTextArea.setLineWrap(true);
                                        policiesTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
                                        // Add sample hotel policies (you can modify this)
                                        String hotelPolicies = "Check-In Time: 3:00 PM\n" + "Check-Out Time: 11:00 AM\n" + "Cancellation Policy: 24 hours prior to check-in date\n" + "No smoking allowed in rooms\n" + "Pets are not allowed\n" + "Contact front desk for any assistance";
                                        policiesTextArea.setText(hotelPolicies);
                                        // Add the policies text area to the policies panel
                                        policiesPanel.add(policiesTextArea, BorderLayout.CENTER);
                                        // Add the policies panel to the content panel
                                        contentPanel.add(policiesPanel);
                                        // Create a "Review Reservation" button
                                        JButton reviewReservationButton = new JButton("Review Reservation");
                                        // Style the button
                                        reviewReservationButton.setBackground(Color.WHITE);
                                        reviewReservationButton.setForeground(Color.BLACK);
                                        reviewReservationButton.setFont(new Font("Arial", Font.PLAIN, 12));
                                        // Add an ActionListener to the button
                                        reviewReservationButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                // Close the current confirmation frame
                                                confirmationFrame.dispose();
                                                createAndShowGUI();
                                            }
                                        });
                                        // Create a JPanel for the button
                                        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                                        buttonPanel.add(reviewReservationButton);
                                        // Add the button panel to the bottom of the content panel
                                        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
                                        imageLabel1.setLayout(new BorderLayout());
                                        imageLabel1.add(overlayPanel, BorderLayout.CENTER);
                                        imagePanel.add(imageLabel1);
                                        imagePanel.add(imageLabel2);
                                        contentPanel.add(imagePanel);
                                        backgroundLabel.add(contentPanel, BorderLayout.CENTER);
                                        confirmationFrame.add(backgroundLabel);
                                        confirmationFrame.pack();
                                        confirmationFrame.setVisible(true);
                                    }
                                }
                            }
                        } else {
                            System.out.println("Failed to insert new reservation.");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        // Add the left and right panels to the contactPaymentFrame
        contactPaymentFrame.add(leftPanel, BorderLayout.WEST);
        contactPaymentFrame.add(rightPanel, BorderLayout.CENTER);
        contactPaymentFrame.add(buttonPanel, BorderLayout.SOUTH);
        // Pack the frame to set the preferred size, and then make it visible
        contactPaymentFrame.pack();
        contactPaymentFrame.setVisible(true);
    }

    private String generateRandomCode()
    {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int digit = ThreadLocalRandom.current().nextInt(10);
            code.append(digit);
        }
        return code.toString();
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, String fieldValue, int gridY) 
    {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField field = new JTextField(fieldValue, 20);
        field.setEditable(false);
        JPanel labelFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelFieldPanel.add(label);
        labelFieldPanel.add(field);
        gbc.gridy = gridY;
        panel.add(labelFieldPanel, gbc);
    }

    private void addTotalBreakdownItem(JPanel panel, String labelText, String valueText) 
    {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel value = new JLabel(valueText);
        value.setFont(new Font("Arial", Font.PLAIN, 16));
        itemPanel.add(label);
        itemPanel.add(value);
        panel.add(itemPanel);
    }

    private void addLabelAndEditableField(JPanel panel, GridBagConstraints gbc, String labelText, JTextField field, int gridy) 
    {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        JPanel labelFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelFieldPanel.add(label);
        labelFieldPanel.add(field);
        gbc.gridy = gridy;
        panel.add(labelFieldPanel, gbc);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelReservationApp());
    }
}