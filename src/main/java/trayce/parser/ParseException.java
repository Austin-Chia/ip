package trayce.parser;

/** Indicates that a user command could not be converted into a valid task. */
public class ParseException extends Exception {
    /**
     * Creates an exception containing guidance that can be shown directly to the user.
     *
     * @param message explanation of the invalid command and how to correct it
     */
    public ParseException(String message) {
        super(message);
    }
}
