# 🎓 Student Record Management System
### Console-Based Java Application | Core Java • JDBC • MySQL • Eclipse IDE

---

## 📋 Project Overvie

A fully functional **console-based Student Record Management System** that performs
complete CRUD operations (Create, Read, Update, Delete) on a MySQL database using
Java JDBC.

### Key Features
- ✅ Add new student records with full validation
- ✅ View all students in a formatted table
- ✅ View single student in a detailed profile card
- ✅ Search students by name, email, department, phone
- ✅ Filter students by department
- ✅ Top students ranking by GPA (with medals 🥇🥈🥉)
- ✅ Dashboard with statistics & department breakdown charts
- ✅ Full update of any student record
- ✅ Quick GPA update
- ✅ Delete student with confirmation
- ✅ Colorful ANSI terminal UI

---

## 🗂️ Project Structure

```
StudentRecordMS/
├── .classpath                          ← Eclipse classpath config
├── .project                            ← Eclipse project config
├── MANIFEST.MF                         ← For runnable JAR
├── README.md
│
├── sql/
│   └── schema.sql                      ← DB schema + sample data
│
├── lib/
│   └── mysql-connector-j-8.0.33.jar   ← JDBC driver (download separately)
│
└── src/
    └── com/srms/
        ├── model/
        │   └── Student.java            ← POJO / Entity class
        ├── dao/
        │   └── StudentDAO.java         ← All DB operations (CRUD)
        ├── menu/
        │   ├── MainMenu.java           ← Entry point + menu loop
        │   └── StudentService.java     ← Console I/O + business logic
        └── util/
            ├── DBConnection.java       ← JDBC connection manager
            └── ConsoleUtils.java       ← Colors, formatting, UI helpers
```

---

## ⚡ Quick Setup (5 Steps)

### Step 1 — MySQL: Create Database

Open MySQL Workbench or MySQL CLI and run:
```sql
mysql -u root -p < sql/schema.sql
```
Or open `sql/schema.sql` in MySQL Workbench and execute it.

This creates:
- Database: `student_record_db`
- Table: `students` (14 columns)
- 8 sample student records

---

### Step 2 — Download MySQL JDBC Driver

Download: https://dev.mysql.com/downloads/connector/j/

- Choose "Platform Independent" → download ZIP
- Extract and copy `mysql-connector-j-8.0.33.jar` into the `lib/` folder

---

### Step 3 — Set Your DB Password

Open `src/com/srms/util/DBConnection.java` and change line:
```java
private static final String PASSWORD = "your_password";  // ← Your MySQL root password
```

---

### Step 4a — Run in Eclipse IDE

1. Open Eclipse → **File → Import → Existing Projects into Workspace**
2. Browse to the `StudentRecordMS` folder → Click **Finish**
3. Right-click project → **Build Path → Add External JARs** → select `lib/mysql-connector-j-8.0.33.jar`
4. Open `src/com/srms/menu/MainMenu.java`
5. Right-click → **Run As → Java Application**

---

### Step 4b — Run from Command Line

```bash
# Navigate to project root
cd StudentRecordMS

# Create bin directory
mkdir -p bin

# Compile all Java files
javac -cp "lib/mysql-connector-j-8.0.33.jar" \
      -d bin \
      src/com/srms/util/DBConnection.java \
      src/com/srms/util/ConsoleUtils.java \
      src/com/srms/model/Student.java \
      src/com/srms/dao/StudentDAO.java \
      src/com/srms/menu/StudentService.java \
      src/com/srms/menu/MainMenu.java

# Run the application
java -cp "bin:lib/mysql-connector-j-8.0.33.jar" com.srms.menu.MainMenu

# Windows (use semicolons):
java -cp "bin;lib/mysql-connector-j-8.0.33.jar" com.srms.menu.MainMenu
```

---

### Step 4c — Build Runnable JAR

```bash
jar cfm StudentRecordMS.jar MANIFEST.MF -C bin .
java -jar StudentRecordMS.jar
```

---

## 🗃️ Database Schema

```sql
CREATE TABLE students (
    student_id    INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    phone         VARCHAR(15),
    date_of_birth DATE,
    gender        ENUM('Male','Female','Other'),
    address       VARCHAR(255),
    department    VARCHAR(100),
    course        VARCHAR(100),
    year          INT CHECK (year BETWEEN 1 AND 6),
    gpa           DECIMAL(4,2) DEFAULT 0.00,
    status        ENUM('Active','Inactive','Graduated','Dropped'),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

---

## 🖥️ Sample Console Output

```
  ╔══════════════════════════════════════════════════════════════════╗
  ║                                                                  ║
  ║        🎓  STUDENT RECORD MANAGEMENT SYSTEM                     ║
  ║              Core Java  •  JDBC  •  MySQL                       ║
  ║                                                                  ║
  ╚══════════════════════════════════════════════════════════════════╝

  Connecting to database... Connected ✔

═══════════════════════════════════════════════════════════════════════
   🎓  STUDENT RECORD MANAGEMENT SYSTEM  —  MAIN MENU
═══════════════════════════════════════════════════════════════════════

   📋  VIEW & SEARCH
     [1 ]  View All Students
     [2 ]  View Student by ID
     [3 ]  Search Students  (name / email / dept / phone)
     [4 ]  Filter by Department
     [5 ]  Top Students by GPA
     [6 ]  Dashboard & Statistics

   ✏️   MANAGE RECORDS
     [7 ]  Add New Student
     [8 ]  Update Student Record
     [9 ]  Quick GPA Update
     [10]  Delete Student

   ⏻   EXIT
     [0 ]  Exit Application
```

---

## 🔑 OOP Concepts Used

| Concept | Where Used |
|---------|-----------|
| **Encapsulation** | `Student.java` — private fields, public getters/setters |
| **Abstraction** | `StudentDAO.java` — hides SQL behind clean method names |
| **Inheritance** | Conceptual — `Student` model extended to custom toString() |
| **Single Responsibility** | Each class has one job: Model, DAO, Service, Menu, Util |

---

## 🛡️ Security & Best Practices

- ✅ **PreparedStatement** — prevents SQL Injection on all queries
- ✅ **try-with-resources** — auto-closes Connection/ResultSet (no leaks)
- ✅ **Input validation** — all user inputs validated before DB calls
- ✅ **Exception handling** — all SQL errors caught and reported gracefully
- ✅ **Singleton DB connection** — reuses one connection efficiently

---

## 📦 Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| `mysql-connector-j` | 8.0.33 | JDBC driver for MySQL |
| Java SE | 11+ | Language & standard library |

No other external dependencies required.

---

## ❌ Troubleshooting

| Problem | Fix |
|---------|-----|
| `Communications link failure` | MySQL server not running. Start it. |
| `Access denied for user 'root'` | Wrong password in `DBConnection.java` |
| `Unknown database 'student_record_db'` | Run `sql/schema.sql` in MySQL first |
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | JAR not in classpath — re-check Step 3 |
| Colors not showing in terminal | Use a terminal that supports ANSI (not Windows CMD) — use Git Bash or Windows Terminal |

---

*Student Record Management System — Core Java Mini Project*
