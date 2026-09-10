package trayce.task;

/** Represents a short piece of reference information recorded by the user. */
public class Note extends Task {
    /**
     * Creates a note containing the specified text.
     *
     * @param description the information to remember
     */
    public Note(String description) {
        super(description, TaskType.NOTE);
    }

    /**
     * Notes record information and therefore have no completion status.
     *
     * @return {@code false}
     */
    @Override
    public boolean isMarkable() {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @return the note icon followed by its text
     */
    @Override
    public String getCompactDisplay() {
        return "[" + getTypeIcon() + "] " + description;
    }
}
