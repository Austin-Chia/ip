import java.util.Scanner;

public class Trayce {
    public static void main(String[] args) {
        String banner = " _____\n"
                + "|_   _| __ __ _ _   _  ___ ___\n"
                + "  | |  '__/ _` | | | |/ __/ _ \\\n"
                + "  | | | | | (_| | |_| | (_|  __/\n"
                + "  |_| |_|  \\__,_|\\__, |\\___\\___|\n"
                + "                  __/ |\n"
                + "                 |___/\n";
        System.out.println(banner);
        System.out.println("Hello! I'm Trayce. What can I do for you?");

        String separator = "_".repeat(80);
        String[] tasks = new String[100];
        boolean[] completed = new boolean[100];
        int taskCount = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println(separator);
                String command = scanner.nextLine();
                System.out.println(separator);

                if (command.equalsIgnoreCase("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    break;
                }

                if (command.equalsIgnoreCase("list")) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        String status = completed[i] ? "X" : " ";
                        System.out.println((i + 1) + ".[" + status + "] " + tasks[i]);
                    }
                } else if (command.toLowerCase().startsWith("mark ")) {
                    String taskNumberText = command.substring(5).trim();
                    try {
                        int taskNumber = Integer.parseInt(taskNumberText);
                        int taskIndex = taskNumber - 1;

                        if (taskIndex >= 0 && taskIndex < taskCount) {
                            completed[taskIndex] = true;
                            System.out.println("Nice! I've marked this task as done:");
                            System.out.println("  [X] " + tasks[taskIndex]);
                        } else {
                            System.out.println("That task number does not exist.");
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("Please provide a valid task number.");
                    }
                } else if (taskCount < tasks.length) {
                    tasks[taskCount] = command;
                    taskCount++;
                    System.out.println("added: " + command);
                } else {
                    System.out.println("I cannot store any more tasks.");
                }

                System.out.println(separator);
            }
        }
    }
}
