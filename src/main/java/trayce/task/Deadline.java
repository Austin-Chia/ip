package trayce.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Represents a task that must be completed by a specified time. */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd uuuu");
    private final LocalDate by;

    /**
     * Creates a deadline task.
     *
     * @param description the task description
     * @param by the deadline date
     */
    public Deadline(String description, LocalDate by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    public String getDateTimeDetails() {
        return " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Returns the deadline date.
     *
     * @return the deadline date
     */
    public LocalDate getBy() {
        return by;
    }
}
