/** Represents a task that occurs between a start and end time. */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task.
     *
     * @param description the event description
     * @param from the start time, stored as entered by the user
     * @param to the end time, stored as entered by the user
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    @Override
    public String getDateTimeDetails() {
        return " (from: " + from + " to: " + to + ")";
    }
}
