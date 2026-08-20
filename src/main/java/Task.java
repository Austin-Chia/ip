/**
 * Represents a task entered by the user.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a new incomplete task.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks this task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the symbol used to display this task's status.
     *
     * @return {@code X} when done, otherwise a blank space
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the type icon shown before the task status.
     *
     * @return {@code T} for a basic todo task
     */
    public String getTypeIcon() {
        return "T";
    }

    /**
     * Returns any date or time details for display.
     *
     * @return an empty string for a todo task
     */
    public String getDateTimeDetails() {
        return "";
    }
}
