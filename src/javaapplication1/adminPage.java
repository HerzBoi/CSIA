package javaapplication1;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.invoke.MethodHandles;
        
import java.sql.Connection;
import java.sql.DriverManager;

public class adminPage extends JFrame implements ActionListener
{

    Container container = getContentPane();
    
    private JButton addUserButton = new JButton("Add User");
    private JLabel nameLabel = new JLabel("Name:");
    private JLabel usernameLabel = new JLabel("Username:");
    private JLabel passwordLabel = new JLabel("Password:");
    private JTextField nameTextField = new JTextField();
    private JTextField usernameTextField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JButton backButton = new JButton("Back");
    
    public adminPage() 
    {
        // Constructor...
        adminComponents();
        addActionEvent();
    }
    
    public void layoutManager()
    {
        container.setLayout(null);
    }

    private void adminComponents() 
    {
        setTitle("Admin Page");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components to the panel with constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nameTextField.setPreferredSize(new Dimension(200, 30));
        nameTextField.setMinimumSize(new Dimension(200, 30));
        gbc.weightx = 1.0;
        panel.add(nameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameTextField.setPreferredSize(new Dimension(200, 30));
        usernameTextField.setMinimumSize(new Dimension(200, 30));
        gbc.weightx = 1.0;
        gbc.weightx = 1.0;
        panel.add(usernameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordField.setMinimumSize(new Dimension(200, 30));
        gbc.weightx = 1.0;
        panel.add(passwordField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(addUserButton, gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        //gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(backButton, gbc);
        
        // Customize UI appearance
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        addUserButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFont(new Font("Arial", Font.BOLD, 14));

        addUserButton.setBackground(Color.BLUE);
        addUserButton.setForeground(Color.WHITE);
        
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLUE);

        // Set layout manager for the content pane
        getContentPane().setLayout(new GridBagLayout());

        // Add the panel to the content pane with constraints
        GridBagConstraints cpGbc = new GridBagConstraints();
        getContentPane().add(panel, cpGbc);

        //pack();
        setLocationRelativeTo(null);
        
        // Add action listener for the Add User button
        addUserButton.addActionListener(this);

        
    }

    private void addActionEvent() 
    {
        backButton.addActionListener(event -> 
        { 
            back();
        });
    }
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == addUserButton) 
        {
            // Handle the action for the Add User button
            String name = nameTextField.getText();
            String username = usernameTextField.getText();
            String password = new String(passwordField.getPassword());

            // Database connection parameters
            String url = "jdbc:mysql://localhost:3306/cssoftware?zeroDateTimeBehavior=CONVERT_TO_NULL";
            String user = "root";
            String dbPassword = "root";

            try ( Connection connection = DriverManager.getConnection(url, user, dbPassword)) 
            {
                // Insert the new user into the 'users' table (modify table name if needed)
                String insertQuery = "INSERT INTO users (name, userName, tempPass) VALUES (?, ?, ?)";
                try ( PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) 
                {
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, username);
                    preparedStatement.setString(3, password);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "User added successfully to the database");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to add user to the database");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        }
    }
    
    
    
    public static void main(String[] args) 
    {
        // Use SwingUtilities.invokeLater to ensure Swing components are accessed on the EDT
        SwingUtilities.invokeLater(() -> 
        {
            adminPage adminPage = new adminPage();
            adminPage.setVisible(true);
        });

    }
    
    public void back()
        {
            System.out.println("back button pressed");
            
            CsIa back = new CsIa();
            //CsIa.setVisible(true); //ask manali maam
            dispose();
        }
}
