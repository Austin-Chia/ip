package trayce.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import trayce.task.Deadline;
import trayce.task.Event;
import trayce.task.Note;
import trayce.task.Task;

/** Converts supported task-creation commands into task objects. */
public class Parser {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("^(\\S+)(?:\\s+(.*))?$",
            Pattern.DOTALL);
    private static final Pattern DEADLINE_DELIMITER = Pattern.compile("\\s+/by\\s+",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern EVENT_START_DELIMITER = Pattern.compile("\\s+/from\\s+",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern EVENT_END_DELIMITER = Pattern.compile("\\s+/to\\s+",
            Pattern.CASE_INSENSITIVE);

    /**
     * Creates a new Parser instance.
     */
    public Parser() {
    }

    /**
     * Parses a todo, deadline, event, or note command.
     *
     * @param command the complete user command
     * @return a new task
     * @throws ParseException if the command is incomplete or contains invalid task data
     */
    public Task parseTask(String command) throws ParseException {
        String trimmedCommand = command == null ? "" : command.trim();
        Matcher commandMatcher = COMMAND_PATTERN.matcher(trimmedCommand);
        if (!commandMatcher.matches()) {
            throw new ParseException("The command is empty. Type 'help' to view the trail map.");
        }

        String commandWord = commandMatcher.group(1).toLowerCase(Locale.ROOT);
        String details = commandMatcher.group(2) == null ? "" : commandMatcher.group(2).trim();
        return switch (commandWord) {
        case "todo" -> new Task(requireDescription(details, "todo <description>"));
        case "deadline" -> parseDeadline(details);
        case "event" -> parseEvent(details);
        case "note" -> new Note(requireDescription(details, "note <text>"));
        default -> throw new ParseException("Unknown command '" + commandMatcher.group(1)
                + "'. Type 'help' to view the trail map.");
        };
    }

    /**
     * Parses the description and date in a deadline command.
     *
     * @param taskDetails the details string containing the description and deadline date
     * @return the parsed deadline task
     * @throws ParseException if required details are missing or invalid
     */
    private Task parseDeadline(String taskDetails) throws ParseException {
        Matcher delimiterMatcher = DEADLINE_DELIMITER.matcher(taskDetails);
        if (!delimiterMatcher.find()) {
            throw new ParseException("A deadline needs a description and date. "
                    + "Try: deadline <description> /by <YYYY-MM-DD>");
        }
        int delimiterStart = delimiterMatcher.start();
        int dateStart = delimiterMatcher.end();
        if (delimiterMatcher.find()) {
            throw new ParseException("Use /by exactly once in a deadline command.");
        }

        String description = requireDescription(taskDetails.substring(0, delimiterStart),
                "deadline <description> /by <YYYY-MM-DD>");
        String deadlineText = taskDetails.substring(dateStart).trim();
        LocalDate deadline = parseDate(deadlineText, "deadline");
        return new Deadline(description, deadline);
    }

    /**
     * Parses the description and dates in an event command.
     *
     * @param taskDetails the details string containing the description and event start/end dates
     * @return the parsed event task
     * @throws ParseException if required details are missing or invalid
     */
    private Task parseEvent(String taskDetails) throws ParseException {
        Matcher startMatcher = EVENT_START_DELIMITER.matcher(taskDetails);
        Matcher endMatcher = EVENT_END_DELIMITER.matcher(taskDetails);
        if (!startMatcher.find() || !endMatcher.find() || endMatcher.start() <= startMatcher.start()) {
            throw new ParseException("An event needs a description and ordered dates. "
                    + "Try: event <description> /from <YYYY-MM-DD> /to <YYYY-MM-DD>");
        }
        int descriptionEnd = startMatcher.start();
        int startDateStart = startMatcher.end();
        int startDateEnd = endMatcher.start();
        int endDateStart = endMatcher.end();
        if (startMatcher.find() || endMatcher.find()) {
            throw new ParseException("Use /from and /to exactly once in an event command.");
        }

        String description = requireDescription(taskDetails.substring(0, descriptionEnd),
                "event <description> /from <YYYY-MM-DD> /to <YYYY-MM-DD>");
        LocalDate startDate = parseDate(taskDetails.substring(startDateStart, startDateEnd).trim(),
                "event start");
        LocalDate endDate = parseDate(taskDetails.substring(endDateStart).trim(), "event end");
        if (!startDate.isBefore(endDate)) {
            throw new ParseException("The event start date must be before its end date.");
        }
        return new Event(description, startDate, endDate);
    }

    /** Returns a nonblank description or explains the required command format. */
    private String requireDescription(String description, String example) throws ParseException {
        String trimmedDescription = description.trim();
        if (trimmedDescription.isEmpty()) {
            throw new ParseException("The description cannot be empty. Try: " + example);
        }
        if (trimmedDescription.chars().anyMatch(Character::isISOControl)) {
            throw new ParseException("Descriptions cannot contain control characters.");
        }
        return trimmedDescription;
    }

    /** Parses one strict ISO date and converts low-level parsing failures into user guidance. */
    private LocalDate parseDate(String dateText, String fieldName) throws ParseException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException exception) {
            throw new ParseException("The " + fieldName
                    + " date must be a real date in YYYY-MM-DD format.");
        }
    }
}
