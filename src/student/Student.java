/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package student;

import db.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author juan
 */
public class Student extends Person {

    Connection con = (Connection) MyConnection.getConnection();
    PreparedStatement ps;

    //get table max row
    public int getMax() {
        int id = 0;
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(id) from student");
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id + 1;
    }

    // Insert data vao bang student sql
    public void insert(int id, String sname, String date, String gender, String email, String phone,
            String father, String mother, String address, String imagePath) {
        String sql = "insert into student values(?,?,?,?,?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, sname);
            ps.setString(3, date);
            ps.setString(4, gender);
            ps.setString(5, email);
            ps.setString(6, phone);
            ps.setString(7, father);
            ps.setString(8, mother);
            ps.setString(9, address);
            ps.setString(10, imagePath);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "New student added successfully");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // check gmail address is already exist
    public boolean isEmailExist(String email) {
        try {
            ps = con.prepareStatement("select * from student where email = ?");
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // check phone number is already exist
    public boolean isPhoneExist(String phone) {
        try {
            ps = con.prepareStatement("select * from student where phone = ?");
            ps.setString(1, phone);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // get all the student values from database student table
    public void getStudentValue(JTable table, String searchValue) {
        String sql = "select * from student where concat(id,name,email,phone)like ? order by id desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + searchValue + "%");
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[10];
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                row[5] = rs.getString(6);
                row[6] = rs.getString(7);
                row[7] = rs.getString(8);
                row[8] = rs.getString(9);
                row[9] = rs.getString(10);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update(int id, String sname, String date, String gender, String email, String phone,
            String father, String mother, String address, String imagePath) {
        String sql = "update student set name=?,date_of_birth=?,gender=?,email=?,phone=?,father_name=?,mother_name=?,address=?,image_path=? where id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, sname);
            ps.setString(2, date);
            ps.setString(3, gender);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setString(6, father);
            ps.setString(7, mother);
            ps.setString(8, address);
            ps.setString(9, imagePath);
            ps.setInt(10, id);
            
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Student data updated successfully");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
