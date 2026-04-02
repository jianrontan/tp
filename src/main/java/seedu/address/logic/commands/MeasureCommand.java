package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BODY_FAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HEIGHT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEIGHT;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.BodyFatPercentage;
import seedu.address.model.person.Height;
import seedu.address.model.person.Person;
import seedu.address.model.person.Weight;

/**
 * Updates body measurements for a client identified by index.
 */
public class MeasureCommand extends Command {

    public static final String COMMAND_WORD = "measure";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Updates body measurements for the client identified by the index number used in the displayed"
            + " client list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_HEIGHT + "HEIGHT_CM] "
            + "[" + PREFIX_WEIGHT + "WEIGHT_KG] "
            + "[" + PREFIX_BODY_FAT + "BODY_FAT_PERCENTAGE]\n"
            + "At least one of h/, w/, or bf/ must be provided (e.g., 'measure 1' is invalid).\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_HEIGHT + "175.5 "
            + PREFIX_WEIGHT + "72.0 "
            + PREFIX_BODY_FAT + "14.8";

    public static final String MESSAGE_HEIGHT_SET_SUCCESS = "Height added/updated to %2$s for client: %1$s";
    public static final String MESSAGE_HEIGHT_CLEAR_SUCCESS = "Height cleared for client: %1$s";
    public static final String MESSAGE_HEIGHT_ALREADY_CLEARED = "Height is already cleared for client: %1$s";

    public static final String MESSAGE_WEIGHT_SET_SUCCESS = "Weight added/updated to %2$s for client: %1$s";
    public static final String MESSAGE_WEIGHT_CLEAR_SUCCESS = "Weight cleared for client: %1$s";
    public static final String MESSAGE_WEIGHT_ALREADY_CLEARED = "Weight is already cleared for client: %1$s";

    public static final String MESSAGE_BODY_FAT_SET_SUCCESS = "Body Fat added/updated to %2$s for client: %1$s";
    public static final String MESSAGE_BODY_FAT_CLEAR_SUCCESS = "Body Fat cleared for client: %1$s";
    public static final String MESSAGE_BODY_FAT_ALREADY_CLEARED =
            "Body Fat is already cleared for client: %1$s";

    private final Index index;
    private final Height height;
    private final Weight weight;
    private final BodyFatPercentage bodyFatPercentage;

    /**
     * Creates a MeasureCommand.
     */
    public MeasureCommand(Index index, Height height, Weight weight, BodyFatPercentage bodyFatPercentage) {
        requireNonNull(index);

        this.index = index;
        this.height = height;
        this.weight = weight;
        this.bodyFatPercentage = bodyFatPercentage;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Height updatedHeight = height != null ? height : personToEdit.getHeight();
        Weight updatedWeight = weight != null ? weight : personToEdit.getWeight();
        BodyFatPercentage updatedBodyFatPercentage = bodyFatPercentage != null
                ? bodyFatPercentage : personToEdit.getBodyFatPercentage();

        Person editedPerson = new Person(
                personToEdit.getId(),
                personToEdit.getName(),
                personToEdit.getGender(),
                personToEdit.getDateOfBirth(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getLocation(),
                personToEdit.getNote(),
                personToEdit.getPlan(),
                personToEdit.getRate(),
                personToEdit.getStatus(),
                updatedHeight,
                updatedWeight,
                updatedBodyFatPercentage,
                personToEdit.getTags());

        model.setPerson(personToEdit, editedPerson);

        String message = formatOutcomeMessages(personToEdit, editedPerson.getName().toString());
        return new CommandResult(message);
    }

    /** Returns field-specific outcomes in deterministic order (h/, w/, bf/). */
    private String formatOutcomeMessages(Person personBeforeEdit, String clientName) {
        StringJoiner joiner = new StringJoiner("\n");
        if (height != null) {
            joiner.add(formatHeightOutcome(clientName, personBeforeEdit.getHeight().value));
        }
        if (weight != null) {
            joiner.add(formatWeightOutcome(clientName, personBeforeEdit.getWeight().value));
        }
        if (bodyFatPercentage != null) {
            joiner.add(formatBodyFatOutcome(clientName, personBeforeEdit.getBodyFatPercentage().value));
        }
        return joiner.toString();
    }

    private String formatHeightOutcome(String clientName, String oldValue) {
        if (height.value.isEmpty()) {
            String message = oldValue.isEmpty() ? MESSAGE_HEIGHT_ALREADY_CLEARED : MESSAGE_HEIGHT_CLEAR_SUCCESS;
            return String.format(message, clientName);
        }
        return String.format(MESSAGE_HEIGHT_SET_SUCCESS, clientName, height.value);
    }

    private String formatWeightOutcome(String clientName, String oldValue) {
        if (weight.value.isEmpty()) {
            String message = oldValue.isEmpty() ? MESSAGE_WEIGHT_ALREADY_CLEARED : MESSAGE_WEIGHT_CLEAR_SUCCESS;
            return String.format(message, clientName);
        }
        return String.format(MESSAGE_WEIGHT_SET_SUCCESS, clientName, weight.value);
    }

    private String formatBodyFatOutcome(String clientName, String oldValue) {
        if (bodyFatPercentage.value.isEmpty()) {
            String message = oldValue.isEmpty() ? MESSAGE_BODY_FAT_ALREADY_CLEARED : MESSAGE_BODY_FAT_CLEAR_SUCCESS;
            return String.format(message, clientName);
        }
        return String.format(MESSAGE_BODY_FAT_SET_SUCCESS, clientName, bodyFatPercentage.value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof MeasureCommand)) {
            return false;
        }

        MeasureCommand otherCommand = (MeasureCommand) other;
        return index.equals(otherCommand.index)
                && Objects.equals(height, otherCommand.height)
                && Objects.equals(weight, otherCommand.weight)
                && Objects.equals(bodyFatPercentage, otherCommand.bodyFatPercentage);
    }
}
