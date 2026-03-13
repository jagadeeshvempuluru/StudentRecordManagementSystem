-- ============================================
--  Student Record Management System
--  Database Schema
-- ============================================

CREATE DATABASE IF NOT EXISTS student_record_db;
USE student_record_db;

DROP TABLE IF EXISTS students;

CREATE TABLE students (
    student_id    INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100)        NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    phone         VARCHAR(15),
    date_of_birth DATE,
    gender        ENUM('Male','Female','Other'),
    address       VARCHAR(255),
    department    VARCHAR(100),
    course        VARCHAR(100),
    year          INT CHECK (year BETWEEN 1 AND 6),
    gpa           DECIMAL(4,2) DEFAULT 0.00,
    status        ENUM('Active','Inactive','Graduated','Dropped') DEFAULT 'Active',
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ── Sample Data ───────────────────────────────────────────────────────────────
INSERT INTO students (name, email, phone, date_of_birth, gender, address,
                      department, course, year, gpa, status)
VALUES
('Aarav Sharma',   'aarav.sharma@edu.in',   '9876543210', '2002-05-15', 'Male',   'Hyderabad, Telangana',  'Computer Science', 'B.Tech CSE', 3, 8.75, 'Active'),
('Priya Patel',    'priya.patel@edu.in',    '9876543211', '2003-03-22', 'Female', 'Bangalore, Karnataka',  'Information Tech', 'B.Tech IT',  2, 9.10, 'Active'),
('Rohan Verma',    'rohan.verma@edu.in',    '9876543212', '2002-11-08', 'Male',   'Mumbai, Maharashtra',   'Computer Science', 'B.Tech CSE', 3, 7.80, 'Active'),
('Sneha Gupta',    'sneha.gupta@edu.in',    '9876543213', '2003-07-19', 'Female', 'Delhi, India',          'Electronics',      'B.Tech ECE', 2, 8.90, 'Active'),
('Arjun Singh',    'arjun.singh@edu.in',    '9876543214', '2002-01-30', 'Male',   'Pune, Maharashtra',     'Mechanical',       'B.Tech ME',  3, 7.50, 'Active'),
('Kavya Nair',     'kavya.nair@edu.in',     '9876543215', '2003-09-12', 'Female', 'Chennai, Tamil Nadu',   'Information Tech', 'B.Tech IT',  2, 9.30, 'Active'),
('Vikram Reddy',   'vikram.reddy@edu.in',   '9876543216', '2001-06-25', 'Male',   'Hyderabad, Telangana',  'Computer Science', 'B.Tech CSE', 4, 8.20, 'Active'),
('Ananya Joshi',   'ananya.joshi@edu.in',   '9876543217', '2002-12-04', 'Female', 'Kolkata, West Bengal',  'Civil Engineering','B.Tech CE',  3, 8.70, 'Active');

SELECT 'Database setup complete!' AS message;
SELECT COUNT(*) AS total_students FROM students;
