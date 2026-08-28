import java.util.Scanner;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

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
        Task[] tasks = new Task[100];
        Storage storage = new Storage();
        int taskCount = loadTasks(storage, tasks);

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
                        System.out.println((i + 1) + ".[" + tasks[i].getTypeIcon() + "]["
                                + tasks[i].getStatusIcon() + "] " + tasks[i].getDescription()
                                + tasks[i].getDateTimeDetails());
                    }
                } else if (command.toLowerCase().startsWith("delete ")) {
                    String taskNumberText = command.substring(7).trim();
                    try {
                        int taskNumber = Integer.parseInt(taskNumberText);
                        int taskIndex = taskNumber - 1;

                        if (taskIndex >= 0 && taskIndex < taskCount) {
                            Task deletedTask = tasks[taskIndex];
                            for (int i = taskIndex; i < taskCount - 1; i++) {
                                tasks[i] = tasks[i + 1];
                            }
                            tasks[taskCount - 1] = null;
                            taskCount--;

                            saveTasks(storage, tasks, taskCount);

                            System.out.println("Noted. I've removed this task:");
                            System.out.println("  [" + deletedTask.getTypeIcon() + "]["
                                    + deletedTask.getStatusIcon() + "] "
                                    + deletedTask.getDescription()
                                    + deletedTask.getDateTimeDetails());
                            System.out.println("Now you have " + taskCount + " tasks in the list.");
                        } else {
                            System.out.println("That task number does not exist.");
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("Please provide a valid task number.");
                    }
                } else if (command.toLowerCase().startsWith("mark ")) {
                    String taskNumberText = command.substring(5).trim();
                    try {
                        int taskNumber = Integer.parseInt(taskNumberText);
                        int taskIndex = taskNumber - 1;

                        if (taskIndex >= 0 && taskIndex < taskCount) {
                            tasks[taskIndex].markAsDone();
                            saveTasks(storage, tasks, taskCount);
                            System.out.println("Nice! I've marked this task as done:");
                            System.out.println("  [X] " + tasks[taskIndex].getDescription());
                        } else {
                            System.out.println("That task number does not exist.");
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("Please provide a valid task number.");
                    }
                } else if (command.toLowerCase().startsWith("unmark ")) {
                    String taskNumberText = command.substring(7).trim();
                    try {
                        int taskNumber = Integer.parseInt(taskNumberText);
                        int taskIndex = taskNumber - 1;

                        if (taskIndex >= 0 && taskIndex < taskCount) {
                            tasks[taskIndex].markAsNotDone();
                            saveTasks(storage, tasks, taskCount);
                            System.out.println("OK, I've marked this task as not done yet:");
                            System.out.println("  [ ] " + tasks[taskIndex].getDescription());
                        } else {
                            System.out.println("That task number does not exist.");
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("Please provide a valid task number.");
                    }
                } else if (taskCount < tasks.length) {
                    Task newTask = createTask(command);
                    if (newTask == null) {
                        if (command.equalsIgnoreCase("todo")) {
                            System.out.println("Please provide a proper action to do.");
                        } else {
                            System.out.println("Invalid input. Please try again.");
                        }
                    } else {
                        tasks[taskCount] = newTask;
                        taskCount++;
                        saveTasks(storage, tasks, taskCount);
                        System.out.println("Got it. I've added this task:");
                        System.out.println("  [" + newTask.getTypeIcon() + "][ ] "
                                + newTask.getDescription() + newTask.getDateTimeDetails());
                        System.out.println("Now you have " + taskCount + " tasks in the list.");
                    }
                } else {
                    System.out.println("I cannot store any more tasks.");
                }

                System.out.println(separator);
            }
        }
    }

    /** Loads saved tasks into the fixed-size task array used by the application. */
    private static int loadTasks(Storage storage, Task[] tasks) {
        try {
            List<Task> savedTasks = storage.loadTasks();
            int taskCount = Math.min(savedTasks.size(), tasks.length);
            for (int i = 0; i < taskCount; i++) {
                tasks[i] = savedTasks.get(i);
            }
            return taskCount;
        } catch (IOException exception) {
            System.out.println("I could not load your saved tasks. Starting with an empty list.");
            return 0;
        }
    }

    /** Saves the current task list and reports an error if disk access fails. */
    private static void saveTasks(Storage storage, Task[] tasks, int taskCount) {
        try {
            storage.saveTasks(tasks, taskCount);
        } catch (IOException exception) {
            System.out.println("I could not save your tasks.");
        }
    }

    /** Creates the correct task subtype from a user command. */
    private static Task createTask(String command) {
        String lowerCaseCommand = command.toLowerCase();

        if (lowerCaseCommand.startsWith("todo ")) {
            String description = command.substring(5).trim();
            return description.isEmpty() ? null : new Task(description);
        }

        if (lowerCaseCommand.startsWith("deadline ")) {
            String taskDetails = command.substring(9).trim();
            int byIndex = taskDetails.toLowerCase().indexOf(" /by ");
            if (byIndex > 0 && !taskDetails.substring(byIndex + 5).trim().isEmpty()) {
                try {
                    return new Deadline(taskDetails.substring(0, byIndex).trim(),
                            LocalDate.parse(taskDetails.substring(byIndex + 5).trim()));
                } catch (DateTimeParseException exception) {
                    return null;
                }
            }
            return null;
        }

        if (lowerCaseCommand.startsWith("event ")) {
            String taskDetails = command.substring(6).trim();
            int fromIndex = taskDetails.toLowerCase().indexOf(" /from ");
            int toIndex = taskDetails.toLowerCase().indexOf(" /to ");
            if (fromIndex > 0 && toIndex > fromIndex
                    && !taskDetails.substring(toIndex + 5).trim().isEmpty()) {
                try {
                    return new Event(taskDetails.substring(0, fromIndex).trim(),
                            LocalDate.parse(taskDetails.substring(fromIndex + 7, toIndex).trim()),
                            LocalDate.parse(taskDetails.substring(toIndex + 5).trim()));
                } catch (DateTimeParseException exception) {
                    return null;
                }
            }
            return null;
        }

        return null;
    }
}
