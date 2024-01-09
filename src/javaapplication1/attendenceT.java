package javaapplication1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class attendenceT extends JFrame implements ActionListener
{
    
    private JButton backButton = new JButton("Back");
    private JButton markButton = new JButton("Mark Attendance");
    private JComboBox<String> teacherComboBox = new JComboBox<>();
    
    public attendenceT()
    {
        initializeUI();
        addActionEvent();
        lTeacher();
    }
    
    private void initializeUI() {
        setTitle("Attendance Page");
        setSize(400, 300);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components to the panel with constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Select Teacher:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(teacherComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(markButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(backButton, gbc);

        // Customize UI appearance
        teacherComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        markButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFont(new Font("Arial", Font.BOLD, 14));

        markButton.setBackground(Color.GREEN);
        markButton.setForeground(Color.WHITE);

        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLUE);

        // Set layout manager for the content pane
        getContentPane().setLayout(new GridBagLayout());

        // Add the panel to the content pane with constraints
        GridBagConstraints cpGbc = new GridBagConstraints();
        getContentPane().add(panel, cpGbc);
    }
    
    private void addActionEvent() {
        backButton.addActionListener(e -> {
            back();
        });

        markButton.addActionListener(e -> {
            markAtten();
        });
    }
    
    private void lTeacher() {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/cssoftware?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String dbPassword = "root";

        try ( Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            // Assuming 'teachers' is the table containing the list of teachers
            String query = "SELECT name FROM teacherslist";
            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String teacherName = resultSet.getString("name");
                    teacherComboBox.addItem(teacherName);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void markAtten() {
        // Retrieve the selected teacher's name
        String selectedTeacher = (String) teacherComboBox.getSelectedItem();

        if (selectedTeacher != null) {
            // Database connection parameters
            String url = "jdbc:mysql://localhost:3306/cssoftware?zeroDateTimeBehavior=CONVERT_TO_NULL";
            String user = "root";
            String dbPassword = "root";

            try ( Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
                // Assuming 'attendance' is the table for recording attendance
                String updateQuery = "UPDATE teacherslist SET status = 'Present', time = CURRENT_TIMESTAMP WHERE name = ?";
                try ( PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                    preparedStatement.setString(1, selectedTeacher);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Attendance marked successfully");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to mark attendance");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a teacher");
        }
    }
    
    private void back() 
    {
        //adminPage oAp = new adminPage();
        //oAp.setVisible(true);
        dispose();
    }

    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
       
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            attendenceT attendancePage = new attendenceT();
            attendancePage.setVisible(true);
        });
    }
}
