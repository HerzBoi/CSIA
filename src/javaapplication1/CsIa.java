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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
        
import java.sql.Connection;
import java.sql.DriverManager;


public class CsIa extends javax.swing.JFrame implements ActionListener
{
    
    Container container = getContentPane();
    
    //buttons and and text fields
    JLabel userLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JButton adminButton = new JButton("admin?");
   
    public void openHomePage(String userName) {
        // Open the home page with the logged-in username
        homePage homePage = new homePage(userName);
        homePage.setVisible(true);
        this.dispose(); 
    }
    
    //contructor class
    public CsIa() 
    {
        layoutManager();
        addActionEvent();
        secComponents();
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Ensure the application is terminated when the window is closed
                System.exit(0);
            }
        });
    }
    
    public void layoutManager()
    {
        container.setLayout(null);
    }
    
    public void secComponents()
    {
        /*
                setTitle("Login");

        // Create panel and set layout manager
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components to the panel with constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userTextField.setPreferredSize(new Dimension(200, 30));
        gbc.weightx = 1.0; 
        panel.add(userTextField, gbc);

        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;                                  old and ugly 
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);
        
        // Set layout manager for the content pane
        getContentPane().setLayout(new GridBagLayout());

        // Add the panel to the content pane with constraints
        GridBagConstraints cpGbc = new GridBagConstraints();
        getContentPane().add(panel, cpGbc);

        pack();  // Pack components to optimize size
        setLocationRelativeTo(null);  // Center the frame on the screen
        
        */
        
        setTitle("Login");

        // Create panel and set layout manager
        JPanel panel = new JPanel(new GridBagLayout());
        setSize(400, 600);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add components to the panel with constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        //gbc.fill = GridBagConstraints.HORIZONTAL;
        userTextField.setPreferredSize(new Dimension(200, 30));
        userTextField.setMinimumSize(new Dimension(200, 30));
        gbc.weightx = 1.0;
        panel.add(userTextField, gbc);

        gbc.gridy = 1;
        panel.add(passwordField, gbc);
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordField.setMinimumSize(new Dimension(200, 30));
        gbc.weightx = 1.0;

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        //gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        //gbc.fill = GridBagConstraints.HORIZONTAL;
        adminButton.setMinimumSize(new Dimension(20, 30));
        panel.add(adminButton, gbc);

        // Customize UI appearance
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));

        loginButton.setBackground(Color.BLUE);
        loginButton.setForeground(Color.WHITE);
        
        adminButton.setBackground(Color.WHITE);
        adminButton.setForeground(Color.BLUE);

        // Set layout manager for the content pane
        getContentPane().setLayout(new GridBagLayout());

        // Add the panel to the content pane with constraints
        GridBagConstraints cpGbc = new GridBagConstraints();
        getContentPane().add(panel, cpGbc);

        pack();  // Pack components to optimize size
        setLocationRelativeTo(null);  // Center the frame on the screen
    }
    
    public void addActionEvent() 
    {
        loginButton.addActionListener(this);
        
        adminButton.addActionListener(event -> {
            
            //System.out.println("Admin button clicked"); 
            openAdminPage();
        });
    }
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        String userName = userTextField.getText();
        String password = new String(passwordField.getPassword());
                try 
                {
                    Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/cssoftware?zeroDateTimeBehavior=CONVERT_TO_NULL",
                        "root", "root");

                    PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select userName, tempPass from users where userName=? and tempPass=?");

                    st.setString(1, userName);
                    st.setString(2, password);
                    ResultSet rs = st.executeQuery();
                    
                    if (rs.next()) 
                    {
                        dispose();
                        JOptionPane.showMessageDialog(loginButton, "You have successfully logged in");
                        openHomePage(userName);
                    } 
                    
                    else 
                    {
                        JOptionPane.showMessageDialog(loginButton, "Wrong Username & Password");
                    }
                } 
                
                catch (SQLException sqlException) 
                {
                    sqlException.printStackTrace();
                }
    }
    
    public void openAdminPage() 
    {
    System.out.println("Opening AdminPage"); // Add this line for debugging

    adminPage adminPage = new adminPage();
    adminPage.setVisible(true);
    setSize(1000, 1000);
    dispose();
    
    }
    


}

    /*
    public void locationSize()
    {
        userLabel.setBounds(50, 150, 100, 30);
        passwordLabel.setBounds(50, 220, 100, 30);
        userTextField.setBounds(150, 150, 150, 30);
        passwordField.setBounds(150, 220, 150, 30);
        //showPassword.setBounds(150, 250, 150, 30); not needed 
        loginButton.setBounds(50, 300, 100, 30);
        resetButton.setBounds(200, 300, 100, 30);
    }
                                                                    redundant and old replaced by scalable layout mananger
    public void componentsAdd()
    {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        //container.add(showPassword); not needed
        container.add(loginButton);
        container.add(resetButton);
    }
    */
        

