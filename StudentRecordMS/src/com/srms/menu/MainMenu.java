package com.srms.menu;

import com.srms.util.ConsoleUtils;
import com.srms.util.DBConnection;

import java.util.Scanner;

import static com.srms.util.ConsoleUtils.*;

/**
 * MainMenu — Application entry point.
 *
 * Displays the interactive console menu and delegates actions
 * to StudentService, which calls StudentDAO for all DB operations.
 *
 * Architecture:
 *   Main → MainMenu → StudentService → StudentDAO → MySQL (via JDBC)
 */
public class MainMenu {

    private static final Scanner       scanner = new Scanner(System.in);
    private static final StudentService service = new StudentService();

    // ── Entry point ───────────────────────────────────────────────────────────
    public static void main(String[] args) {

        // Test DB connection on startup
        ConsoleUtils.printBanner();
        System.out.print(B_CYAN + "  Connecting to database..." + RESET);

        if (!DBConnection.testConnection()) {
            System.out.println();
            error("Cannot connect to MySQL database!");
            warn("Please check:");
            System.out.println(YELLOW + "    1. MySQL server is running" + RESET);
            System.out.println(YELLOW + "    2. Database 'student_record_db' exists  (run sql/schema.sql)" + RESET);
            System.out.println(YELLOW + "    3. Password is correct in DBConnection.java" + RESET);
            System.out.println(YELLOW + "    4. mysql-connector-java.jar is in your classpath" + RESET);
            System.out.println();
            System.exit(1);
        }
        System.out.println(B_GREEN + " Connected ✔" + RESET);
        System.out.println();

        // Run menu loop
        run();

        // Cleanup
        DBConnection.closeConnection();
        System.out.println();
        System.out.println(B_BLUE + "  Thank you for using Student Record Management System!" + RESET);
        System.out.println();
    }

    // ── Main menu loop ────────────────────────────────────────────────────────
    private static void run() {
        while (true) {
            printMainMenu();
            prompt("Choose an option:");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1"  -> { service.viewAllStudents();     pause(); }
                case "2"  -> { service.viewStudentById();     pause(); }
                case "3"  -> { service.searchStudents();      pause(); }
                case "4"  -> { service.filterByDepartment();  pause(); }
                case "5"  -> { service.showTopStudents();     pause(); }
                case "6"  -> { service.showDashboard();       pause(); }
                case "7"  -> { service.addStudent();          pause(); }
                case "8"  -> { service.updateStudent();       pause(); }
                case "9"  -> { service.quickUpdateGpa();      pause(); }
                case "10" -> { service.deleteStudent();       pause(); }
                case "0"  -> { return; }
                default   -> {
                    warn("Invalid option. Please enter a number from the menu.");
                    pause();
                }
            }
        }
    }

    // ── Menu layout ───────────────────────────────────────────────────────────
    private static void printMainMenu() {
        System.out.println();
        printDoubleLine();
        System.out.println(B_BLUE + "   🎓  STUDENT RECORD MANAGEMENT SYSTEM  —  MAIN MENU" + RESET);
        printDoubleLine();

        // READ section
        System.out.println();
        System.out.println(B_CYAN + "   📋  VIEW & SEARCH" + RESET);
        menuItem("1",  "View All Students");
        menuItem("2",  "View Student by ID");
        menuItem("3",  "Search Students  (name / email / dept / phone)");
        menuItem("4",  "Filter by Department");
        menuItem("5",  "Top Students by GPA");
        menuItem("6",  "Dashboard & Statistics");

        // WRITE section
        System.out.println();
        System.out.println(B_YELLOW + "   ✏️   MANAGE RECORDS" + RESET);
        menuItem("7",  "Add New Student");
        menuItem("8",  "Update Student Record");
        menuItem("9",  "Quick GPA Update");
        menuItem("10", "Delete Student");

        // Exit
        System.out.println();
        System.out.println(B_RED + "   ⏻   EXIT" + RESET);
        menuItem("0",  "Exit Application");
        System.out.println();
        printLine();
    }

    private static void menuItem(String num, String label) {
        System.out.printf("     %s[%s%-2s%s]%s  %s%n",
            CYAN, B_WHITE, num, CYAN, RESET, label);
    }

    private static void pause() {
        pauseMsg();
        scanner.nextLine();
    }
}
