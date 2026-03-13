package com.srms.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Student Model — represents one row in the students table.
 * Demonstrates OOP: Encapsulation (private fields + getters/setters).
 */
public class Student {

    // ── Private fields (Encapsulation) ────────────────────────────────────────
    private int       studentId;
    private String    name;
    private String    email;
    private String    phone;
    private Date      dateOfBirth;
    private String    gender;
    private String    address;
    private String    department;
    private String    course;
    private int       year;
    private double    gpa;
    private String    status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Student() {}

    /** Constructor for creating a new student (no id/timestamps yet). */
    public Student(String name, String email, String phone, Date dateOfBirth,
                   String gender, String address, String department,
                   String course, int year, double gpa, String status) {
        this.name        = name;
        this.email       = email;
        this.phone       = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender      = gender;
        this.address     = address;
        this.department  = department;
        this.course      = course;
        this.year        = year;
        this.gpa         = gpa;
        this.status      = status;
    }

    // ── Utility ───────────────────────────────────────────────────────────────

    /** Returns year as ordinal string: 1 → "1st Year", 2 → "2nd Year", etc. */
    public String getYearLabel() {
        return switch (year) {
            case 1 -> "1st Year";
            case 2 -> "2nd Year";
            case 3 -> "3rd Year";
            case 4 -> "4th Year";
            case 5 -> "5th Year";
            case 6 -> "6th Year";
            default -> year + " Year";
        };
    }

    @Override
    public String toString() {
        return String.format("Student{id=%d, name='%s', dept='%s', gpa=%.2f, status='%s'}",
                studentId, name, department, gpa, status);
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int getStudentId()            { return studentId; }
    public void setStudentId(int id)     { this.studentId = id; }

    public String getName()              { return name; }
    public void setName(String name)     { this.name = name; }

    public String getEmail()             { return email; }
    public void setEmail(String email)   { this.email = email; }

    public String getPhone()             { return phone; }
    public void setPhone(String phone)   { this.phone = phone; }

    public Date getDateOfBirth()                     { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth)     { this.dateOfBirth = dateOfBirth; }

    public String getGender()              { return gender; }
    public void setGender(String gender)   { this.gender = gender; }

    public String getAddress()               { return address; }
    public void setAddress(String address)   { this.address = address; }

    public String getDepartment()                  { return department; }
    public void setDepartment(String department)   { this.department = department; }

    public String getCourse()              { return course; }
    public void setCourse(String course)   { this.course = course; }

    public int getYear()           { return year; }
    public void setYear(int year)  { this.year = year; }

    public double getGpa()         { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public String getStatus()              { return status; }
    public void setStatus(String status)   { this.status = status; }

    public Timestamp getCreatedAt()                  { return createdAt; }
    public void setCreatedAt(Timestamp createdAt)    { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt()                  { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt)    { this.updatedAt = updatedAt; }
}
