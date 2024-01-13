package javaapplication1;

import javax.swing.SwingUtilities;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.ResultSet;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
        
import java.sql.Connection;
import java.sql.DriverManager;


public class homePage extends JFrame implements ActionListener
{
    Container container = getContentPane();
    
    private JLabel nameLabel = new JLabel("Name:");
    private JLabel usernameLabel = new JLabel("Username:");
    private JLabel passwordLabel = new JLabel("Password:");
    private JLabel nameLabelValue = new JLabel();
    private JLabel usernameLabelValue = new JLabel();
    private JLabel passwordLabelValue = new JLabel();
    private JButton changePasswordButton = new JButton("Change Password");
    private JButton logoutButton = new JButton("Logout");
    
    private JPanel topBarPanel = new JPanel(new BorderLayout());
    private JLabel greetingLabel = new JLabel();
    private JButton menuButton = new JButton("...");

    private JPanel sidePanel = new JPanel(new GridLayout(3, 1));
    private JButton profileButton = new JButton("Profile Page");
    private JButton timetableButton = new JButton("Timetable");
    private JButton announcementsButton = new JButton("Announcements");

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    private void initComponents(String username) {
        setTitle("Home Page");
        setSize(600, 400);
        //setDefaultClose Operation(JFrame.EXIT_ON_CLOSE);
        
        // Top Bar
        greetingLabel.setText("Hi, " + username);
        topBarPanel.add(greetingLabel, BorderLayout.WEST);
        topBarPanel.add(menuButton, BorderLayout.EAST);

        // Side Panel
        sidePanel.add(profileButton);
        sidePanel.add(timetableButton);
        sidePanel.add(announcementsButton);

        // Main Panel
        mainPanel.add(createProfilePanel(), "profile");
        mainPanel.add(createTimetablePanel(), "timetable");
        mainPanel.add(createAnnouncementsPanel(), "announcements");

        // Container Layout
        container.setLayout(new BorderLayout());
        container.add(topBarPanel, BorderLayout.NORTH);
        container.add(mainPanel, BorderLayout.CENTER);

        // Add action listeners
        changePasswordButton.addActionListener(this);
        logoutButton.addActionListener(this);
        menuButton.addActionListener(this);
        profileButton.addActionListener(this);
        timetableButton.addActionListener(this);
        announcementsButton.addActionListener(this);
        
        
        
        /*
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components to the panel with constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(nameLabelValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameLabelValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordLabelValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(changePasswordButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(logoutButton, gbc);

        // Customize UI appearance
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabelValue.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabelValue.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabelValue.setFont(new Font("Arial", Font.PLAIN, 14));
        changePasswordButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));

        changePasswordButton.setBackground(Color.BLUE);
        changePasswordButton.setForeground(Color.WHITE);

        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);

        // Set layout manager for the content pane
        getContentPane().setLayout(new BorderLayout());

        // Add the panel to the content pane with constraints
        getContentPane().add(panel, BorderLayout.CENTER);
        

        // Add action listeners
        changePasswordButton.addActionListener(this);
        logoutButton.addActionListener(this);
        */
    }

    private JPanel createProfilePanel() {
        JPanel profilePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components to the profile panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        profilePanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        profilePanel.add(nameLabelValue, gbc);

        // Repeat similar layout for other components...
        return profilePanel;
    }
    
    private JPanel createTimetablePanel() {
        // Create and return the timetable panel with its components
        return new JPanel();
    }
    
    private JPanel createAnnouncementsPanel() {
        // Create and return the announcements panel with its components
        return new JPanel();
    }
    
    homePage(String username) 
    {
        initComponents(username);
        loadUserData(username);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Ensure the application is terminated when the window is closed
                System.exit(0);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == changePasswordButton) {
            // Implement logic for changing password (e.g., show a dialog)
            String newPassword = JOptionPane.showInputDialog(this, "Enter new password:");
            if (newPassword != null) {
                // Update the password in the database (modify the update query based on your schema)
                updatePassword(newPassword);
            }
        } else if (e.getSource() == logoutButton) {
            dispose();
            CsIa loginPage = new CsIa();
            loginPage.setVisible(true);
            setSize(1000, 1000);   
        }
        else if (e.getSource() == menuButton) {
            showSidePanel();
        } else if (e.getSource() == profileButton) {
            cardLayout.show(mainPanel, "profile");
        } else if (e.getSource() == timetableButton) {
            cardLayout.show(mainPanel, "timetable");
        } else if (e.getSource() == announcementsButton) {
            cardLayout.show(mainPanel, "announcements");
        }
    }
    
    private void showSidePanel() {
        int option = JOptionPane.showOptionDialog(
                this,
                sidePanel,
                "Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{},
                null);

        if (option == 0) {
            // Handle menu option if needed
        }
    }
    
    public void layoutManager() 
    {
        container.setLayout(null);
    }

    private void loadUserData(String username) {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/cssoftware?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String dbPassword = "root";

        try ( Connection connection = DriverManager.getConnection(url, user, dbPassword)) 
        {
            String query = "SELECT name, userName, tempPass FROM users WHERE username = ?";
            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) 
            {
                preparedStatement.setString(1, username);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String retrievedUsername = resultSet.getString("userName");
                    String retrievedPassword = resultSet.getString("tempPass");

                    nameLabelValue.setText(name);
                    usernameLabelValue.setText(retrievedUsername);
                    passwordLabelValue.setText(retrievedPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    private void updatePassword(String newPassword) {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/cssoftware?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String dbPassword = "root";

        try ( Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            String updateQuery = "UPDATE users SET tempPass = ? WHERE username = ?";
            try ( PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, newPassword);
                preparedStatement.setString(2, usernameLabelValue.getText());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Password updated successfully");
                    passwordLabelValue.setText(newPassword);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update password");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
    
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> {
            // Pass the username to the constructor when creating the home page
            homePage homePage = new homePage("username");
            homePage.setVisible(true);
        });
    }
}




