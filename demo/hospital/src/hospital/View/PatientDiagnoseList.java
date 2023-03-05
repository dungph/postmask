/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospital.View;

import hospital.Database;
import hospital.Data.PatientDiagnose;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

/**
 *
 * @author dun
 */

public class PatientDiagnoseList {
    JButton newAccountButton;
    JButton newPatientButton;
    JButton newDiagnoseButton;
    JButton listPatientButton;
    JButton reloadButton;
    JButton logoutButton;
    JButton dummyButton;

    public PatientDiagnoseList(Database db) {
        JFrame frame = new JFrame();
        // Frame Title
        frame.setTitle("Diagnose");
        // Data to be displayed in the JTable

        String[][] data = new String[100][6];
        try {
            ArrayList<PatientDiagnose> patients = db.getAllPatientWithDiagnose();
            for (int i = 0; i < patients.size(); i++) {
                data[i][0] = patients.get(i).getPatientName();
                data[i][1] = patients.get(i).getPatientBirthday();
                data[i][2] = patients.get(i).getPatientPhone();
                data[i][3] = patients.get(i).getPatientAddress();
                data[i][4] = patients.get(i).getDiagnoseDate();
                data[i][5] = patients.get(i).getDiagnoseContent();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Tai danh sach that bai");
            e.printStackTrace();
        }
        // Column Names
        String[] columnNames = { "Name", "Birthday", "Phone", "Address", "Date", "Diagnose" };
        // Initializing the JTable
        JTable jTable = new JTable(data, columnNames);
        jTable.setBounds(30, 40, 200, 300);
        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(jTable);

        JPanel ctrls = new JPanel(new GridLayout(1, 5));

        newDiagnoseButton = new JButton("New Diagnose");
        newAccountButton = new JButton("Create Account");
        newPatientButton = new JButton("New Patient");
        reloadButton = new JButton("Reload");
        logoutButton = new JButton("Logout");
        listPatientButton = new JButton("List Patient");

        listPatientButton.addActionListener(a -> {
            new PatientList(db);
        });
        newDiagnoseButton.addActionListener(a -> {
            new NewDiagnose(db);
        });
        newAccountButton.addActionListener(a -> {
            new NewUserWithGroup(db);
        });
        newPatientButton.addActionListener(a -> {
            new NewPatient(db);
        });
        reloadButton.addActionListener(a -> {
            new PatientDiagnoseList(db);
            frame.dispose();
        });
        logoutButton.addActionListener(a -> {
            new Login();
            frame.dispose();
        });

        ctrls.add(newPatientButton);
        ctrls.add(newDiagnoseButton);
        ctrls.add(newAccountButton);
        ctrls.add(listPatientButton);
        ctrls.add(reloadButton);
        ctrls.add(logoutButton);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(sp, BorderLayout.SOUTH);
        frame.add(ctrls, BorderLayout.NORTH);
        // Frame Size
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
