package myapp.models;

import myapp.db.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class Student extends Person {
    private String fatherName;
    private String motherName;
    private String imagePath;
    private Connection con;

    // Default constructor for compatibility
    public Student() {
        super(0, ""); // Default values
        this.con = MyConnection.getConnection();
    }

    // Parameterized constructor
    public Student(int id, String name) {
        super(id, name);
        this.con = MyConnection.getConnection();
    }

    // Additional getters and setters
    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // Database operations
    public int getMax() {
        int id = 0;
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("select max(id) from student")) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            handleSQLException("Error getting max student ID", ex);
        }
        return id + 1;
    }

    public void insert(int id, String sname, String date, String gender, String email, String phone,
            String father, String mother, String address, String imagePath) {
        String sql = "insert into student values(?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
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
            handleSQLException("Error inserting student", ex);
        }
    }

    public boolean isEmailExist(String email) {
        return checkFieldExists("email", email);
    }

    public boolean isPhoneExist(String phone) {
        return checkFieldExists("phone", phone);
    }

    public boolean isIdExist(int id) {
        return checkFieldExists("id", String.valueOf(id));
    }

    private boolean checkFieldExists(String field, String value) {
        String sql = "select * from student where " + field + " = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            handleSQLException("Error checking field existence: " + field, ex);
            return false;
        }
    }

    public void getStudentValue(JTable table, String searchValue) {
        String sql = "select * from student where concat(id,name,email,phone) like ? order by id desc";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + searchValue + "%");
            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0); // Clear existing rows
                
                while (rs.next()) {
                    Object[] row = new Object[10];
                    for (int i = 0; i < 10; i++) {
                        row[i] = rs.getObject(i + 1);
                    }
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            handleSQLException("Error getting student values", ex);
        }
    }

    public void update(int id, String sname, String date, String gender, String email, String phone,
            String father, String mother, String address, String imagePath) {
        String sql = "update student set name=?,date_of_birth=?,gender=?,email=?,phone=?,father_name=?,mother_name=?,address=?,image_path=? where id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
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
            handleSQLException("Error updating student", ex);
        }
    }

    public void delete(int id) {
        int yesOrNo = JOptionPane.showConfirmDialog(
            null,
            "Courses and score records will also be deleted",
            "Student Delete",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (yesOrNo == JOptionPane.OK_OPTION) {
            String input = JOptionPane.showInputDialog(null, "Please re-enter the student ID to confirm:");
            if (input != null) {
                try {
                    int confirmId = Integer.parseInt(input.trim());
                    if (confirmId == id) {
                        try (PreparedStatement ps = con.prepareStatement("DELETE FROM student WHERE id = ?")) {
                            ps.setInt(1, id);
                            if (ps.executeUpdate() > 0) {
                                JOptionPane.showMessageDialog(null, "Student deleted successfully.");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "ID does not match. Deletion canceled.");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
                } catch (SQLException ex) {
                    handleSQLException("Error deleting student", ex);
                }
            }
        }
    }

    private void handleSQLException(String message, SQLException ex) {
        System.err.println(message);
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
    }
}