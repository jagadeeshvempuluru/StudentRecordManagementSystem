package com.srms.dao;

import com.srms.model.Student;
import com.srms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentDAO — Data Access Object
 *
 * All database operations for the students table.
 * Uses PreparedStatement to prevent SQL Injection.
 * Demonstrates OOP: Abstraction (hides SQL details behind clean method names).
 */
public class StudentDAO {

    // ══════════════════════════════════════════════════════════════════════════
    //  CREATE
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Inserts a new student record into the database.
     * @return  true if insert succeeded, false otherwise
     */
    public boolean addStudent(Student s) {
        String sql = """
            INSERT INTO students
                (name, email, phone, date_of_birth, gender, address,
                 department, course, year, gpa, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1,  s.getName());
            ps.setString(2,  s.getEmail());
            ps.setString(3,  s.getPhone());
            ps.setDate(4,    s.getDateOfBirth());
            ps.setString(5,  s.getGender());
            ps.setString(6,  s.getAddress());
            ps.setString(7,  s.getDepartment());
            ps.setString(8,  s.getCourse());
            ps.setInt(9,     s.getYear());
            ps.setDouble(10, s.getGpa());
            ps.setString(11, s.getStatus());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) s.setStudentId(keys.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("  [DB Error] addStudent: " + e.getMessage());
        }
        return false;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  READ — all
    // ══════════════════════════════════════════════════════════════════════════

    /** Returns every student row, newest first. */
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("  [DB Error] getAllStudents: " + e.getMessage());
        }
        return list;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  READ — single
    // ══════════════════════════════════════════════════════════════════════════

    /** Finds one student by primary key. Returns null if not found. */
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("  [DB Error] getStudentById: " + e.getMessage());
        }
        return null;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  READ — search
    // ══════════════════════════════════════════════════════════════════════════

    /** Full-text search across name, email, department, course, phone. */
    public List<Student> searchStudents(String keyword) {
        List<Student> list = new ArrayList<>();
        String sql = """
            SELECT * FROM students
            WHERE name       LIKE ?
               OR email      LIKE ?
               OR department LIKE ?
               OR course     LIKE ?
               OR phone      LIKE ?
            ORDER BY name
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String k = "%" + keyword + "%";
            for (int i = 1; i <= 5; i++) ps.setString(i, k);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("  [DB Error] searchStudents: " + e.getMessage());
        }
        return list;
    }

    /** Returns all students in a specific department. */
    public List<Student> getStudentsByDepartment(String dept) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE department = ? ORDER BY name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dept);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("  [DB Error] getStudentsByDepartment: " + e.getMessage());
        }
        return list;
    }

    /** Returns all students by status (Active / Inactive / Graduated / Dropped). */
    public List<Student> getStudentsByStatus(String status) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE status = ? ORDER BY name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("  [DB Error] getStudentsByStatus: " + e.getMessage());
        }
        return list;
    }

    /** Returns top N students by GPA. */
    public List<Student> getTopStudents(int limit) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE status = 'Active' ORDER BY gpa DESC LIMIT ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("  [DB Error] getTopStudents: " + e.getMessage());
        }
        return list;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  UPDATE
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Updates ALL fields of a student record (full update).
     * @return true if at least one row was updated
     */
    public boolean updateStudent(Student s) {
        String sql = """
            UPDATE students SET
                name        = ?,
                email       = ?,
                phone       = ?,
                date_of_birth = ?,
                gender      = ?,
                address     = ?,
                department  = ?,
                course      = ?,
                year        = ?,
                gpa         = ?,
                status      = ?
            WHERE student_id = ?
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1,  s.getName());
            ps.setString(2,  s.getEmail());
            ps.setString(3,  s.getPhone());
            ps.setDate(4,    s.getDateOfBirth());
            ps.setString(5,  s.getGender());
            ps.setString(6,  s.getAddress());
            ps.setString(7,  s.getDepartment());
            ps.setString(8,  s.getCourse());
            ps.setInt(9,     s.getYear());
            ps.setDouble(10, s.getGpa());
            ps.setString(11, s.getStatus());
            ps.setInt(12,    s.getStudentId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("  [DB Error] updateStudent: " + e.getMessage());
            return false;
        }
    }

    /** Quick GPA update — only changes GPA for a student. */
    public boolean updateGpa(int studentId, double newGpa) {
        String sql = "UPDATE students SET gpa = ? WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newGpa);
            ps.setInt(2, studentId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("  [DB Error] updateGpa: " + e.getMessage());
            return false;
        }
    }

    /** Quick status update. */
    public boolean updateStatus(int studentId, String newStatus) {
        String sql = "UPDATE students SET status = ? WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, studentId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("  [DB Error] updateStatus: " + e.getMessage());
            return false;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  DELETE
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Permanently deletes a student by ID.
     * @return true if the record was deleted
     */
    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("  [DB Error] deleteStudent: " + e.getMessage());
            return false;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STATISTICS
    // ══════════════════════════════════════════════════════════════════════════

    public int getTotalCount() {
        return getSingleInt("SELECT COUNT(*) FROM students");
    }

    public int getActiveCount() {
        return getSingleInt("SELECT COUNT(*) FROM students WHERE status = 'Active'");
    }

    public double getAverageGpa() {
        String sql = "SELECT AVG(gpa) FROM students WHERE status = 'Active'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("  [DB Error] getAverageGpa: " + e.getMessage());
        }
        return 0.0;
    }

    public String getHighestGpaStudent() {
        String sql = "SELECT name, gpa FROM students ORDER BY gpa DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getString("name") + " (" + rs.getDouble("gpa") + ")";
        } catch (SQLException e) {
            System.err.println("  [DB Error] getHighestGpaStudent: " + e.getMessage());
        }
        return "N/A";
    }

    /** Returns department-wise student count as a formatted list. */
    public List<String[]> getDeptStats() {
        List<String[]> list = new ArrayList<>();
        String sql = """
            SELECT department,
                   COUNT(*) AS total,
                   AVG(gpa) AS avg_gpa
            FROM students
            GROUP BY department
            ORDER BY total DESC
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("department"),
                    String.valueOf(rs.getInt("total")),
                    String.format("%.2f", rs.getDouble("avg_gpa"))
                });
            }
        } catch (SQLException e) {
            System.err.println("  [DB Error] getDeptStats: " + e.getMessage());
        }
        return list;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ══════════════════════════════════════════════════════════════════════════

    /** Maps a ResultSet row to a Student object. */
    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setStudentId(rs.getInt("student_id"));
        s.setName(rs.getString("name"));
        s.setEmail(rs.getString("email"));
        s.setPhone(rs.getString("phone"));
        s.setDateOfBirth(rs.getDate("date_of_birth"));
        s.setGender(rs.getString("gender"));
        s.setAddress(rs.getString("address"));
        s.setDepartment(rs.getString("department"));
        s.setCourse(rs.getString("course"));
        s.setYear(rs.getInt("year"));
        s.setGpa(rs.getDouble("gpa"));
        s.setStatus(rs.getString("status"));
        s.setCreatedAt(rs.getTimestamp("created_at"));
        s.setUpdatedAt(rs.getTimestamp("updated_at"));
        return s;
    }

    private int getSingleInt(String sql) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("  [DB Error]: " + e.getMessage());
        }
        return 0;
    }
}
