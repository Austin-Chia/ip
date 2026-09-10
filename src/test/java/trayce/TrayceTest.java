package trayce;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import trayce.storage.Storage;

/** Tests note management through the user-facing command interface. */
class TrayceTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    void noteCommands_addListFindRejectMarkAndDeleteNote() {
        Storage storage = new Storage(temporaryDirectory.resolve("trayce.txt"));
        Trayce trayce = new Trayce(storage);

        assertEquals("Added note: remember Interstellar", trayce.getResponse("note remember Interstellar"));
        assertEquals("1. [N] remember Interstellar", trayce.getResponse("list"));
        assertEquals("remember Interstellar", trayce.getResponse("find interstellar"));
        assertEquals("Notes cannot be marked or unmarked.", trayce.getResponse("mark 1"));

        Trayce restartedTrayce = new Trayce(storage);
        assertEquals("1. [N] remember Interstellar", restartedTrayce.getResponse("list"));

        assertEquals("Deleted: remember Interstellar", trayce.getResponse("delete 1"));
        assertEquals("Your task list is empty.", trayce.getResponse("list"));
    }
}
