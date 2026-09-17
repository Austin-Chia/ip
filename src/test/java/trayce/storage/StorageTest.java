package trayce.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import trayce.task.Note;
import trayce.task.Task;

/** Tests saving and loading tasks using an isolated temporary file. */
class StorageTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    void saveAndLoad_note_preservesTextAndType() throws IOException {
        Storage storage = new Storage(temporaryDirectory.resolve("trayce.txt"));
        Note originalNote = new Note("watch Dune\tPart Two");

        storage.saveTasks(List.of(originalNote));
        List<Task> loadedTasks = storage.loadTasks();

        Note loadedNote = assertInstanceOf(Note.class, loadedTasks.get(0));
        assertEquals("watch Dune\tPart Two", loadedNote.getDescription());
        assertFalse(loadedNote.isDone());
    }

    @Test
    void loadTasks_corruptedLines_skipsInvalidDataAndLoadsValidTasks() throws IOException {
        Path dataFile = temporaryDirectory.resolve("trayce.txt");
        Files.writeString(dataFile, String.join("\n",
                "D\t0\timpossible date\t2026-02-30",
                "E\t0\treversed event\t2026-09-20\t2026-09-19",
                "T\tmaybe\tinvalid status",
                "N\t1\tnote cannot be complete",
                "T\t0\tvalid task"), StandardCharsets.UTF_8);
        Storage storage = new Storage(dataFile);

        List<Task> loadedTasks = storage.loadTasks();

        assertEquals(1, loadedTasks.size());
        assertEquals("valid task", loadedTasks.get(0).getDescription());
    }
}
