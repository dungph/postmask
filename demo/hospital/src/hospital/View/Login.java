/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospital.View;

import hospital.Database;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *
 * @author dun
 */
public class Login {

    JTextField username;
    JTextField password;
    JButton login;

    public Login() {
        JFrame frame = new JFrame("Dang nhap");
        JPanel panel = new JPanel(new GridLayout(2, 2));
        username = new JTextField("admin");
        password = new JTextField();
        login = new JButton("Dang nhap");
        panel.add(new JLabel("Ten dang nhap"));
        panel.add(username);
        panel.add(new JLabel("Mat khau"));
        panel.add(password);
        frame.add(panel, BorderLayout.NORTH);

        login.addActionListener(view -> {
            try {
                Database data = new Database(username.getText(), password.getText());
                frame.setVisible(false);
                frame.dispose();
                JOptionPane.showMessageDialog(frame, "Dang nhap thanh cong");
                new PatientDiagnoseList(data);
            } catch (ClassNotFoundException | SQLException e) {
                JOptionPane.showMessageDialog(frame, "Dang nhap that bai");
                e.printStackTrace();
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(login, BorderLayout.SOUTH);

        frame.setSize(300, 100);
        frame.setVisible(true);
    }
}
