package trayce;

import java.io.IOException;
import java.util.Locale;

import trayce.parser.Parser;
import trayce.storage.Storage;
import trayce.task.Task;
import trayce.task.TaskList;
import trayce.ui.Ui;

/** Coordinates user input, task management, and persistent storage for Trayce. */
public class Trayce {
    private static final String HELP_COMMAND = "help";
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND_PREFIX = "mark ";
    private static final String UNMARK_COMMAND_PREFIX = "unmark ";
    private static final String DELETE_COMMAND_PREFIX = "delete ";
    private static final String FIND_COMMAND_PREFIX = "find ";
    private static final String INVALID_TASK_NUMBER_MESSAGE = "Please provide a valid task number.";
    private static final String HELP_MESSAGE = "Here are the commands you can use:\n\n"
            + "todo <description>\n"
            + "  Add a task\n\n"
            + "deadline <description> /by <YYYY-MM-DD>\n"
            + "  Add a deadline\n\n"
            + "event <description> /from <YYYY-MM-DD> /to <YYYY-MM-DD>\n"
            + "  Add an event\n\n"
            + "list\n"
            + "  Show all tasks\n\n"
            + "mark <number> / unmark <number>\n"
            + "  Complete or reopen a task\n\n"
            + "delete <number>\n"
            + "  Delete a task\n\n"
            + "find <keyword>\n"
            + "  Search your tasks";

    private final Ui ui = new Ui();
    private final Storage storage = new Storage();
    private final Parser parser = new Parser();
    private TaskList taskList = new TaskList();

    /** Creates a Trayce application with its required collaborators. */
    public Trayce() { }

    /** Processes a command for the graphical interface and returns a response. */
    public String getResponse(String command) {
        String trimmedCommand = command.trim();
        String lowerCaseCommand = trimmedCommand.toLowerCase(Locale.ROOT);

        if (lowerCaseCommand.equals(HELP_COMMAND)) {
            return HELP_MESSAGE;
        }
        if (lowerCaseCommand.equals(LIST_COMMAND)) {
            return getTaskListResponse();
        }
        if (lowerCaseCommand.startsWith(MARK_COMMAND_PREFIX)) {
            return updateTaskStatus(trimmedCommand.substring(MARK_COMMAND_PREFIX.length()), true);
        }
        if (lowerCaseCommand.startsWith(UNMARK_COMMAND_PREFIX)) {
            return updateTaskStatus(trimmedCommand.substring(UNMARK_COMMAND_PREFIX.length()), false);
        }
        if (lowerCaseCommand.startsWith(DELETE_COMMAND_PREFIX)) {
            return deleteTask(trimmedCommand.substring(DELETE_COMMAND_PREFIX.length()));
        }
        if (lowerCaseCommand.startsWith(FIND_COMMAND_PREFIX)) {
            return findTasks(trimmedCommand.substring(FIND_COMMAND_PREFIX.length()));
        }

        return addTask(trimmedCommand);
    }

    private String getTaskListResponse() {
        if (taskList.size() == 0) {
            return "Your task list is empty.";
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            result.append(i + 1).append(". [").append(task.getStatusIcon()).append("] ")
                    .append(task.getDescription()).append("\n");
        }
        return result.toString().trim();
    }

    private String deleteTask(String number) {
        Integer index = parseTaskNumber(number);
        if (index == null) {
            return INVALID_TASK_NUMBER_MESSAGE;
        }

        Task deletedTask = taskList.delete(index);
        return deletedTask == null
                ? INVALID_TASK_NUMBER_MESSAGE
                : "Deleted: " + deletedTask.getDescription();
    }

    private String findTasks(String keyword) {
        return taskList.find(keyword.trim()).stream()
                .map(Task::getDescription)
                .reduce("", (firstDescription, nextDescription) -> firstDescription + nextDescription + "\n")
                .trim();
    }

    private String addTask(String command) {
        Task task = parser.parseTask(command);
        if (task != null) {
            taskList.add(task);
            return "Added task: " + task.getDescription();
        }
        return "I do not understand that command.";
    }

    private String updateTaskStatus(String number, boolean markDone) {
        Integer index = parseTaskNumber(number);
        Task task = index == null ? null : taskList.get(index);
        if (task == null) {
            return INVALID_TASK_NUMBER_MESSAGE;
        }
        if (markDone) {
            task.markAsDone();
            return "Marked task as done: " + task.getDescription();
        }
        task.markAsNotDone();
        return "Marked task as not done: " + task.getDescription();
    }

    private Integer parseTaskNumber(String number) {
        try {
            int oneBasedIndex = Integer.parseInt(number.trim());
            return oneBasedIndex > 0 ? oneBasedIndex - 1 : null;
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    /** Starts the command-line interface. */
    public void run() {
        ui.showWelcome();
        taskList = loadTasks();
        while (true) {
            ui.showLine();
            String command = ui.readCommand();
            ui.showLine();
            if (command.equalsIgnoreCase("bye")) {
                ui.showGoodbye();
                break;
            }
            getResponse(command);
        }
    }

    private TaskList loadTasks() {
        try {
            return new TaskList(storage.loadTasks());
        } catch (IOException exception) {
            return new TaskList();
        }
    }

    /** Starts Trayce. */
    public static void main(String[] args) {
        new Trayce().run();
    }
}
