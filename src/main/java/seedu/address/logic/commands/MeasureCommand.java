package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BODY_FAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HEIGHT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEIGHT;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

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

    public static final String MESSAGE_SET_SUCCESS = "Measurements added/updated to %2$s for client: %1$s";
    public static final String MESSAGE_CLEAR_SUCCESS = "Measurements cleared for client: %1$s";
    public static final String MESSAGE_MEASUREMENTS_ALREADY_CLEARED =
            "Specified measurements are already cleared for client: %1$s";

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
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        boolean allSpecifiedMeasurementsClear = (height == null || height.value.isEmpty())
                && (weight == null || weight.value.isEmpty())
                && (bodyFatPercentage == null || bodyFatPercentage.value.isEmpty());
        boolean allSpecifiedMeasurementsAlreadyCleared = (height == null || personToEdit.getHeight().value.isEmpty())
                && (weight == null || personToEdit.getWeight().value.isEmpty())
                && (bodyFatPercentage == null || personToEdit.getBodyFatPercentage().value.isEmpty());

        String message;
        if (allSpecifiedMeasurementsClear && allSpecifiedMeasurementsAlreadyCleared) {
            message = MESSAGE_MEASUREMENTS_ALREADY_CLEARED;
        } else if (allSpecifiedMeasurementsClear) {
            message = MESSAGE_CLEAR_SUCCESS;
        } else {
            message = MESSAGE_SET_SUCCESS;
        }

        String measurementSummary = formatSpecifiedMeasurements();
        return new CommandResult(String.format(message, editedPerson.getName(), measurementSummary));
    }

    /**
     * Returns a deterministic summary of specified measurement prefixes in command input order.
     */
    private String formatSpecifiedMeasurements() {
        StringJoiner joiner = new StringJoiner(", ");
        if (height != null) {
            joiner.add(PREFIX_HEIGHT + height.value);
        }
        if (weight != null) {
            joiner.add(PREFIX_WEIGHT + weight.value);
        }
        if (bodyFatPercentage != null) {
            joiner.add(PREFIX_BODY_FAT + bodyFatPercentage.value);
        }
        return joiner.toString();
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

