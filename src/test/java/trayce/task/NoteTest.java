package trayce.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

/** Tests behavior that distinguishes notes from completable tasks. */
class NoteTest {
    @Test
    void note_referenceInformation_isNotMarkableAndUsesNoteIcon() {
        Note note = new Note("waist size is 76 cm");

        assertFalse(note.isMarkable());
        assertEquals("N", note.getTypeIcon());
        assertEquals("[N] waist size is 76 cm", note.getCompactDisplay());
    }
}
