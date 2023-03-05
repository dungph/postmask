/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospital.View;

import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hospital.Database;

/**
 *
 * @author dun
 */
public class NewPatient {
    JTextField name;
    JTextField birthday;
    JTextField phone;
    JTextField address;
    JButton submit;
    JButton cancel;

    public NewPatient(Database db) {

        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new GridLayout(5, 2));

        name = new JTextField();
        birthday = new JTextField();
        phone = new JTextField();
        address = new JTextField();

        submit = new JButton("Submit");
        cancel = new JButton("Cancel");
        submit.addActionListener(a -> {
            try {
                db.newPatient(name.getText(), birthday.getText(), phone.getText(), address.getText());
                frame.setVisible(false);
                frame.dispose();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "That bai");
                e.printStackTrace();
            }
        });
        cancel.addActionListener(a -> {
            frame.setVisible(false);
            frame.dispose();
        });
        panel.add(new JLabel("Patient Name:"));
        panel.add(name);
        panel.add(new JLabel("Patient Birthday(dd/mm/yyyy):"));
        panel.add(birthday);
        panel.add(new JLabel("Patient Phone"));
        panel.add(phone);
        panel.add(new JLabel("Patient address"));
        panel.add(address);
        panel.add(submit);
        panel.add(cancel);

        frame.add(panel);
        frame.setSize(400, 200);
        frame.setVisible(true);

    }
}
