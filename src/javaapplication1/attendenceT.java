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
import javax.swing.table.DefaultTableModel;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class attendenceT extends JFrame implements ActionListener {

    private JButton backButton = new JButton("Back");
    private JButton markButton = new JButton("mark attendence");
    private DefaultListModel<String> teacherListModel = new DefaultListModel<>();
    private JList<String> teacherList = new JList<>(teacherListModel);
    private JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Present", "Absent"});
    private Map<String, JRadioButton[]> teacherStatusMap = new HashMap<>();


    public attendenceT() {
        initializeUI();
        addActionEvent();
        lTeacher();
        //loadAttendanceData();
    }

    private void initializeUI() {

        setTitle("Attendance Page");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components to the panel with constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Select Teacher:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane listScrollPane = new JScrollPane(teacherList);
        panel.add(listScrollPane, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Mark Status:"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(markButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        panel.add(backButton, gbc);

        // Customize UI appearance
        teacherList.setFont(new Font("Arial", Font.PLAIN, 14));
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

        String url = "jdbc:mysql://localhost:3306/cssoftware?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String dbPassword = "root";

        try ( Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            // Fetch names from the users table
            Set<String> teacherNames = fetchUserNames(connection);

            // Insert names into the teacherslist table
            insertTeacherNames(connection, teacherNames);

            // Load teacher names into the JList
            teacherListModel.clear();
            teacherNames.forEach(teacherListModel::addElement);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private Set<String> fetchUserNames(Connection connection) throws SQLException {
        String query = "SELECT name FROM users";
        Set<String> teacherNames = new HashSet<>();

        try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                teacherNames.add(resultSet.getString("name"));
            }
        }

        return teacherNames;
    }
    
    private void fetchTeacherNames(Connection connection) throws SQLException {
        String query = "SELECT name FROM teacherslist";
        try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String teacherName = resultSet.getString("name");

                // Add a radio button group for each teacher
                JRadioButton[] statusButtons = {new JRadioButton("Present"), new JRadioButton("Absent")};
                ButtonGroup buttonGroup = new ButtonGroup();
                buttonGroup.add(statusButtons[0]);
                buttonGroup.add(statusButtons[1]);

                teacherStatusMap.put(teacherName, statusButtons);
            }
        }
    }

    private void insertTeacherNames(Connection connection, Set<String> teacherNames) throws SQLException {
        String insertQuery = "INSERT IGNORE INTO teacherslist (name) VALUES (?)";

        try ( PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            for (String teacherName : teacherNames) {
                preparedStatement.setString(1, teacherName);
                preparedStatement.executeUpdate();
            }
        }
    }

    private void markAttendance() {
        for (Map.Entry<String, JRadioButton[]> entry : teacherStatusMap.entrySet()) {
            String teacherName = entry.getKey();
            JRadioButton[] statusButtons = entry.getValue();

            if (statusButtons[0].isSelected()) {
                markStatus(teacherName, "Present");
            } else if (statusButtons[1].isSelected()) {
                markStatus(teacherName, "Absent");
            }
        }

        JOptionPane.showMessageDialog(this, "Attendance marked successfully");
    }

    private void markStatus(String teacherName, String status) {
        String url = "jdbc:mysql://localhost:3306/cssoftware?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String dbPassword = "root";

        try ( Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            String updateQuery = "UPDATE teacherslist SET status = ?, time = CURRENT_TIMESTAMP WHERE name = ?";
            try ( PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, status);
                preparedStatement.setString(2, teacherName);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    /*
    private void loadAttendanceData() {
        // Clear the table before loading new data
        tableModel.setRowCount(0);

        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/cssoftware?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String dbPassword = "root";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            // Assuming 'attendance' is the table for recording attendance
            String query = "SELECT name, status FROM teacherslist";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String teacherName = resultSet.getString("name");
                    String status = resultSet.getString("status");

                    // Add a new row to the table
                    tableModel.addRow(new Object[]{teacherName, status});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void markAtten() {
        String selectedTeacher = teacherList.getSelectedValue();
        String selectedStatus = (String) statusComboBox.getSelectedItem();

        if (selectedTeacher != null && selectedStatus != null) {
            String url = "jdbc:mysql://localhost:3306/cssoftware?zeroDateTimeBehavior=CONVERT_TO_NULL";
            String user = "root";
            String dbPassword = "root";

            try ( Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
                String updateQuery = "UPDATE teacherslist SET status = ?, time = CURRENT_TIMESTAMP WHERE name = ?";
                try ( PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                    preparedStatement.setString(1, selectedStatus);
                    preparedStatement.setString(2, selectedTeacher);

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
            JOptionPane.showMessageDialog(this, "Please select a teacher and mark status");
        }
    }
     */
    private void markAtten() {
        for (Map.Entry<String, JRadioButton[]> entry : teacherStatusMap.entrySet()) {
            String teacherName = entry.getKey();
            JRadioButton[] statusButtons = entry.getValue();

            if (statusButtons[0].isSelected()) {
                markStatus(teacherName, "Present");
            } else if (statusButtons[1].isSelected()) {
                markStatus(teacherName, "Absent");
            }
        }

        JOptionPane.showMessageDialog(this, "Attendance marked successfully");
    }

    private void back() {
        //adminPage oAp = new adminPage();
        //oAp.setVisible(true);
        dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            attendenceT attendancePage = new attendenceT();
            attendancePage.setVisible(true);
        });
    }
}
