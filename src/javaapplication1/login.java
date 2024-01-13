
package javaapplication1;

import javax.swing.JFrame;

public class login 
    {

        public static void main(String[] a) 
        {
            //Creating window and setting its properties
            CsIa frame = new CsIa();
            frame.setTitle("Login Form");
            frame.setVisible(true);
            frame.setBounds(10,10,370,600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true);

        }
    }
    
