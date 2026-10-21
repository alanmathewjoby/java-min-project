import java.util.*;
import java.io.*;

abstract class Person {
    String name;

    Person(String name) {
        this.name = name;
    }

    abstract void display();
}

class Teacher extends Person {
    String id, subject, availability, phd;

    Teacher(String name, String id, String subject,
            String availability, String phd) {
        super(name);
        this.id = id;
        this.subject = subject;
        this.availability = availability;
        this.phd = phd;
    }

    @Override
    void display() {
        System.out.println(id + " | " + name + " | "
                + subject + " | " + availability + " | " + phd);
    }

    void save() {
        try (FileWriter fw = new FileWriter("teachers.txt", true)) {
            fw.write(id + "|" + name + "|" + subject + "|"
                    + availability + "|" + phd + "\n");
        } catch (IOException e) {
            System.out.println("File error.");
        }
    }
}

class Subject {
    String code, name, semester;
    int hours;

    Subject(String code, String name, int hours, String semester) {
        this.code = code;
        this.name = name;
        this.hours = hours;
        this.semester = semester;
    }

    void display() {
        System.out.println(code + " | " + name + " | "
                + hours + " | " + semester);
    }

    void save() {
        try (FileWriter fw = new FileWriter("subjects.txt", true)) {
            fw.write(code + "|" + name + "|" + hours + "|"
                    + semester + "\n");
        } catch (IOException e) {
            System.out.println("File error.");
        }
    }
}

class PeriodOccupiedException extends Exception {
    PeriodOccupiedException() {
        super("That period is already occupied.");
    }
}

class TimetableEntry {
    Subject subject;
    Teacher teacher;

    TimetableEntry(Subject subject, Teacher teacher) {
        this.subject = subject;
        this.teacher = teacher;
    }

    void display() {
        System.out.println(subject.code + " - " + teacher.name);
    }
}

public class MeridianMini {
    static Scanner sc = new Scanner(System.in);

    static Teacher[] teachers = new Teacher[20];
    static Subject[] subjects = new Subject[20];

    static int teacherCount = 0;
    static int subjectCount = 0;

    static TimetableEntry[][] timetable = new TimetableEntry[3][3];

    static String[] days = {"Monday", "Tuesday", "Wednesday"};

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nMERIDIAN MINI");
            System.out.println("1. Add Teacher");
            System.out.println("2. View Teachers");
            System.out.println("3. Add Subject");
            System.out.println("4. View Subjects");
            System.out.println("5. Add Timetable Entry");
            System.out.println("6. View Timetable");
            System.out.println("7. Exit");

            System.out.print("Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: addTeacher(); break;
                case 2: viewTeachers(); break;
                case 3: addSubject(); break;
                case 4: viewSubjects(); break;
                case 5: addEntry(); break;
                case 6: viewTimetable(); break;
                case 7: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    static void addTeacher() {
        if (teacherCount == teachers.length) {
            System.out.println("Teacher limit reached.");
            return;
        }

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("College ID: ");
        String id = sc.nextLine();

        System.out.print("Subject code: ");
        String subject = sc.nextLine();

        System.out.print("Availability (Yes/No): ");
        String availability = sc.nextLine();

        System.out.print("PhD (Yes/No): ");
        String phd = sc.nextLine();

        teachers[teacherCount] =
                new Teacher(name, id, subject, availability, phd);

        teachers[teacherCount].save();
        teacherCount++;
        System.out.println("Teacher added.");
    }

    static void viewTeachers() {
        for (int i = 0; i < teacherCount; i++) {
            teachers[i].display();
        }
    }

    static void addSubject() {
        if (subjectCount == subjects.length) {
            System.out.println("Subject limit reached.");
            return;
        }

        System.out.print("Subject code: ");
        String code = sc.nextLine();

        System.out.print("Subject name: ");
        String name = sc.nextLine();

        System.out.print("Weekly hours: ");
        int hours = sc.nextInt();
        sc.nextLine();

        System.out.print("Semester: ");
        String semester = sc.nextLine();

        subjects[subjectCount] =
                new Subject(code, name, hours, semester);

        subjects[subjectCount].save();
        subjectCount++;
        System.out.println("Subject added.");
    }

    static void viewSubjects() {
        for (int i = 0; i < subjectCount; i++) {
            subjects[i].display();
        }
    }

    static void addEntry() {
        System.out.println("0. Monday");
        System.out.println("1. Tuesday");
        System.out.println("2. Wednesday");
        System.out.print("Day index: ");
        int day = sc.nextInt();

        System.out.print("Period index (0-2): ");
        int period = sc.nextInt();
        sc.nextLine();

        if (day < 0 || day > 2 || period < 0 || period > 2) {
            System.out.println("Invalid day or period.");
            return;
        }

        try {
            if (timetable[day][period] != null) {
                throw new PeriodOccupiedException();
            }

            System.out.println("Available subjects:");
            viewSubjects();
            System.out.print("Subject code: ");
            String code = sc.nextLine();

            Subject chosenSubject = null;
            for (int i = 0; i < subjectCount; i++) {
                if (subjects[i].code.equalsIgnoreCase(code)) {
                    chosenSubject = subjects[i];
                    break;
                }
            }

            System.out.println("Available teachers:");
            viewTeachers();
            System.out.print("Teacher ID: ");
            String id = sc.nextLine();

            Teacher chosenTeacher = null;
            for (int i = 0; i < teacherCount; i++) {
                if (teachers[i].id.equalsIgnoreCase(id)) {
                    chosenTeacher = teachers[i];
                    break;
                }
            }

            if (chosenSubject == null || chosenTeacher == null) {
                System.out.println("Subject or teacher not found.");
                return;
            }

            timetable[day][period] =
                    new TimetableEntry(chosenSubject, chosenTeacher);

            saveTimetable();
            System.out.println("Entry added.");

        } catch (PeriodOccupiedException e) {
            System.out.println(e.getMessage());
        }
    }

    static void viewTimetable() {
        for (int d = 0; d < 3; d++) {
            System.out.println("\n" + days[d]);

            for (int p = 0; p < 3; p++) {
                System.out.print("Period " + (p + 1) + ": ");

                if (timetable[d][p] == null) {
                    System.out.println("Free");
                } else {
                    timetable[d][p].display();
                }
            }
        }
    }

    static void saveTimetable() {
        try (FileWriter fw = new FileWriter("timetable.txt")) {
            for (int d = 0; d < 3; d++) {
                for (int p = 0; p < 3; p++) {
                    if (timetable[d][p] == null) {
                        fw.write("-");
                    } else {
                        fw.write(timetable[d][p].subject.code + ","
                                + timetable[d][p].teacher.id);
                    }

                    if (p < 2) {
                        fw.write("|");
                    }
                }
                fw.write("\n");
            }
        } catch (IOException e) {
            System.out.println("File error.");
        }
    }
}
