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

public class timeTable extends JFrame
{

    private static final int DAYS = 5;
    private static final int CLASSES_PER_DAY = 5;

    private JButton[][] scheduleButtons;

    public timeTable() {
        initializeUI();
        loadScheduleFromDatabase();
    }

    private void initializeUI() {
        setTitle("School Timetable");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        scheduleButtons = new JButton[DAYS][CLASSES_PER_DAY];

        JPanel timetablePanel = new JPanel(new GridLayout(CLASSES_PER_DAY + 1, DAYS + 1));
        timetablePanel.add(new JLabel("Time/Day"));

        for (int day = 1; day <= DAYS; day++) {
            timetablePanel.add(new JLabel("Day " + day));
        }

        for (int classNum = 1; classNum <= CLASSES_PER_DAY; classNum++) {
            timetablePanel.add(new JLabel("Class " + classNum));

            for (int day = 1; day <= DAYS; day++) {
                JButton button = new JButton("Edit");
                scheduleButtons[day - 1][classNum - 1] = button;
                button.addActionListener(new ScheduleButtonListener(day, classNum));
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
                        scheduleButtons[day][classNum].setText(schedule);
                    }
                    day++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load schedule from the database");
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
    
    

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> 
        {
            timeTable timetableApp = new timeTable();
            timetableApp.setVisible(true);
        });
    }
}
