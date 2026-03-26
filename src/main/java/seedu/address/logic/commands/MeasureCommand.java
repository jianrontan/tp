package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BODY_FAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HEIGHT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEIGHT;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.BodyFatPercentage;
import seedu.address.model.person.Height;
import seedu.address.model.person.Person;
import seedu.address.model.person.Weight;

/**
 * Updates body measurements for a person identified by index.
 */
public class MeasureCommand extends Command {

    public static final String COMMAND_WORD = "measure";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Updates body measurements for the specified person by index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_HEIGHT + "HEIGHT_CM] "
            + "[" + PREFIX_WEIGHT + "WEIGHT_KG] "
            + "[" + PREFIX_BODY_FAT + "BODY_FAT_PERCENTAGE]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_HEIGHT + "175.5 "
            + PREFIX_WEIGHT + "72.0 "
            + PREFIX_BODY_FAT + "14.8";

    public static final String MESSAGE_NOT_EDITED = "At least one measurement to set must be provided.";
    public static final String MESSAGE_SUCCESS = "Updated measurements for person: %1$s";

    private final Index index;
    private final MeasureDescriptor measureDescriptor;

    /**
     * Creates a MeasureCommand.
     */
    public MeasureCommand(Index index, MeasureDescriptor measureDescriptor) {
        requireNonNull(index);
        requireNonNull(measureDescriptor);

        this.index = index;
        this.measureDescriptor = new MeasureDescriptor(measureDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
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
                measureDescriptor.getHeight().orElse(personToEdit.getHeight()),
                measureDescriptor.getWeight().orElse(personToEdit.getWeight()),
                measureDescriptor.getBodyFatPercentage().orElse(personToEdit.getBodyFatPercentage()),
                personToEdit.getTags());

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(editedPerson)));
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
                && measureDescriptor.equals(otherCommand.measureDescriptor);
    }

    /**
     * Stores the body measurement fields to update.
     */
    public static class MeasureDescriptor {
        private Height height;
        private Weight weight;
        private BodyFatPercentage bodyFatPercentage;

        public MeasureDescriptor() {}

        /**
         * Copy constructor.
         */
        public MeasureDescriptor(MeasureDescriptor toCopy) {
            setHeight(toCopy.height);
            setWeight(toCopy.weight);
            setBodyFatPercentage(toCopy.bodyFatPercentage);
        }

        /**
         * Returns true if at least one measurement field is set.
         */
        public boolean isAnyFieldEdited() {
            return height != null || weight != null || bodyFatPercentage != null;
        }

        public void setHeight(Height height) {
            this.height = height;
        }

        public Optional<Height> getHeight() {
            return Optional.ofNullable(height);
        }

        public void setWeight(Weight weight) {
            this.weight = weight;
        }

        public Optional<Weight> getWeight() {
            return Optional.ofNullable(weight);
        }

        public void setBodyFatPercentage(BodyFatPercentage bodyFatPercentage) {
            this.bodyFatPercentage = bodyFatPercentage;
        }

        public Optional<BodyFatPercentage> getBodyFatPercentage() {
            return Optional.ofNullable(bodyFatPercentage);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof MeasureDescriptor)) {
                return false;
            }
            MeasureDescriptor otherDescriptor = (MeasureDescriptor) other;
            return Objects.equals(height, otherDescriptor.height)
                    && Objects.equals(weight, otherDescriptor.weight)
                    && Objects.equals(bodyFatPercentage, otherDescriptor.bodyFatPercentage);
        }

        @Override
        public int hashCode() {
            return Objects.hash(height, weight, bodyFatPercentage);
        }
    }
}

