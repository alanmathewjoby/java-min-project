import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

abstract class Person {
    String name;

    Person(String name) {
        this.name = name;
    }

    abstract String getRole();
}

class Teacher extends Person {
    String collegeId;
    String subject;
    String availability;
    String phdStatus;

    Teacher(String name, String collegeId, String subject,
            String availability, String phdStatus) {
        super(name);
        this.collegeId = collegeId;
        this.subject = subject;
        this.availability = availability;
        this.phdStatus = phdStatus;
    }

    @Override
    String getRole() {
        return "Teacher";
    }

    public String toString() {
        return collegeId + " | " + name + " | " + subject
                + " | " + availability + " | " + phdStatus;
    }
}

class Subject {
    String code;
    String name;
    int weeklyHours;
    String semester;

    Subject(String code, String name, int weeklyHours, String semester) {
        this.code = code;
        this.name = name;
        this.weeklyHours = weeklyHours;
        this.semester = semester;
    }

    public String toString() {
        return code + " | " + name + " | " + weeklyHours
                + " | Semester " + semester;
    }
}

class TimetableEntry {
    int day;
    int period;
    Subject subject;
    Teacher teacher;

    TimetableEntry(int day, int period, Subject subject, Teacher teacher) {
        this.day = day;
        this.period = period;
        this.subject = subject;
        this.teacher = teacher;
    }
}

public class MeridianMini {
    static final int MAX_TEACHERS = 100;
    static final int MAX_SUBJECTS = 100;

    static final String[] DAYS = {
        "Monday", "Tuesday", "Wednesday"
    };

    static final String[] PERIODS = {
        "Period 1", "Period 2", "Period 3"
    };

    Teacher[] teachers = new Teacher[MAX_TEACHERS];
    Subject[] subjects = new Subject[MAX_SUBJECTS];
    TimetableEntry[][] timetable = new TimetableEntry[3][3];

    int teacherCount = 0;
    int subjectCount = 0;

    Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        MeridianMini app = new MeridianMini();
        app.loadAll();
        app.run();
    }

    String input(String message) {
        System.out.print(message);
        return scan.nextLine().trim();
    }

    int readInt(String message) {
        while (true) {
            try {
                return Integer.parseInt(input(message));
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    boolean yes(String value) {
        return value.equalsIgnoreCase("yes")
                || value.equalsIgnoreCase("y");
    }

    void run() {
        while (true) {
            System.out.println("\n========== MERIDIAN MINI ==========");
            System.out.println("1. Manage Teachers");
            System.out.println("2. Manage Subjects");
            System.out.println("3. Add Timetable Entry");
            System.out.println("4. View Full Timetable");
            System.out.println("5. View Timetable by Day");
            System.out.println("6. Search Timetable");
            System.out.println("7. Update Timetable Entry");
            System.out.println("8. Delete Timetable Entry");
            System.out.println("9. Exit");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    teacherMenu();
                    break;
                case 2:
                    subjectMenu();
                    break;
                case 3:
                    addEntry();
                    break;
                case 4:
                    viewTimetable();
                    break;
                case 5:
                    viewDay();
                    break;
                case 6:
                    searchTimetable();
                    break;
                case 7:
                    updateEntry();
                    break;
                case 8:
                    deleteEntry();
                    break;
                case 9:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    Teacher findTeacher(String id) {
        for (int i = 0; i < teacherCount; i++) {
            if (teachers[i].collegeId.equalsIgnoreCase(id)) {
                return teachers[i];
            }
        }
        return null;
    }

    Subject findSubject(String code) {
        for (int i = 0; i < subjectCount; i++) {
            if (subjects[i].code.equalsIgnoreCase(code)) {
                return subjects[i];
            }
        }
        return null;
    }

    void teacherMenu() {
        while (true) {
            System.out.println("\n--- TEACHER MANAGEMENT ---");
            System.out.println("1. Add Teacher");
            System.out.println("2. View All Teachers");
            System.out.println("3. Search Teacher");
            System.out.println("4. Update Teacher");
            System.out.println("5. Delete Teacher");
            System.out.println("0. Back");

            int choice = readInt("Choice: ");

            switch (choice) {
                case 1:
                    addTeacher();
                    break;
                case 2:
                    viewTeachers();
                    break;
                case 3:
                    searchTeacher();
                    break;
                case 4:
                    updateTeacher();
                    break;
                case 5:
                    deleteTeacher();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    void addTeacher() {
        if (teacherCount == MAX_TEACHERS) {
            System.out.println("Teacher capacity reached.");
            return;
        }

        String id = input("College ID: ");

        if (findTeacher(id) != null) {
            System.out.println("That college ID already exists.");
            return;
        }

        String name = input("Teacher name: ");
        String subject = input("Subject code taught: ");
        String availability = input("Available? (Yes/No): ");
        String phd = input("PhD status (Yes/No): ");

        Teacher t = new Teacher(name, id, subject,
                availability, phd);

        teachers[teacherCount++] = t;
        saveTeachers();
        log("ADD_TEACHER", id + " " + name);
        System.out.println("Teacher added.");
    }

    void viewTeachers() {
        if (teacherCount == 0) {
            System.out.println("No teachers found.");
            return;
        }

        for (int i = 0; i < teacherCount; i++) {
            Teacher t = teachers[i];
            System.out.println("\n" + t);

            boolean assigned = false;
            for (int d = 0; d < 3; d++) {
                for (int p = 0; p < 3; p++) {
                    TimetableEntry e = timetable[d][p];
                    if (e != null && e.teacher == t) {
                        System.out.println("  Assigned: " + DAYS[d]
                                + ", " + PERIODS[p]
                                + " (" + e.subject.code + ")");
                        assigned = true;
                    }
                }
            }

            if (!assigned) {
                System.out.println("  No timetable assignments.");
            }
        }
    }

    void searchTeacher() {
        String id = input("Enter college ID: ");
        Teacher t = findTeacher(id);

        if (t == null) {
            System.out.println("Teacher not found.");
            return;
        }

        System.out.println(t);
        for (int d = 0; d < 3; d++) {
            for (int p = 0; p < 3; p++) {
                TimetableEntry e = timetable[d][p];
                if (e != null && e.teacher == t) {
                    System.out.println(DAYS[d] + ", " + PERIODS[p]
                            + ": " + e.subject.name);
                }
            }
        }
    }

    void updateTeacher() {
        String id = input("Enter college ID: ");
        Teacher t = findTeacher(id);

        if (t == null) {
            System.out.println("Teacher not found.");
            return;
        }

        String availability = input("New availability (Yes/No): ");
        String phd = input("New PhD status (Yes/No): ");

        if (availability.equalsIgnoreCase("yes")
                || availability.equalsIgnoreCase("no")) {
            t.availability = availability;
        } else {
            System.out.println("Invalid availability; it was unchanged.");
        }

        if (phd.equalsIgnoreCase("yes")
                || phd.equalsIgnoreCase("no")) {
            t.phdStatus = phd;
        } else {
            System.out.println("Invalid PhD status; it was unchanged.");
        }

        saveTeachers();
        log("UPDATE_TEACHER", id);
        System.out.println("Teacher details updated.");
    }

    void deleteTeacher() {
        String id = input("Enter college ID: ");
        int index = -1;

        for (int i = 0; i < teacherCount; i++) {
            if (teachers[i].collegeId.equalsIgnoreCase(id)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("Teacher not found.");
            return;
        }

        for (int d = 0; d < 3; d++) {
            for (int p = 0; p < 3; p++) {
                if (timetable[d][p] != null
                        && timetable[d][p].teacher == teachers[index]) {
                    System.out.println(
                            "Cannot delete: teacher is assigned to the timetable.");
                    return;
                }
            }
        }

        for (int i = index; i < teacherCount - 1; i++) {
            teachers[i] = teachers[i + 1];
        }

        teachers[--teacherCount] = null;
        saveTeachers();
        log("DELETE_TEACHER", id);
        System.out.println("Teacher deleted.");
    }

    void subjectMenu() {
        while (true) {
            System.out.println("\n--- SUBJECT MANAGEMENT ---");
            System.out.println("1. Add Subject");
            System.out.println("2. View All Subjects");
            System.out.println("3. Search Subject");
            System.out.println("4. Update Subject");
            System.out.println("5. Delete Subject");
            System.out.println("0. Back");

            int choice = readInt("Choice: ");

            switch (choice) {
                case 1:
                    addSubject();
                    break;
                case 2:
                    viewSubjects();
                    break;
                case 3:
                    searchSubject();
                    break;
                case 4:
                    updateSubject();
                    break;
                case 5:
                    deleteSubject();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    void addSubject() {
        if (subjectCount == MAX_SUBJECTS) {
            System.out.println("Subject capacity reached.");
            return;
        }

        String code = input("Subject code: ");

        if (findSubject(code) != null) {
            System.out.println("That subject code already exists.");
            return;
        }

        String name = input("Subject name: ");
        int hours = readInt("Weekly hours: ");
        String semester = input("Semester: ");

        if (hours < 1) {
            System.out.println("Weekly hours must be positive.");
            return;
        }

        subjects[subjectCount++] =
                new Subject(code, name, hours, semester);

        saveSubjects();
        log("ADD_SUBJECT", code + " " + name);
        System.out.println("Subject added.");
    }

    void viewSubjects() {
        if (subjectCount == 0) {
            System.out.println("No subjects found.");
            return;
        }

        for (int i = 0; i < subjectCount; i++) {
            System.out.println(subjects[i]);
        }
    }

    void searchSubject() {
        String code = input("Enter subject code: ");
        Subject s = findSubject(code);

        if (s == null) {
            System.out.println("Subject not found.");
        } else {
            System.out.println(s);
        }
    }

    void updateSubject() {
        String code = input("Enter subject code: ");
        Subject s = findSubject(code);

        if (s == null) {
            System.out.println("Subject not found.");
            return;
        }

        String name = input("New subject name: ");
        int hours = readInt("New weekly hours: ");
        String semester = input("New semester: ");

        if (hours < 1) {
            System.out.println("Weekly hours must be positive.");
            return;
        }

        int scheduled = countSubject(code, -1, -1);
        if (hours < scheduled) {
            System.out.println("Weekly hours cannot be less than "
                    + scheduled + " already scheduled periods.");
            return;
        }

        s.name = name;
        s.weeklyHours = hours;
        s.semester = semester;

        saveSubjects();
        log("UPDATE_SUBJECT", code);
        System.out.println("Subject updated.");
    }

    void deleteSubject() {
        String code = input("Enter subject code: ");
        int index = -1;

        for (int i = 0; i < subjectCount; i++) {
            if (subjects[i].code.equalsIgnoreCase(code)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("Subject not found.");
            return;
        }

        for (int i = 0; i < teacherCount; i++) {
            if (teachers[i].subject.equalsIgnoreCase(code)) {
                System.out.println(
                        "Cannot delete: a teacher is associated with this subject.");
                return;
            }
        }

        for (int d = 0; d < 3; d++) {
            for (int p = 0; p < 3; p++) {
                if (timetable[d][p] != null
                        && timetable[d][p].subject == subjects[index]) {
                    System.out.println(
                            "Cannot delete: subject is in the timetable.");
                    return;
                }
            }
        }

        for (int i = index; i < subjectCount - 1; i++) {
            subjects[i] = subjects[i + 1];
        }

        subjects[--subjectCount] = null;
        saveSubjects();
        log("DELETE_SUBJECT", code);
        System.out.println("Subject deleted.");
    }

    int chooseDay() {
        System.out.println("1. Monday");
        System.out.println("2. Tuesday");
        System.out.println("3. Wednesday");

        int day = readInt("Day: ");

        if (day < 1 || day > 3) {
            System.out.println("Invalid day.");
            return -1;
        }

        return day - 1;
    }

    int choosePeriod() {
        int period = readInt("Period (1-3): ");

        if (period < 1 || period > 3) {
            System.out.println("Invalid period.");
            return -1;
        }

        return period - 1;
    }

    int countSubject(String code, int excludedDay, int excludedPeriod) {
        int count = 0;

        for (int d = 0; d < 3; d++) {
            for (int p = 0; p < 3; p++) {
                if (d == excludedDay && p == excludedPeriod) {
                    continue;
                }

                TimetableEntry e = timetable[d][p];
                if (e != null && e.subject.code.equalsIgnoreCase(code)) {
                    count++;
                }
            }
        }

        return count;
    }

    void addEntry() {
        int day = chooseDay();
        int period = choosePeriod();

        if (day == -1 || period == -1) {
            return;
        }

        if (timetable[day][period] != null) {
            System.out.println("That period is already occupied.");
            return;
        }

        assignEntry(day, period);
    }

    void assignEntry(int day, int period) {
        String code = input("Subject code: ");
        Subject s = findSubject(code);

        if (s == null) {
            System.out.println("Subject not found. Add it first.");
            return;
        }

        String id = input("Teacher college ID: ");
        Teacher t = findTeacher(id);

        if (t == null) {
            System.out.println("Teacher not found. Add them first.");
            return;
        }

        if (!t.subject.equalsIgnoreCase(s.code)) {
            System.out.println("This teacher is not assigned to that subject.");
            return;
        }

        if (!t.availability.equalsIgnoreCase("yes")
                && !t.availability.equalsIgnoreCase("available")) {
            System.out.println("This teacher is unavailable.");
            return;
        }

        if (countSubject(s.code, day, period) >= s.weeklyHours) {
            System.out.println("This subject has reached its weekly hours.");
            return;
        }

        timetable[day][period] =
                new TimetableEntry(day, period, s, t);

        saveTimetable();
        log("ADD_ENTRY", DAYS[day] + " P" + (period + 1)
                + " " + s.code + " " + t.collegeId);

        System.out.println("Timetable entry saved.");
    }

    void viewTimetable() {
        System.out.println("\n================ TIMETABLE ================");
        System.out.printf("%-12s | %-25s | %-25s | %-25s%n",
                "Day", "Period 1", "Period 2", "Period 3");

        for (int d = 0; d < 3; d++) {
            System.out.printf("%-12s", DAYS[d]);

            for (int p = 0; p < 3; p++) {
                TimetableEntry e = timetable[d][p];
                String value = "-";

                if (e != null) {
                    value = e.subject.code + " / " + e.teacher.name;
                }

                System.out.printf(" | %-25s", value);
            }
            System.out.println();
        }
    }

    void viewDay() {
        int day = chooseDay();

        if (day == -1) {
            return;
        }

        System.out.println("\n" + DAYS[day]);

        for (int p = 0; p < 3; p++) {
            TimetableEntry e = timetable[day][p];

            if (e == null) {
                System.out.println(PERIODS[p] + ": Free");
            } else {
                System.out.println(PERIODS[p] + ": "
                        + e.subject.name + " (" + e.subject.code + ")"
                        + " - " + e.teacher.name);
            }
        }
    }

    void searchTimetable() {
        System.out.println("1. Search by subject code");
        System.out.println("2. Search by teacher ID");
        System.out.println("3. Search by day");

        int choice = readInt("Choice: ");
        String key = "";

        if (choice == 1) {
            key = input("Subject code: ");
        } else if (choice == 2) {
            key = input("Teacher ID: ");
        } else if (choice == 3) {
            int day = chooseDay();
            if (day == -1) {
                return;
            }
            key = DAYS[day];
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        boolean found = false;

        for (int d = 0; d < 3; d++) {
            for (int p = 0; p < 3; p++) {
                TimetableEntry e = timetable[d][p];
                if (e == null) {
                    continue;
                }

                boolean match = false;

                if (choice == 1) {
                    match = e.subject.code.equalsIgnoreCase(key);
                } else if (choice == 2) {
                    match = e.teacher.collegeId.equalsIgnoreCase(key);
                } else if (choice == 3) {
                    match = DAYS[d].equalsIgnoreCase(key);
                }

                if (match) {
                    System.out.println(DAYS[d] + ", " + PERIODS[p]
                            + ": " + e.subject.name
                            + " - " + e.teacher.name);
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No matching entries found.");
        }
    }

    void updateEntry() {
        int day = chooseDay();
        int period = choosePeriod();

        if (day == -1 || period == -1) {
            return;
        }

        if (timetable[day][period] == null) {
            System.out.println("That period is empty. Use Add instead.");
            return;
        }

        TimetableEntry old = timetable[day][period];
        timetable[day][period] = null;

        System.out.println("Current entry: " + old.subject.code
                + " - " + old.teacher.name);
        System.out.println("Enter the replacement details.");

        assignEntry(day, period);

        if (timetable[day][period] == null) {
            timetable[day][period] = old;
            saveTimetable();
            System.out.println("Original entry restored.");
        } else {
            log("UPDATE_ENTRY", DAYS[day] + " P" + (period + 1));
        }
    }

    void deleteEntry() {
        int day = chooseDay();
        int period = choosePeriod();

        if (day == -1 || period == -1) {
            return;
        }

        TimetableEntry e = timetable[day][period];

        if (e == null) {
            System.out.println("That period is already empty.");
            return;
        }

        timetable[day][period] = null;
        saveTimetable();

        log("DELETE_ENTRY", DAYS[day] + " P" + (period + 1)
                + " " + e.subject.code + " " + e.teacher.collegeId);

        System.out.println("Timetable entry deleted.");
    }

    void saveTeachers() {
        try (BufferedWriter out =
                     new BufferedWriter(new FileWriter("teachers.txt"))) {
            for (int i = 0; i < teacherCount; i++) {
                Teacher t = teachers[i];
                out.write(t.collegeId + "|" + t.name + "|" + t.subject
                        + "|" + t.availability + "|" + t.phdStatus);
                out.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save teachers: " + e.getMessage());
        }
    }

    void saveSubjects() {
        try (BufferedWriter out =
                     new BufferedWriter(new FileWriter("subjects.txt"))) {
            for (int i = 0; i < subjectCount; i++) {
                Subject s = subjects[i];
                out.write(s.code + "|" + s.name + "|" + s.weeklyHours
                        + "|" + s.semester);
                out.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save subjects: " + e.getMessage());
        }
    }

    void saveTimetable() {
        try (BufferedWriter out =
                     new BufferedWriter(new FileWriter("timetable.txt"))) {
            for (int d = 0; d < 3; d++) {
                for (int p = 0; p < 3; p++) {
                    TimetableEntry e = timetable[d][p];

                    if (e == null) {
                        out.write("-");
                    } else {
                        out.write(e.subject.code + "," + e.teacher.collegeId);
                    }

                    if (p < 2) {
                        out.write("|");
                    }
                }
                out.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save timetable: " + e.getMessage());
        }
    }

    void log(String action, String details) {
        try (BufferedWriter out =
                     new BufferedWriter(new FileWriter("log.txt", true))) {
            String time = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            out.write("[" + time + "] " + action + " | " + details);
            out.newLine();
        } catch (IOException e) {
            System.out.println("Could not write to log: " + e.getMessage());
        }
    }

    void loadAll() {
        loadTeachers();
        loadSubjects();
        loadTimetable();
    }

    void loadTeachers() {
        File file = new File("teachers.txt");

        if (!file.exists()) {
            return;
        }

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = in.readLine()) != null
                    && teacherCount < MAX_TEACHERS) {
                String[] parts = line.split("\\|", -1);

                if (parts.length != 5 || findTeacher(parts[0]) != null) {
                    continue;
                }

                teachers[teacherCount++] = new Teacher(
                        parts[1], parts[0], parts[2], parts[3], parts[4]);
            }
        } catch (IOException e) {
            System.out.println("Could not load teachers: " + e.getMessage());
        }
    }

    void loadSubjects() {
        File file = new File("subjects.txt");

        if (!file.exists()) {
            return;
        }

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = in.readLine()) != null
                    && subjectCount < MAX_SUBJECTS) {
                String[] parts = line.split("\\|", -1);

                if (parts.length != 4 || findSubject(parts[0]) != null) {
                    continue;
                }

                try {
                    int hours = Integer.parseInt(parts[2]);

                    if (hours > 0) {
                        subjects[subjectCount++] = new Subject(
                                parts[0], parts[1], hours, parts[3]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid subject record.");
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load subjects: " + e.getMessage());
        }
    }

    void loadTimetable() {
        File file = new File("timetable.txt");

        if (!file.exists()) {
            return;
        }

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            for (int d = 0; d < 3; d++) {
                String line = in.readLine();

                if (line == null) {
                    break;
                }

                String[] slots = line.split("\\|", -1);

                if (slots.length != 3) {
                    continue;
                }

                for (int p = 0; p < 3; p++) {
                    if (slots[p].equals("-")) {
                        continue;
                    }

                    String[] parts = slots[p].split(",", -1);

                    if (parts.length != 2) {
                        continue;
                    }

                    Subject s = findSubject(parts[0]);
                    Teacher t = findTeacher(parts[1]);

                    if (s != null && t != null
                            && t.subject.equalsIgnoreCase(s.code)
                            && (t.availability.equalsIgnoreCase("yes")
                                || t.availability.equalsIgnoreCase("available"))
                            && countSubject(s.code, d, p) < s.weeklyHours) {
                        timetable[d][p] =
                                new TimetableEntry(d, p, s, t);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load timetable: " + e.getMessage());
        }
    }
}
