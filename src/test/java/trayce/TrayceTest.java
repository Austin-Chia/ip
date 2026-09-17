package trayce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import trayce.storage.Storage;
import trayce.task.Task;

/** Tests note management through the user-facing command interface. */
class TrayceTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    void noteCommands_addListFindRejectMarkAndDeleteNote() {
        Storage storage = new Storage(temporaryDirectory.resolve("trayce.txt"));
        Trayce trayce = new Trayce(storage);

        assertEquals("Packed for the journey! Added note: remember Interstellar",
                trayce.getResponse("note remember Interstellar"));
        assertEquals("1. [N] remember Interstellar", trayce.getResponse("list"));
        assertEquals("remember Interstellar", trayce.getResponse("find interstellar"));
        assertEquals("That is a trail note, so it cannot be marked or unmarked.",
                trayce.getResponse("mark 1"));

        Trayce restartedTrayce = new Trayce(storage);
        assertEquals("1. [N] remember Interstellar", restartedTrayce.getResponse("list"));

        assertEquals("Trail cleared! Removed: remember Interstellar", trayce.getResponse("delete 1"));
        assertEquals("The trail is clear — your task list is empty.", trayce.getResponse("list"));
    }

    @Test
    void invalidAndDuplicateCommands_returnSpecificGuidanceWithoutChangingList() {
        Trayce trayce = new Trayce(new Storage(temporaryDirectory.resolve("errors.txt")));

        assertEquals("The command is empty. Type 'help' to view the trail map.",
                trayce.getResponse("   "));
        assertEquals("The event start date must be before its end date.",
                trayce.getResponse("event trip /from 2026-10-02 /to 2026-10-01"));
        assertEquals("Packed for the journey! Added task: read book",
                trayce.getResponse("todo read book"));
        assertEquals("That item is already on the trail.",
                trayce.getResponse("todo READ BOOK"));
        assertEquals("Tell me what to search for. Try: find <keyword>",
                trayce.getResponse("find"));
        assertEquals("No trail items match 'homework'.", trayce.getResponse("find homework"));
        assertEquals("1. [ ] read book", trayce.getResponse("list"));
    }

    @Test
    void startup_storageCannotBeRead_warnsUserAndStartsWithEmptyList() {
        Storage unreadableStorage = new Storage(temporaryDirectory.resolve("unreadable.txt")) {
            @Override
            public List<Task> loadTasks() throws IOException {
                throw new IOException("Access denied");
            }
        };

        Trayce trayce = new Trayce(unreadableStorage);

        assertTrue(trayce.getGreeting().contains("could not read the saved trail"));
        assertEquals("The trail is clear — your task list is empty.", trayce.getResponse("list"));
    }
}
