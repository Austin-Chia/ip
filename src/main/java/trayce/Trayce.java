package trayce;

import java.io.IOException;
import java.util.List;

import trayce.parser.Parser;
import trayce.storage.Storage;
import trayce.task.Task;
import trayce.task.TaskList;
import trayce.ui.Ui;

/** Coordinates user input, task management, and persistent storage for Trayce. */
public class Trayce {
    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private TaskList taskList;

    /** Creates a Trayce application with its required collaborators. */
    public Trayce() {
        ui = new Ui();
        storage = new Storage();
        parser = new Parser();
        taskList = new TaskList();
    }

    /** Starts the command loop. */
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
            handleCommand(command);
            ui.showLine();
        }
    }

    /**
     * Handles one complete user command.
     *
     * @param command the user-entered command string
     */
    private void handleCommand(String command) {
        if (command.equalsIgnoreCase("list")) {
            ui.showTaskList(taskList.getTasks());
        } else if (command.toLowerCase().startsWith("find ")) {
            String keyword = command.substring(5).trim();
            ui.showMatchingTasks(taskList.find(keyword));
        } else if (command.toLowerCase().startsWith("delete ")) {
            deleteTask(command.substring(7).trim());
        } else if (command.toLowerCase().startsWith("mark ")) {
            markTask(command.substring(5).trim(), true);
        } else if (command.toLowerCase().startsWith("unmark ")) {
            markTask(command.substring(7).trim(), false);
        } else {
            addTask(command);
        }
    }

    /**
     * Adds a task created from the command, if valid.
     *
     * @param command the task creation command string
     */
    private void addTask(String command) {
        if (taskList.isFull()) {
            ui.showTaskLimitReached();
            return;
        }
        Task task = parser.parseTask(command);
        if (task == null) {
            ui.showInvalidTaskCommand(command);
            return;
        }
        taskList.add(task);
        saveTasks();
        ui.showTaskAdded(task, taskList.size());
    }

    /**
     * Deletes the task whose number was supplied by the user.
     *
     * @param taskNumberText the user input representing the task number to delete
     */
    private void deleteTask(String taskNumberText) {
        Integer taskIndex = parseTaskIndex(taskNumberText);
        if (taskIndex == null) {
            return;
        }
        Task deletedTask = taskList.delete(taskIndex);
        if (deletedTask == null) {
            ui.showMissingTaskNumber();
            return;
        }
        saveTasks();
        ui.showTaskDeleted(deletedTask, taskList.size());
    }

    /**
     * Marks or unmarks the task whose number was supplied by the user.
     *
     * @param taskNumberText the user input representing the task number
     * @param markDone {@code true} to mark the task as done, {@code false} to mark it as not done
     */
    private void markTask(String taskNumberText, boolean markDone) {
        Integer taskIndex = parseTaskIndex(taskNumberText);
        if (taskIndex == null) {
            return;
        }
        Task task = taskList.get(taskIndex);
        if (task == null) {
            ui.showMissingTaskNumber();
            return;
        }
        if (markDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        saveTasks();
        ui.showTaskMarked(task, markDone);
    }

    /**
     * Converts a one-based task number from the user into a zero-based list index.
     *
     * @param taskNumberText the user input representing the task number
     * @return the zero-based task index, or {@code null} if the task number is invalid
     */
    private Integer parseTaskIndex(String taskNumberText) {
        try {
            return Integer.parseInt(taskNumberText) - 1;
        } catch (NumberFormatException exception) {
            ui.showInvalidTaskNumber();
            return null;
        }
    }

    /**
     * Loads saved tasks, using an empty list if the file cannot be read.
     *
     * @return the loaded task list, or an empty task list if load failed
     */
    private TaskList loadTasks() {
        try {
            List<Task> savedTasks = storage.loadTasks();
            return new TaskList(savedTasks);
        } catch (IOException exception) {
            ui.showLoadError();
            return new TaskList();
        }
    }

    /** Saves the current task list and reports an error if disk access fails. */
    private void saveTasks() {
        try {
            storage.saveTasks(taskList.getTasks());
        } catch (IOException exception) {
            ui.showSaveError();
        }
    }

    /**
     * Starts Trayce.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        new Trayce().run();
    }
}
