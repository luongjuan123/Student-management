package myapp.models;

import db.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import myapp.view.Home;

/**
 * Class representing a Score
 */
public class Score {

    private int id;
    private int studentId;
    private int semester;
    private String course1;
    private String course2;
    private String course3;
    private String course4;
    private String course5;
    private double score1;
    private double score2;
    private double score3;
    private double score4;
    private double score5;
    private double average;
    private Connection con;

    public Score() {
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

    public double getScore1() {
        return score1;
    }

    public void setScore1(double score1) {
        this.score1 = score1;
    }

    public double getScore2() {
        return score2;
    }

    public void setScore2(double score2) {
        this.score2 = score2;
    }

    public double getScore3() {
        return score3;
    }

    public void setScore3(double score3) {
        this.score3 = score3;
    }

    public double getScore4() {
        return score4;
    }

    public void setScore4(double score4) {
        this.score4 = score4;
    }

    public double getScore5() {
        return score5;
    }

    public void setScore5(double score5) {
        this.score5 = score5;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    // Database operations
    public int getMax() {
        int id = 0;
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery("select max(id) from score")) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            handleSQLException("Error getting max score ID", ex);
        }
        return id + 1;
    }

    public boolean getDetails(int studentId, int semesterNo) {
        try (PreparedStatement ps = con.prepareStatement("select * from course where student_id = ? and semester = ?")) {
            ps.setInt(1, studentId);
            ps.setInt(2, semesterNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Home.jTextField11.setText(String.valueOf(rs.getInt(2)));
                    Home.jTextField15.setText(String.valueOf(rs.getInt(3)));
                    Home.jTextCourse1.setText(rs.getString(4));
                    Home.jTextCourse2.setText(rs.getString(5));
                    Home.jTextCourse3.setText(rs.getString(6));
                    Home.jTextCourse4.setText(rs.getString(7));
                    Home.jTextCourse5.setText(rs.getString(8));
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Student's ID or semester doesn't exist");
                    return false;
                }
            }
        } catch (SQLException ex) {
            handleSQLException("Error getting course details", ex);
            return false;
        }
    }

    public boolean isIdExist(int id) {
        return checkFieldExists("id", String.valueOf(id));
    }

    public boolean isSidSemesterNoExist(int studentId, int semesterNo) {
        try (PreparedStatement ps = con.prepareStatement("select * from score where student_id = ? and semester = ?")) {
            ps.setInt(1, studentId);
            ps.setInt(2, semesterNo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            handleSQLException("Error checking student ID and semester existence", ex);
            return false;
        }
    }

    private boolean checkFieldExists(String field, String value) {
        String sql = "select * from score where " + field + " = ?";
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

    public void insert(int id, int studentId, int semester, String course1, String course2, String course3, 
                  String course4, String course5, double score1, double score2, double score3, 
                  double score4, double score5, double average) {
    String sql = "insert into score values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, id);
        ps.setInt(2, studentId);
        ps.setInt(3, semester);
        ps.setString(4, course1);
        ps.setDouble(5, score1);
        ps.setString(6, course2);
        ps.setDouble(7, score2);
        ps.setString(8, course3);
        ps.setDouble(9, score3);
        ps.setString(10, course4);
        ps.setDouble(11, score4);
        ps.setString(12, course5);
        ps.setDouble(13, score5);
        ps.setDouble(14, average);
        
        if (ps.executeUpdate() > 0) {
            JOptionPane.showMessageDialog(null, "Score added successfully");
        }
    } catch (SQLException ex) {
        handleSQLException("Error inserting score", ex);
    }
}

    public void getScoreValue(JTable table, String searchValue) {
        String sql = "select * from score where concat(id,student_id,semester) like ? order by id desc";
        fillTable(table, sql, "%" + searchValue + "%");
    }
    
    public void getScoreValue1(JTable table, String searchValue) {
        String sql = "SELECT * FROM score WHERE student_id LIKE ? ORDER BY id DESC";
        fillTable(table, sql, "%" + searchValue + "%");
    }

    public void update(int id, double score1, double score2, double score3, double score4, double score5, double average) {
        String sql = "update score set score1=?,score2=?,score3=?,score4=?,score5=?,average=? where id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, score1);
            ps.setDouble(2, score2);
            ps.setDouble(3, score3);
            ps.setDouble(4, score4);
            ps.setDouble(5, score5);
            ps.setDouble(6, average);
            ps.setInt(7, id);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Score updated successfully");
            }
        } catch (SQLException ex) {
            handleSQLException("Error updating score", ex);
        }
    }

    private void fillTable(JTable table, String sql, String searchValue) {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, searchValue);
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
            handleSQLException("Error filling score table", ex);
        }
    }

    private void handleSQLException(String message, SQLException ex) {
        System.err.println(message);
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
    }
}
