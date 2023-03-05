/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospital.View;

import hospital.Database;
import hospital.Data.Patient;

import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author dun
 */

public class PatientList {
    JButton createAccountButton;
    JButton listAccountButton;
    JButton newDiagnoseButton;
    JButton dummyButton;

    public PatientList(Database db) {
        JFrame frame = new JFrame();
        // Frame Title
        frame.setTitle("Patient");
        // Data to be displayed in the JTable

        String[][] data = new String[100][5];
        try {
            ArrayList<Patient> patients = db.getAllPatient();
            for (int i = 0; i < patients.size(); i++) {
                data[i][0] = Integer.toString(patients.get(i).getPatientId());
                data[i][1] = patients.get(i).getPatientName();
                data[i][2] = patients.get(i).getPatientBirthday();
                data[i][3] = patients.get(i).getPatientPhone();
                data[i][4] = patients.get(i).getPatientAddress();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Tai danh sach that bai");
            e.printStackTrace();
        }
        // Column Names
        String[] columnNames = { "ID", "Name", "Birthday", "Phone", "Address" };
        // Initializing the JTable
        JTable jTable = new JTable(data, columnNames);
        jTable.setBounds(30, 40, 200, 300);
        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(jTable);

        JPanel ctrls = new JPanel(new GridLayout(1, 5));

        newDiagnoseButton = new JButton("New Diagnose");
        createAccountButton = new JButton("Create Account");
        listAccountButton = new JButton("List Account");
        newDiagnoseButton.addActionListener(a -> {

        });
        createAccountButton.addActionListener(a -> {
            new NewUserWithGroup(db);
        });
        listAccountButton.addActionListener(a -> {

        });
        ctrls.add(newDiagnoseButton);
        ctrls.add(createAccountButton);
        ctrls.add(listAccountButton);

        frame.add(ctrls);
        frame.add(sp);
        // Frame Size
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
