# Manual Test Plan

## D-Notes

| Scenario | Command | Expected result |
|---|---|---|
| Add a note | `note waist size is 76 cm` | `Added note: waist size is 76 cm` |
| Reject an empty note | `note` | `I do not understand that command.` |
| List a note | `list` | The note appears as `[N] waist size is 76 cm`. |
| Find a note | `find waist` | `waist size is 76 cm` |
| Reject marking a note | `mark 1` | `Notes cannot be marked or unmarked.` |
| Delete a note | `delete 1` | `Deleted: waist size is 76 cm` |
| Restore a note | Restart Trayce after adding a note | The note is loaded with the same text and `[N]` icon. |
