package com.srms.menu;

import com.srms.dao.StudentDAO;
import com.srms.model.Student;
import com.srms.util.ConsoleUtils;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import static com.srms.util.ConsoleUtils.*;

/**
 * StudentService — handles all console I/O and user interaction.
 * Calls StudentDAO for every DB operation.
 * Demonstrates OOP: Abstraction (UI logic separated from data logic).
 */
public class StudentService {

    private final StudentDAO dao     = new StudentDAO();
    private final Scanner    scanner = new Scanner(System.in);

    // ══════════════════════════════════════════════════════════════════════════
    //  ADD STUDENT
    // ══════════════════════════════════════════════════════════════════════════

    public void addStudent() {
        printSubHeader("➕  ADD NEW STUDENT");

        Student s = new Student();

        s.setName(readRequired("Full Name"));
        s.setEmail(readRequired("Email Address"));
        s.setPhone(readOptional("Phone Number"));

        // Date of birth
        String dob = readOptional("Date of Birth (YYYY-MM-DD)");
        if (!dob.isEmpty()) {
            try { s.setDateOfBirth(Date.valueOf(dob)); }
            catch (IllegalArgumentException e) { warn("Invalid date — skipped."); }
        }

        // Gender
        System.out.println();
        info("Gender options:  1. Male   2. Female   3. Other");
        String gChoice = readOptional("Choose (1/2/3)");
        s.setGender(switch (gChoice) {
            case "1" -> "Male";
            case "2" -> "Female";
            case "3" -> "Other";
            default  -> "Male";
        });

        s.setAddress(readOptional("Address"));
        s.setDepartment(readRequired("Department  (e.g. Computer Science)"));
        s.setCourse(readRequired("Course       (e.g. B.Tech CSE)"));

        int year = readInt("Current Year (1-6)", 1, 6);
        s.setYear(year);

        double gpa = readDouble("GPA (0.00 - 10.00)", 0.0, 10.0);
        s.setGpa(gpa);

        // Status
        System.out.println();
        info("Status: 1. Active   2. Inactive   3. Graduated   4. Dropped");
        String stChoice = readOptional("Choose (1-4, default=1)");
        s.setStatus(switch (stChoice) {
            case "2" -> "Inactive";
            case "3" -> "Graduated";
            case "4" -> "Dropped";
            default  -> "Active";
        });

        printLine();
        System.out.println(B_YELLOW + "\n  Review before saving:" + RESET);
        printStudentCompact(s);
        System.out.println();
        prompt("Confirm save? (y/n):");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            if (dao.addStudent(s)) {
                success("Student added! Assigned ID: " + B_GREEN + s.getStudentId() + RESET);
            } else {
                error("Failed to add student. Email may already exist.");
            }
        } else {
            warn("Operation cancelled.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  VIEW ALL STUDENTS
    // ══════════════════════════════════════════════════════════════════════════

    public void viewAllStudents() {
        printSubHeader("📋  ALL STUDENTS");
        List<Student> list = dao.getAllStudents();
        if (list.isEmpty()) {
            warn("No students found in the database.");
            return;
        }
        printTable(list);
        System.out.println();
        info("Total records: " + B_WHITE + list.size() + RESET);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  VIEW SINGLE STUDENT
    // ══════════════════════════════════════════════════════════════════════════

    public void viewStudentById() {
        printSubHeader("🔍  VIEW STUDENT DETAILS");
        int id = readInt("Enter Student ID", 1, Integer.MAX_VALUE);
        Student s = dao.getStudentById(id);
        if (s == null) {
            error("No student found with ID: " + id);
        } else {
            printStudentDetail(s);
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SEARCH
    // ══════════════════════════════════════════════════════════════════════════

    public void searchStudents() {
        printSubHeader("🔎  SEARCH STUDENTS");
        prompt("Enter keyword (name / email / dept / course / phone):");
        String keyword = scanner.nextLine().trim();
        if (keyword.isEmpty()) { warn("Please enter a search keyword."); return; }

        List<Student> results = dao.searchStudents(keyword);
        if (results.isEmpty()) {
            warn("No students found matching: \"" + keyword + "\"");
        } else {
            info("Found " + results.size() + " result(s) for \"" + keyword + "\":");
            System.out.println();
            printTable(results);
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  UPDATE STUDENT
    // ══════════════════════════════════════════════════════════════════════════

    public void updateStudent() {
        printSubHeader("✏️   UPDATE STUDENT RECORD");
        int id = readInt("Enter Student ID to update", 1, Integer.MAX_VALUE);

        Student existing = dao.getStudentById(id);
        if (existing == null) {
            error("No student found with ID: " + id);
            return;
        }

        System.out.println();
        info("Current record (press ENTER to keep existing value):");
        printStudentDetail(existing);
        System.out.println();

        // Read each field — keep existing if user presses ENTER
        String name = readWithDefault("Name", existing.getName());
        existing.setName(name);

        String email = readWithDefault("Email", existing.getEmail());
        existing.setEmail(email);

        String phone = readWithDefault("Phone", existing.getPhone());
        existing.setPhone(phone);

        String dept = readWithDefault("Department", existing.getDepartment());
        existing.setDepartment(dept);

        String course = readWithDefault("Course", existing.getCourse());
        existing.setCourse(course);

        System.out.print(CYAN + "  » " + RESET + "Year [" + YELLOW + existing.getYear() + RESET + "]: ");
        String yearIn = scanner.nextLine().trim();
        if (!yearIn.isEmpty()) {
            try { existing.setYear(Integer.parseInt(yearIn)); }
            catch (NumberFormatException e) { warn("Invalid — keeping old value."); }
        }

        System.out.print(CYAN + "  » " + RESET + "GPA [" + YELLOW + existing.getGpa() + RESET + "]: ");
        String gpaIn = scanner.nextLine().trim();
        if (!gpaIn.isEmpty()) {
            try { existing.setGpa(Double.parseDouble(gpaIn)); }
            catch (NumberFormatException e) { warn("Invalid — keeping old value."); }
        }

        System.out.print(CYAN + "  » " + RESET + "Address [" + YELLOW + existing.getAddress() + RESET + "]: ");
        String addrIn = scanner.nextLine().trim();
        if (!addrIn.isEmpty()) existing.setAddress(addrIn);

        info("Status: 1. Active   2. Inactive   3. Graduated   4. Dropped");
        System.out.print(CYAN + "  » " + RESET + "Status [" + YELLOW + existing.getStatus() + RESET + "]: ");
        String stIn = scanner.nextLine().trim();
        if (!stIn.isEmpty()) {
            existing.setStatus(switch (stIn) {
                case "1" -> "Active";
                case "2" -> "Inactive";
                case "3" -> "Graduated";
                case "4" -> "Dropped";
                default  -> existing.getStatus();
            });
        }

        printLine();
        System.out.println();
        prompt("Save changes? (y/n):");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            if (dao.updateStudent(existing)) {
                success("Student record updated successfully!");
            } else {
                error("Update failed.");
            }
        } else {
            warn("Update cancelled.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  DELETE STUDENT
    // ══════════════════════════════════════════════════════════════════════════

    public void deleteStudent() {
        printSubHeader("🗑️   DELETE STUDENT RECORD");
        int id = readInt("Enter Student ID to delete", 1, Integer.MAX_VALUE);

        Student s = dao.getStudentById(id);
        if (s == null) {
            error("No student found with ID: " + id);
            return;
        }

        System.out.println();
        warn("You are about to permanently delete:");
        printStudentCompact(s);
        System.out.println();
        prompt(B_RED + "Type DELETE to confirm, or anything else to cancel:" + RESET);
        String confirm = scanner.nextLine().trim();

        if (confirm.equals("DELETE")) {
            if (dao.deleteStudent(id)) {
                success("Student " + s.getName() + " (ID: " + id + ") deleted.");
            } else {
                error("Delete failed.");
            }
        } else {
            warn("Delete cancelled.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  FILTER BY DEPARTMENT
    // ══════════════════════════════════════════════════════════════════════════

    public void filterByDepartment() {
        printSubHeader("🏛️   FILTER BY DEPARTMENT");
        prompt("Enter department name:");
        String dept = scanner.nextLine().trim();
        if (dept.isEmpty()) { warn("Department name cannot be empty."); return; }

        List<Student> list = dao.getStudentsByDepartment(dept);
        if (list.isEmpty()) {
            warn("No students found in department: " + dept);
        } else {
            info(list.size() + " student(s) in " + dept + ":");
            System.out.println();
            printTable(list);
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  TOP STUDENTS
    // ══════════════════════════════════════════════════════════════════════════

    public void showTopStudents() {
        printSubHeader("🏆  TOP STUDENTS BY GPA");
        int n = readInt("How many top students to show? (1-20)", 1, 20);
        List<Student> list = dao.getTopStudents(n);
        if (list.isEmpty()) {
            warn("No active students found.");
            return;
        }
        System.out.println();

        // Print ranked list with GPA bars
        System.out.printf("  %s%-4s  %-22s  %-20s  %s%s%n",
            B_YELLOW, "Rank", "Name", "Department", "GPA", RESET);
        printLine();

        String[] medals = {"🥇", "🥈", "🥉"};
        for (int i = 0; i < list.size(); i++) {
            Student s = list.get(i);
            String rank = i < medals.length ? medals[i] : "  " + (i + 1);
            System.out.printf("  %-5s  %-22s  %-20s  %s%n",
                rank,
                cell(s.getName(), 22),
                cell(s.getDepartment(), 20),
                gpaBar(s.getGpa()));
        }
        printLine();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  DASHBOARD / STATISTICS
    // ══════════════════════════════════════════════════════════════════════════

    public void showDashboard() {
        printSubHeader("📊  DASHBOARD & STATISTICS");

        int total    = dao.getTotalCount();
        int active   = dao.getActiveCount();
        double avgGpa= dao.getAverageGpa();
        String topS  = dao.getHighestGpaStudent();
        List<String[]> depts = dao.getDeptStats();

        // Summary cards
        System.out.println();
        System.out.println(CYAN + "  ┌──────────────────────┬──────────────────────┐" + RESET);
        System.out.printf(CYAN  + "  │" + RESET + B_YELLOW + "  Total Students      " + RESET + CYAN + "│" + RESET + B_GREEN  + "  Active Students     " + RESET + CYAN + "│%n" + RESET);
        System.out.printf(CYAN  + "  │" + RESET + B_WHITE  + "  %-20s  " + RESET + CYAN + "│" + RESET + B_WHITE  + "  %-20s  " + RESET + CYAN + "│%n" + RESET,
            total, active);
        System.out.println(CYAN + "  ├──────────────────────┼──────────────────────┤" + RESET);
        System.out.printf(CYAN  + "  │" + RESET + B_YELLOW + "  Average GPA         " + RESET + CYAN + "│" + RESET + B_YELLOW + "  Top Student         " + RESET + CYAN + "│%n" + RESET);
        System.out.printf(CYAN  + "  │" + RESET + B_WHITE  + "  %-20s  " + RESET + CYAN + "│" + RESET + B_WHITE  + "  %-20s  " + RESET + CYAN + "│%n" + RESET,
            String.format("%.2f / 10.00", avgGpa), cell(topS, 20));
        System.out.println(CYAN + "  └──────────────────────┴──────────────────────┘" + RESET);

        // Department breakdown
        if (!depts.isEmpty()) {
            System.out.println();
            System.out.println(B_CYAN + "  Department Breakdown:" + RESET);
            printLine();
            System.out.printf("  %s%-28s  %6s  %s%s%n", B_YELLOW, "Department", "Count", "Avg GPA", RESET);
            printLine();
            int maxCount = depts.stream().mapToInt(d -> Integer.parseInt(d[1])).max().orElse(1);
            for (String[] d : depts) {
                int count = Integer.parseInt(d[1]);
                int barLen = (int) ((count * 1.0 / maxCount) * 20);
                System.out.printf("  %-28s  %s%6s%s  %s  %s%.2f%s%n",
                    cell(d[0], 28),
                    B_WHITE, d[1], RESET,
                    GREEN + "▪".repeat(barLen) + RESET,
                    YELLOW, Double.parseDouble(d[2]), RESET);
            }
            printLine();
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  QUICK GPA UPDATE
    // ══════════════════════════════════════════════════════════════════════════

    public void quickUpdateGpa() {
        printSubHeader("⭐  QUICK GPA UPDATE");
        int id = readInt("Enter Student ID", 1, Integer.MAX_VALUE);
        Student s = dao.getStudentById(id);
        if (s == null) { error("Student not found."); return; }
        info("Current GPA for " + s.getName() + ": " + s.getGpa());
        double newGpa = readDouble("New GPA (0.00 - 10.00)", 0.0, 10.0);
        if (dao.updateGpa(id, newGpa)) {
            success("GPA updated to " + newGpa + " for " + s.getName());
        } else {
            error("GPA update failed.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  DISPLAY HELPERS
    // ══════════════════════════════════════════════════════════════════════════

    /** Prints a table of students with headers. */
    private void printTable(List<Student> list) {
        // Header
        System.out.printf("  %s%-5s  %-22s  %-12s  %-20s  %-8s  %-10s  %-12s%s%n",
            B_YELLOW,
            "ID", "Name", "Phone", "Department", "Year", "GPA", "Status",
            RESET);
        printLine();

        for (Student s : list) {
            String gpaColor = s.getGpa() >= 8.5 ? B_GREEN : s.getGpa() >= 7.0 ? YELLOW : RED;
            String statusCol = switch (s.getStatus() == null ? "" : s.getStatus()) {
                case "Active"    -> B_GREEN;
                case "Graduated" -> B_BLUE;
                case "Dropped"   -> B_RED;
                default          -> YELLOW;
            };
            System.out.printf("  %-5s  %-22s  %-12s  %-20s  %-8s  %s%-10s%s  %s%-12s%s%n",
                CYAN + s.getStudentId() + RESET,
                WHITE + cell(s.getName(), 22) + RESET,
                cell(s.getPhone(), 12),
                cell(s.getDepartment(), 20),
                s.getYearLabel(),
                gpaColor, String.format("%.2f", s.getGpa()), RESET,
                statusCol, s.getStatus(), RESET);
        }
        printLine();
    }

    /** Prints a single student in a detailed card view. */
    private void printStudentDetail(Student s) {
        System.out.println();
        System.out.println(CYAN + "  ┌──────────────────────────────────────────────────────────────┐" + RESET);
        System.out.printf(  CYAN + "  │" + RESET + B_BLUE + "  STUDENT PROFILE   " + RESET + CYAN + "%-43s│%n" + RESET, "");
        System.out.println(CYAN + "  ├──────────────────────────────────────────────────────────────┤" + RESET);

        field("Student ID",    String.valueOf(s.getStudentId()));
        field("Name",          s.getName());
        field("Email",         s.getEmail());
        field("Phone",         s.getPhone());
        field("Date of Birth", s.getDateOfBirth() != null ? s.getDateOfBirth().toString() : "—");
        field("Gender",        s.getGender());
        field("Address",       s.getAddress());

        System.out.println(CYAN + "  ├──────────────────────────────────────────────────────────────┤" + RESET);

        field("Department",    s.getDepartment());
        field("Course",        s.getCourse());
        field("Year",          s.getYearLabel());
        System.out.println("  " + YELLOW + String.format("%-18s", "GPA") + RESET + " : " + gpaBar(s.getGpa()));
        System.out.println("  " + YELLOW + String.format("%-18s", "Status") + RESET + " : " + statusBadge(s.getStatus()));

        System.out.println(CYAN + "  ├──────────────────────────────────────────────────────────────┤" + RESET);

        if (s.getCreatedAt() != null)
            field("Added On",  s.getCreatedAt().toString().substring(0, 19));
        if (s.getUpdatedAt() != null)
            field("Updated On",s.getUpdatedAt().toString().substring(0, 19));

        System.out.println(CYAN + "  └──────────────────────────────────────────────────────────────┘" + RESET);
        System.out.println();
    }

    /** Compact single-line display for confirmation prompts. */
    private void printStudentCompact(Student s) {
        System.out.println();
        System.out.printf("  %s%-18s%s: %s%s%s%n", YELLOW,"Name",       RESET, WHITE, s.getName(), RESET);
        System.out.printf("  %s%-18s%s: %s%s%s%n", YELLOW,"Email",      RESET, WHITE, s.getEmail(), RESET);
        System.out.printf("  %s%-18s%s: %s%s%s%n", YELLOW,"Department", RESET, WHITE, s.getDepartment(), RESET);
        System.out.printf("  %s%-18s%s: %s%s%s%n", YELLOW,"GPA",        RESET, WHITE, s.getGpa(), RESET);
        System.out.printf("  %s%-18s%s: %s%n",     YELLOW,"Status",     RESET, statusBadge(s.getStatus()));
        System.out.println();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  INPUT HELPERS
    // ══════════════════════════════════════════════════════════════════════════

    private String readRequired(String label) {
        while (true) {
            prompt(label + " *:");
            String val = scanner.nextLine().trim();
            if (!val.isEmpty()) return val;
            warn("This field is required.");
        }
    }

    private String readOptional(String label) {
        prompt(label + " (optional):");
        return scanner.nextLine().trim();
    }

    private String readWithDefault(String label, String current) {
        System.out.print(CYAN + "  » " + RESET + label
            + " [" + YELLOW + current + RESET + "]: ");
        String val = scanner.nextLine().trim();
        return val.isEmpty() ? current : val;
    }

    private int readInt(String label, int min, int max) {
        while (true) {
            prompt(label + " (" + min + "-" + (max == Integer.MAX_VALUE ? "any" : max) + "):");
            try {
                int v = Integer.parseInt(scanner.nextLine().trim());
                if (v >= min && v <= max) return v;
                warn("Value must be between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                warn("Please enter a valid number.");
            }
        }
    }

    private double readDouble(String label, double min, double max) {
        while (true) {
            prompt(label + ":");
            try {
                double v = Double.parseDouble(scanner.nextLine().trim());
                if (v >= min && v <= max) return v;
                warn("Value must be between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                warn("Please enter a valid number (e.g. 8.50).");
            }
        }
    }
}
