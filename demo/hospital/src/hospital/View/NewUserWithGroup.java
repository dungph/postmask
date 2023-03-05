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
public class NewUserWithGroup {
    JTextField username;
    JTextField password;
    JTextField group;
    JButton submit;
    JButton cancel;

    public NewUserWithGroup(Database db) {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new GridLayout(4, 2));

        username = new JTextField();
        password = new JTextField();
        group = new JTextField();
        submit = new JButton("Submit");
        cancel = new JButton("Cancel");
        submit.addActionListener(a -> {
            try {
                db.createUser(username.getText(), password.getText(), group.getText());
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
        panel.add(new JLabel("Username:"));
        panel.add(username);
        panel.add(new JLabel("Password:"));
        panel.add(password);
        panel.add(new JLabel("Group"));
        panel.add(group);
        panel.add(submit);
        panel.add(cancel);

        frame.add(panel);
        frame.setSize(300, 200);
        frame.setVisible(true);

    }
}
