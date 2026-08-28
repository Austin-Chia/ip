import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Converts supported task-creation commands into task objects. */
public class Parser {
    /**
     * Parses a todo, deadline, or event command.
     *
     * @param command the complete user command
     * @return a new task, or {@code null} if the command is invalid
     */
    public Task parseTask(String command) {
        String lowerCaseCommand = command.toLowerCase();
        if (lowerCaseCommand.startsWith("todo ")) {
            String description = command.substring(5).trim();
            return description.isEmpty() ? null : new Task(description);
        }
        if (lowerCaseCommand.startsWith("deadline ")) {
            return parseDeadline(command.substring(9).trim());
        }
        if (lowerCaseCommand.startsWith("event ")) {
            return parseEvent(command.substring(6).trim());
        }
        return null;
    }

    /** Parses the description and date in a deadline command. */
    private Task parseDeadline(String taskDetails) {
        int byIndex = taskDetails.toLowerCase().indexOf(" /by ");
        if (byIndex <= 0 || taskDetails.substring(byIndex + 5).trim().isEmpty()) {
            return null;
        }
        try {
            return new Deadline(taskDetails.substring(0, byIndex).trim(),
                    LocalDate.parse(taskDetails.substring(byIndex + 5).trim()));
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    /** Parses the description and dates in an event command. */
    private Task parseEvent(String taskDetails) {
        int fromIndex = taskDetails.toLowerCase().indexOf(" /from ");
        int toIndex = taskDetails.toLowerCase().indexOf(" /to ");
        if (fromIndex <= 0 || toIndex <= fromIndex || taskDetails.substring(toIndex + 5).trim().isEmpty()) {
            return null;
        }
        try {
            return new Event(taskDetails.substring(0, fromIndex).trim(),
                    LocalDate.parse(taskDetails.substring(fromIndex + 7, toIndex).trim()),
                    LocalDate.parse(taskDetails.substring(toIndex + 5).trim()));
        } catch (DateTimeParseException exception) {
            return null;
        }
    }
}
