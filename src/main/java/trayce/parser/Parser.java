package trayce.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import trayce.task.Deadline;
import trayce.task.Event;
import trayce.task.Task;

/** Converts supported task-creation commands into task objects. */
public class Parser {
    private static final String TODO_COMMAND_PREFIX = "todo ";
    private static final String DEADLINE_COMMAND_PREFIX = "deadline ";
    private static final String EVENT_COMMAND_PREFIX = "event ";
    private static final String DEADLINE_DELIMITER = " /by ";
    private static final String EVENT_START_DELIMITER = " /from ";
    private static final String EVENT_END_DELIMITER = " /to ";

    /**
     * Creates a new Parser instance.
     */
    public Parser() {
    }

    /**
     * Parses a todo, deadline, or event command.
     *
     * @param command the complete user command
     * @return a new task, or {@code null} if the command is invalid
     */
    public Task parseTask(String command) {
        String lowerCaseCommand = command.toLowerCase(Locale.ROOT);
        if (lowerCaseCommand.startsWith(TODO_COMMAND_PREFIX)) {
            String description = command.substring(TODO_COMMAND_PREFIX.length()).trim();
            return description.isEmpty() ? null : new Task(description);
        }
        if (lowerCaseCommand.startsWith(DEADLINE_COMMAND_PREFIX)) {
            return parseDeadline(command.substring(DEADLINE_COMMAND_PREFIX.length()).trim());
        }
        if (lowerCaseCommand.startsWith(EVENT_COMMAND_PREFIX)) {
            return parseEvent(command.substring(EVENT_COMMAND_PREFIX.length()).trim());
        }
        return null;
    }

    /**
     * Parses the description and date in a deadline command.
     *
     * @param taskDetails the details string containing the description and deadline date
     * @return the parsed deadline task, or {@code null} if parsing failed or input format was invalid
     */
    private Task parseDeadline(String taskDetails) {
        int delimiterIndex = taskDetails.toLowerCase(Locale.ROOT).indexOf(DEADLINE_DELIMITER);
        if (delimiterIndex <= 0) {
            return null;
        }

        String description = taskDetails.substring(0, delimiterIndex).trim();
        String deadlineText = taskDetails.substring(delimiterIndex + DEADLINE_DELIMITER.length()).trim();
        if (deadlineText.isEmpty()) {
            return null;
        }

        try {
            return new Deadline(description, LocalDate.parse(deadlineText));
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    /**
     * Parses the description and dates in an event command.
     *
     * @param taskDetails the details string containing the description and event start/end dates
     * @return the parsed event task, or {@code null} if parsing failed or input format was invalid
     */
    private Task parseEvent(String taskDetails) {
        String lowerCaseDetails = taskDetails.toLowerCase(Locale.ROOT);
        int startDelimiterIndex = lowerCaseDetails.indexOf(EVENT_START_DELIMITER);
        int endDelimiterIndex = lowerCaseDetails.indexOf(EVENT_END_DELIMITER);
        if (startDelimiterIndex <= 0 || endDelimiterIndex <= startDelimiterIndex) {
            return null;
        }

        String description = taskDetails.substring(0, startDelimiterIndex).trim();
        String startDateText = taskDetails.substring(
                startDelimiterIndex + EVENT_START_DELIMITER.length(), endDelimiterIndex).trim();
        String endDateText = taskDetails.substring(endDelimiterIndex + EVENT_END_DELIMITER.length()).trim();
        if (startDateText.isEmpty() || endDateText.isEmpty()) {
            return null;
        }

        try {
            return new Event(description, LocalDate.parse(startDateText), LocalDate.parse(endDateText));
        } catch (DateTimeParseException exception) {
            return null;
        }
    }
}
