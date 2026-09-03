package trayce;

import java.io.IOException;
import trayce.parser.Parser;
import trayce.storage.Storage;
import trayce.task.Task;
import trayce.task.TaskList;
import trayce.ui.Ui;

/** Coordinates user input, task management, and persistent storage for Trayce. */
public class Trayce {
    private final Ui ui = new Ui();
    private final Storage storage = new Storage();
    private final Parser parser = new Parser();
    private TaskList taskList = new TaskList();

    /** Creates a Trayce application with its required collaborators. */
    public Trayce() { }

    /** Processes a command for the graphical interface and returns a response. */
    public String getResponse(String command) {
        if (command.equalsIgnoreCase("list")) {
            return taskList.getTasks().stream().map(Task::getDescription).reduce("", (a, b) -> a + b + "\n");
        }
        Task task = parser.parseTask(command);
        if (task != null) {
            taskList.add(task);
            return "Added task: " + task.getDescription();
        }
        return "I do not understand that command.";
    }

    /** Starts the command-line interface. */
    public void run() {
        ui.showWelcome();
        taskList = loadTasks();
        while (true) {
            ui.showLine();
            String command = ui.readCommand();
            ui.showLine();
            if (command.equalsIgnoreCase("bye")) { ui.showGoodbye(); break; }
            getResponse(command);
        }
    }

    private TaskList loadTasks() {
        try { return new TaskList(storage.loadTasks()); }
        catch (IOException exception) { return new TaskList(); }
    }

    /** Starts Trayce. */
    public static void main(String[] args) { new Trayce().run(); }
}
