/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospital;

import hospital.Data.PatientDiagnose;
import hospital.Data.Patient;
import hospital.Data.UserGroup;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author dun
 */
public class Database {

    private static final String DB_URL = "jdbc:postgresql://localhost/hospital";
    java.sql.Connection con;

    public Database(String username, String password) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        con = java.sql.DriverManager.getConnection(DB_URL, username, password);
    }

    public ArrayList<String> getAllGroup() throws SQLException {
        ArrayList<String> list = new ArrayList<String>();
        java.sql.Statement st;

        st = con.createStatement();
        java.sql.ResultSet rec = st.executeQuery(
                "select distinct unmask_group from unmask_object");
        while (rec.next()) {
            list.add(rec.getString("patient_birthday"));
        }

        return list;

    }

    public ArrayList<Patient> getAllPatient() throws SQLException {
        ArrayList<Patient> list = new ArrayList<Patient>();
        java.sql.Statement st;

        st = con.createStatement();
        java.sql.ResultSet rec = st.executeQuery(
                "select"
                        + " patient_id"
                        + ", patient_name"
                        + ", to_char(patient_birthday, 'yyyy/mm/dd') as patient_birthday"
                        + ", patient_phone"
                        + ", patient_address"
                        + " from patient");
        while (rec.next()) {
            var p = new Patient();
            p.setPatientId(rec.getInt("patient_id"));
            p.setPatientName(rec.getString("patient_name"));
            p.setPatientBirthday(rec.getString("patient_birthday"));
            p.setPatientPhone(rec.getString("patient_phone"));
            p.setPatientAddress(rec.getString("patient_address"));
            list.add(p);
        }

        return list;
    }

    public ArrayList<PatientDiagnose> getAllPatientWithDiagnose() throws SQLException {
        ArrayList<PatientDiagnose> list = new ArrayList<PatientDiagnose>();
        java.sql.Statement st;

        st = con.createStatement();
        java.sql.ResultSet rec = st.executeQuery(
                "select"
                        + " patient.patient_id"
                        + ", diagnose_id"
                        + ", patient_name"
                        + ", to_char(patient_birthday, 'yyyy/mm/dd') as patient_birthday"
                        + ", patient_phone"
                        + ", patient_address"
                        + ", diagnose_date"
                        + ", diagnose_content"
                        + " from patient"
                        + " join diagnose"
                        + " on diagnose.patient_id = patient.patient_id");

        while (rec.next()) {
            var p = new PatientDiagnose();
            p.setPatientId(rec.getInt("patient_id"));
            p.setDiagnoseId(rec.getInt("diagnose_id"));
            p.setPatientName(rec.getString("patient_name"));
            p.setPatientBirthday(rec.getString("patient_birthday"));
            p.setPatientPhone(rec.getString("patient_phone"));
            p.setPatientAddress(rec.getString("patient_address"));
            p.setDiagnoseDate(rec.getString("diagnose_date"));
            p.setDiagnoseContent(rec.getString("diagnose_content"));
            list.add(p);
        }

        return list;
    }

    public void newDiagnose(String patient_id, String content) throws SQLException {
        var st2 = con.prepareStatement(
                "insert into diagnose (patient_id,diagnose_date, diagnose_content) values (?, now()::date, ?)");
        st2.setInt(1, Integer.parseInt(patient_id));
        st2.setString(2, content);
        st2.execute();

    }

    public void newPatient(String name, String birth, String phone, String addr) throws SQLException {
        var st2 = con.prepareStatement(
                "insert into patient (patient_name, patient_birthday, patient_phone, patient_address) values (?, to_date(?, 'dd/mm/yyyy'), ?, ?)");
        st2.setString(1, name);
        st2.setString(2, birth);
        st2.setString(3, phone);
        st2.setString(4, addr);
        st2.execute();

    }

    public void createUser(String user, String password, String group) throws SQLException {
        con.createStatement().execute("create user " + user + " with password '"
                + password + "'");

        con.createStatement().execute("grant all on patient to " + user);
        con.createStatement().execute("grant all on diagnose to " + user);
        con.createStatement().execute("grant select on unmask_user to " + user);
        con.createStatement().execute("grant select on unmask_object to " + user);

        var st2 = con.prepareStatement("select assign_user_to_group(?, ?)");
        st2.setString(1, user);
        st2.setString(2, group);
        st2.execute();
    }

    public ArrayList<UserGroup> listUserAndGroup() throws SQLException {
        ArrayList<UserGroup> list = new ArrayList<UserGroup>();
        java.sql.Statement st;

        st = con.createStatement();
        java.sql.ResultSet rec = st.executeQuery(
                "select unmask_user, unmask_group from unmask_user");

        while (rec.next()) {
            var p = new UserGroup();
            p.setUser(rec.getString("unmask_user"));
            p.setGroup(rec.getString("unmask_group"));
            list.add(p);
        }

        return list;
    }
}
