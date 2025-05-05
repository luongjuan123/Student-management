package student.models;

import db.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import student.view.Home;


public class Course {
    private int id;
    private int studentId;
    private int semester;
    private String course1;
    private String course2;
    private String course3;
    private String course4;
    private String course5;
    private Connection con;

    public Course() {
        this.con = MyConnection.getConnection();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getCourse1() {
        return course1;
    }

    public void setCourse1(String course1) {
        this.course1 = course1;
    }

    public String getCourse2() {
        return course2;
    }

    public void setCourse2(String course2) {
        this.course2 = course2;
    }

    public String getCourse3() {
        return course3;
    }

    public void setCourse3(String course3) {
        this.course3 = course3;
    }

    public String getCourse4() {
        return course4;
    }

    public void setCourse4(String course4) {
        this.course4 = course4;
    }

    public String getCourse5() {
        return course5;
    }

    public void setCourse5(String course5) {
        this.course5 = course5;
    }

    // Database operations - using original method names for compatibility
    public int getMax() {
        int id = 0;
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("select max(id) from course")) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            handleSQLException("Error getting max course ID", ex);
        }
        return id + 1;
    }

    public boolean getId(int id) {
        try (PreparedStatement ps = con.prepareStatement("select * from student where id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Home.jTextField10.setText(String.valueOf(rs.getInt(1)));
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Student's ID doesn't exist");
                    return false;
                }
            }
        } catch (SQLException ex) {
            handleSQLException("Error getting student by ID", ex);
            return false;
        }
    }

    public int countSemester(int studentId) {
        int total = 0;
        try (PreparedStatement ps = con.prepareStatement("select count(*) as 'total' from course where student_id = ?")) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt(1);
                }
                if (total == 8) {
                    JOptionPane.showMessageDialog(null, "This student has finished all the courses");
                    return -1;
                }
            }
        } catch (SQLException ex) {
            handleSQLException("Error counting semesters", ex);
        }
        return total;
    }

    public boolean isSemesterExist(int studentId, int semesterNo) {
        try (PreparedStatement ps = con.prepareStatement("select * from course where student_id = ? and semester = ?")) {
            ps.setInt(1, studentId);
            ps.setInt(2, semesterNo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            handleSQLException("Error checking semester existence", ex);
            return false;
        }
    }

    public boolean isCourseExist(int studentId, String courseNo, String course) {
        String sql = "select * from course where student_id = ? and " + courseNo + " = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setString(2, course);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            handleSQLException("Error checking course existence", ex);
            return false;
        }
    }

    public void insert(int id, int sid, int semester, String course1, String course2, String course3, String course4, String course5) {
        String sql = "insert into course values(?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, sid);
            ps.setInt(3, semester);
            ps.setString(4, course1);
            ps.setString(5, course2);
            ps.setString(6, course3);
            ps.setString(7, course4);
            ps.setString(8, course5);
            
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Course added successfully");
            }
        } catch (SQLException ex) {
            handleSQLException("Error inserting course", ex);
        }
    }

    public void getCourseValue(JTable table, String searchValue) {
        String sql = "select * from course where concat(id,student_id,semester) like ? order by id desc";
        fillTable(table, sql, "%" + searchValue + "%");
    }

    public void getCourseValueTable(JTable table, String searchValue) {
        String sql = "SELECT * FROM course WHERE student_id LIKE ? ORDER BY id DESC";
        fillTable(table, sql, "%" + searchValue + "%");
    }

    private void fillTable(JTable table, String sql, String searchValue) {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, searchValue);
            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0); // Clear existing rows
                
                while (rs.next()) {
                    Object[] row = new Object[8];
                    for (int i = 0; i < 8; i++) {
                        row[i] = rs.getObject(i + 1);
                    }
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            handleSQLException("Error filling table", ex);
        }
    }

    private void handleSQLException(String message, SQLException ex) {
        System.err.println(message);
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
    }
}