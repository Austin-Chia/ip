package trayce.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import trayce.task.Deadline;
import trayce.task.Event;
import trayce.task.Note;
import trayce.task.Task;

/** Tests conversion of user task commands into the correct task objects. */
class ParserTest {
    private final Parser parser = new Parser();

    @Test
    void parseTask_validCommands_createCorrectTaskTypesAndDetails() throws ParseException {
        Task todo = parser.parseTask("  TODO   read book  ");
        Deadline deadline = assertInstanceOf(Deadline.class,
                parser.parseTask("deadline submit report   /by   2026-09-15"));
        Event event = assertInstanceOf(Event.class,
                parser.parseTask("event orientation /from 2026-09-01 /to 2026-09-03"));
        Note note = assertInstanceOf(Note.class,
                parser.parseTask("note waist size is 76 cm"));

        assertEquals("read book", todo.getDescription());
        assertEquals("submit report", deadline.getDescription());
        assertEquals(LocalDate.of(2026, 9, 15), deadline.getBy());
        assertEquals(LocalDate.of(2026, 9, 1), event.getFrom());
        assertEquals(LocalDate.of(2026, 9, 3), event.getTo());
        assertEquals("waist size is 76 cm", note.getDescription());
    }

    @Test
    void parseTask_missingDetailsOrInvalidDate_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parseTask("todo"));
        assertThrows(ParseException.class,
                () -> parser.parseTask("deadline return book /by 15/09/2026"));
        assertThrows(ParseException.class,
                () -> parser.parseTask("deadline return book /by 2026-02-30"));
        assertThrows(ParseException.class,
                () -> parser.parseTask("event meeting /from 2026-09-01"));
        assertThrows(ParseException.class, () -> parser.parseTask("note"));
        assertThrows(ParseException.class, () -> parser.parseTask("unknown command"));
    }

    @Test
    void parseTask_duplicateParametersOrInvalidEventRange_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parseTask(
                "deadline submit /by 2026-09-20 /by 2026-09-21"));
        assertThrows(ParseException.class, () -> parser.parseTask(
                "event camp /from 2026-09-20 /to 2026-09-20"));
        assertThrows(ParseException.class, () -> parser.parseTask(
                "event camp /from 2026-09-21 /to 2026-09-20"));
    }
}
