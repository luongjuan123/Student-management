package myapp.models;

import db.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Class representing a MarkSheet
 */
public class MarkSheet {
    private Connection con;

    public MarkSheet() {
        this.con = MyConnection.getConnection();
    }

    public boolean isIdExist(int studentId) {
        try (PreparedStatement ps = con.prepareStatement("select * from score where student_id = ?")) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            handleSQLException("Error checking student ID existence", ex);
            return false;
        }
    }

    public void getScoreValue(JTable table, int studentId) {
        String sql = "select * from score where student_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0); // Clear existing rows
                
                while (rs.next()) {
                    Object[] row = new Object[14];
                    for (int i = 0; i < 14; i++) {
                        row[i] = rs.getObject(i + 1);
                    }
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            handleSQLException("Error getting score values", ex);
        }
    }

    public double getGPA(int studentId) {
        double gpa = 0.0;
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("select avg(average) from score where student_id = " + studentId)) {
            if (rs.next()) {
                gpa = rs.getDouble(1);
            }
        } catch (SQLException ex) {
            handleSQLException("Error calculating GPA", ex);
        }
        return gpa;
    }

    private void handleSQLException(String message, SQLException ex) {
        System.err.println(message);
        ex.printStackTrace();
    }
}