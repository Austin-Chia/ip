package trayce.ui;

import java.util.List;
import java.util.Scanner;

import trayce.task.Task;

/** Handles all console input and output for the Trayce application. */
public class Ui {
    private static final String SEPARATOR = "_".repeat(80);
    private final Scanner scanner;

    /** Creates a UI that reads commands from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Shows Trayce's welcome banner and greeting. */
    public void showWelcome() {
        String banner = " _____\n"
                + "|_   _| __ __ _ _   _  ___ ___\n"
                + "  | |  '__/ _` | | | |/ __/ _ \\\n"
                + "  | | | | | (_| | |_| | (_|  __/\n"
                + "  |_| |_|  \\__,_|\\__, |\\___\\___|\n"
                + "                  __/ |\n"
                + "                 |___/\n";
        System.out.println(banner);
        System.out.println("Hello! I'm Trayce. What can I do for you?");
    }

    /** Reads one line entered by the user. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Shows a horizontal divider. */
    public void showLine() {
        System.out.println(SEPARATOR);
    }

    /** Shows the goodbye message. */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /** Shows all tasks in their display order. */
    public void showTaskList(List<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println((i + 1) + ".[" + task.getTypeIcon() + "][" + task.getStatusIcon() + "] "
                    + task.getDescription() + task.getDateTimeDetails());
        }
    }

    /** Shows confirmation that a task was added. */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  [" + task.getTypeIcon() + "][ ] " + task.getDescription()
                + task.getDateTimeDetails());
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /** Shows confirmation that a task was deleted. */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  [" + task.getTypeIcon() + "][" + task.getStatusIcon() + "] "
                + task.getDescription() + task.getDateTimeDetails());
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /** Shows confirmation that a task's completion status changed. */
    public void showTaskMarked(Task task, boolean isDone) {
        String message = isDone ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:";
        System.out.println(message);
        System.out.println("  [" + task.getStatusIcon() + "] " + task.getDescription());
    }

    /** Shows the error used for an invalid task number. */
    public void showInvalidTaskNumber() {
        System.out.println("Please provide a valid task number.");
    }

    /** Shows the error used when a task number is outside the list. */
    public void showMissingTaskNumber() {
        System.out.println("That task number does not exist.");
    }

    /** Shows the error used for an invalid task-creation command. */
    public void showInvalidTaskCommand(String command) {
        if (command.equalsIgnoreCase("todo")) {
            System.out.println("Please provide a proper action to do.");
        } else {
            System.out.println("Invalid input. Please try again.");
        }
    }

    /** Shows the error used when the task list reaches its capacity. */
    public void showTaskLimitReached() {
        System.out.println("I cannot store any more tasks.");
    }

    /** Shows the error used when saved tasks cannot be loaded. */
    public void showLoadError() {
        System.out.println("I could not load your saved tasks. Starting with an empty list.");
    }

    /** Shows the error used when tasks cannot be saved. */
    public void showSaveError() {
        System.out.println("I could not save your tasks.");
    }

    /** Shows tasks matching a keyword. */
    public void showMatchingTasks(List<Task> tasks) {
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println((i + 1) + ".[" + task.getTypeIcon() + "][" + task.getStatusIcon() + "] "
                    + task.getDescription() + task.getDateTimeDetails());
        }
    }
}
