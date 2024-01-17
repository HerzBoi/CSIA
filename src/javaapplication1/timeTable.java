package javaapplication1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class timeTable extends JFrame
{

    private static final int DAYS = 5;
    private static final int CLASSES_PER_DAY = 5;

    private JButtonInfo[][] scheduleButtons;

    public timeTable() {
        initializeUI();
        loadScheduleFromDatabase();
    }

    private void initializeUI() {
        setTitle("School Timetable");
        setSize(600, 400);
        setLocationRelativeTo(null);

        scheduleButtons = new JButtonInfo[DAYS][CLASSES_PER_DAY];

        JPanel timetablePanel = new JPanel(new GridLayout(CLASSES_PER_DAY + 2, DAYS + 1));

        // Create a teacher dropdown for the top row
        JComboBox<String> topTeacherComboBox = new JComboBox<>();
        loadTeacherNames(topTeacherComboBox);

        // Add an action listener to handle changes in the top combo box
        topTeacherComboBox.addActionListener(e -> {
            String selectedTeacher = (String) topTeacherComboBox.getSelectedItem();
            // Set the selected teacher for the entire column (all classes) on that day
            for (int classNum = 1; classNum <= CLASSES_PER_DAY; classNum++) {
                scheduleButtons[0][classNum - 1].setTeacherName(selectedTeacher);
            }
        });

        // Add an empty label to the top-left corner
        timetablePanel.add(new JLabel());

        for (int day = 1; day <= DAYS; day++) {
            timetablePanel.add(new JLabel("Day " + day));
        }

        // Add the top combo box to the panel
        timetablePanel.add(topTeacherComboBox);

        for (int classNum = 1; classNum <= CLASSES_PER_DAY; classNum++) {
            timetablePanel.add(new JLabel("Class " + classNum));

            for (int day = 1; day <= DAYS; day++) {
                JComboBox<String> teacherComboBox = new JComboBox<>();
                JButton button = new JButton("Edit");
                scheduleButtons[day - 1][classNum - 1] = new JButtonInfo(button);

                // Load teacher names into the combo box
                loadTeacherNames(teacherComboBox);

                final int finalDay = day;
                final int finalClassNum = classNum;
                teacherComboBox.addActionListener(e -> {
                    String selectedTeacher = (String) teacherComboBox.getSelectedItem();
                    scheduleButtons[finalDay - 1][finalClassNum - 1].setTeacherName(selectedTeacher);
                });

                // Add the combo box and button to the panel
                timetablePanel.add(teacherComboBox);
                timetablePanel.add(button);
            }
        }

        add(timetablePanel);
    }

    private void loadScheduleFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/cssoftware?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String password = "root";

        try ( Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT class1, class2, class3, class4, class5 FROM tt";
            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                int day = 0;
                while (resultSet.next() && day < DAYS) {
                    for (int classNum = 0; classNum < CLASSES_PER_DAY; classNum++) {
                        String schedule = resultSet.getString("class" + (classNum + 1));
                        scheduleButtons[day][classNum].setText(schedule); //error: missing import?
                    }
                    day++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load schedule from the database");
        }
    }
    
    private void loadTeacherNames(JComboBox<String> comboBox) {
        String url = "jdbc:mysql://localhost:3306/cssoftware?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String password = "root";

        comboBox.removeAllItems(); // Clear existing items

        try ( Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT name FROM users";
            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String teacherName = resultSet.getString("name");
                    comboBox.addItem(teacherName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load teacher names from the database");
        }
    }

    private class ScheduleButtonListener implements ActionListener {

        private final int day;
        private final int classNum;

        public ScheduleButtonListener(int day, int classNum) {
            this.day = day;
            this.classNum = classNum;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            // Handle button click, you can open a dialog or perform any action here
            JOptionPane.showMessageDialog(timeTable.this, "Edit schedule for Day " + day + ", Class " + classNum);
        }
    }
    
    
    private class JButtonInfo {

        private final JButton button;
        private String className;
        private String teacherName;

        public JButtonInfo(JButton button) {
            this.button = button;
        }

        public JButton getButton() {
            return button;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }
        
        public void setText(String text) {
            button.setText(text);
        }
    }
    
    
   
    

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> 
        {
            timeTable timetableApp = new timeTable();
            timetableApp.setVisible(true);
        });
    }
}
