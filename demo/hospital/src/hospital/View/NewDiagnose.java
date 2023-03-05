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
import javax.swing.JTextArea;
import javax.swing.JTextField;

import hospital.Database;

/**
 *
 * @author dun
 */
public class NewDiagnose {
    JTextField patient_id;
    JTextArea diagnose_content;
    JButton submit;
    JButton cancel;

    public NewDiagnose(Database db) {
        JFrame frame = new JFrame("New Diagnose");
        JPanel panel = new JPanel(new GridLayout(5, 2));

        patient_id = new JTextField();
        diagnose_content = new JTextArea(5, 50);

        submit = new JButton("Submit");
        cancel = new JButton("Cancel");
        submit.addActionListener(a -> {
            try {
                db.newDiagnose(patient_id.getText(), diagnose_content.getText());
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
        panel.add(new JLabel("Patient Id:"));
        panel.add(patient_id);
        panel.add(new JLabel("Diagnose: "));
        panel.add(diagnose_content);
        panel.add(submit);
        panel.add(cancel);

        frame.add(panel);
        frame.setSize(400, 200);
        frame.setVisible(true);

    }
}
