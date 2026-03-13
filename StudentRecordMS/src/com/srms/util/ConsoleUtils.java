package com.srms.util;

/**
 * Console colors and UI helper methods for a rich terminal experience.
 */
public class ConsoleUtils {

    // ANSI color codes
    public static final String RESET   = "\033[0m";
    public static final String BOLD    = "\033[1m";
    public static final String RED     = "\033[0;31m";
    public static final String GREEN   = "\033[0;32m";
    public static final String YELLOW  = "\033[0;33m";
    public static final String BLUE    = "\033[0;34m";
    public static final String PURPLE  = "\033[0;35m";
    public static final String CYAN    = "\033[0;36m";
    public static final String WHITE   = "\033[0;37m";
    public static final String B_RED   = "\033[1;31m";
    public static final String B_GREEN = "\033[1;32m";
    public static final String B_YELLOW= "\033[1;33m";
    public static final String B_BLUE  = "\033[1;34m";
    public static final String B_CYAN  = "\033[1;36m";
    public static final String B_WHITE = "\033[1;37m";
    public static final String BG_BLUE = "\033[44m";
    public static final String BG_GREEN= "\033[42m";

    private static final int WIDTH = 70;

    // ── Dividers ──────────────────────────────────────────────────────────────

    public static void printLine() {
        System.out.println(CYAN + "─".repeat(WIDTH) + RESET);
    }

    public static void printDoubleLine() {
        System.out.println(BLUE + "═".repeat(WIDTH) + RESET);
    }

    public static void printThinLine() {
        System.out.println(CYAN + "·".repeat(WIDTH) + RESET);
    }

    // ── Section headers ───────────────────────────────────────────────────────

    public static void printHeader(String title) {
        System.out.println();
        printDoubleLine();
        int padding = (WIDTH - title.length()) / 2;
        System.out.println(B_BLUE + " ".repeat(Math.max(0, padding)) + title + RESET);
        printDoubleLine();
    }

    public static void printSubHeader(String title) {
        printLine();
        System.out.println(B_CYAN + "  " + title + RESET);
        printLine();
    }

    // ── Status messages ───────────────────────────────────────────────────────

    public static void success(String msg) {
        System.out.println(B_GREEN + "  ✔  " + msg + RESET);
    }

    public static void error(String msg) {
        System.out.println(B_RED + "  ✘  " + msg + RESET);
    }

    public static void info(String msg) {
        System.out.println(B_CYAN + "  ℹ  " + msg + RESET);
    }

    public static void warn(String msg) {
        System.out.println(B_YELLOW + "  ⚠  " + msg + RESET);
    }

    // ── Table drawing ─────────────────────────────────────────────────────────

    /**
     * Prints a labelled field row: "  Label         : Value"
     */
    public static void field(String label, String value) {
        System.out.printf("  %s%-18s%s : %s%s%s%n",
            YELLOW, label, RESET, WHITE, value == null ? "—" : value, RESET);
    }

    /**
     * Formats a cell to fixed width (truncates with … if too long).
     */
    public static String cell(String value, int width) {
        if (value == null) value = "—";
        if (value.length() > width) value = value.substring(0, width - 1) + "…";
        return String.format("%-" + width + "s", value);
    }

    public static String cellRight(String value, int width) {
        if (value == null) value = "—";
        return String.format("%" + width + "s", value);
    }

    // ── GPA bar ───────────────────────────────────────────────────────────────

    public static String gpaBar(double gpa) {
        int filled = (int) Math.round(gpa);   // 0-10
        int empty  = 10 - filled;
        String color = gpa >= 8.5 ? B_GREEN : gpa >= 7.0 ? B_YELLOW : B_RED;
        return color + "█".repeat(filled) + RESET + CYAN + "░".repeat(empty) + RESET
             + String.format("  %s%.2f%s", color, gpa, RESET);
    }

    // ── Status badge ─────────────────────────────────────────────────────────

    public static String statusBadge(String status) {
        if (status == null) return "—";
        return switch (status) {
            case "Active"    -> B_GREEN  + "● Active"    + RESET;
            case "Inactive"  -> YELLOW   + "● Inactive"  + RESET;
            case "Graduated" -> B_BLUE   + "● Graduated" + RESET;
            case "Dropped"   -> B_RED    + "● Dropped"   + RESET;
            default          -> WHITE    + status        + RESET;
        };
    }

    // ── Prompt ────────────────────────────────────────────────────────────────

    public static void prompt(String msg) {
        System.out.print(B_CYAN + "  » " + RESET + msg + " ");
    }

    // ── Banner ────────────────────────────────────────────────────────────────

    public static void printBanner() {
        System.out.println(B_BLUE);
        System.out.println("  ╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("  ║                                                                  ║");
        System.out.println("  ║        🎓  STUDENT RECORD MANAGEMENT SYSTEM                     ║");
        System.out.println("  ║              Core Java  •  JDBC  •  MySQL                       ║");
        System.out.println("  ║                                                                  ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════════════╝");
        System.out.println(RESET);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void pauseMsg() {
        System.out.println();
        System.out.println(CYAN + "  Press ENTER to continue..." + RESET);
    }
}
