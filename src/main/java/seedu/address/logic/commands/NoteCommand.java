package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;

/**
 * Adds a note to a person in PowerRoster.
 */
public class NoteCommand extends Command {

    public static final String COMMAND_WORD = "note";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a note to the specified person by index number used in the last person listing.\n"
            + "Existing notes will be overwritten.\n"
            + "Parameters: INDEX (must be a positive integer) " + PREFIX_NOTE + "NOTE_CONTENT\n"
            + "Example: " + COMMAND_WORD + " 1 Focuses on strength more.";

    public static final String MESSAGE_ADD_SUCCESS = "Note added to person: %1$s";
    public static final String MESSAGE_DELETE_SUCCESS = "Note deleted from person: %1$s";
    public static final String MESSAGE_ARGUMENTS = "Index: %1$d, Note: %2$s";

    private final Index index;
    private final Note note;

    /**
     * Creates an NoteCommand to add the specified {@code note} to the person at the specified
     * {@code index}.
     *
     * @param index of the person in the last person list to edit the note
     * @param note of the person to be updated to
     */
    public NoteCommand(Index index, Note note) {
        requireNonNull(index);
        requireNonNull(note);

        this.index = index;
        this.note = note;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getGender(),
                personToEdit.getDateOfBirth(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getLocation(), note,
                personToEdit.getTags());
        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(generateSuccessMessage(personToEdit));
    }

    /**
     * Generates a command execution success message based on whether the note is added to or
     * removed from {@code personToEdit}. An empty note is considered as a deletion of the existing
     * note.
     */
    private String generateSuccessMessage(Person personToEdit) {
        String message = !note.value.isEmpty() ? MESSAGE_ADD_SUCCESS : MESSAGE_DELETE_SUCCESS;
        return String.format(message, Messages.format(personToEdit));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NoteCommand)) {
            return false;
        }

        NoteCommand otherNoteCommand = (NoteCommand) other;
        return index.equals(otherNoteCommand.index) && note.equals(otherNoteCommand.note);
    }
}
