package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_BODY_FAT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_HEIGHT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WEIGHT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.BodyFatPercentage;
import seedu.address.model.person.Height;
import seedu.address.model.person.Person;
import seedu.address.model.person.Weight;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for MeasureCommand.
 */
public class MeasureCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_updateMeasurementsUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson)
                .withHeight(VALID_HEIGHT_AMY)
                .withWeight(VALID_WEIGHT_AMY)
                .withBodyFatPercentage(VALID_BODY_FAT_AMY)
                .build();

        MeasureCommand measureCommand = new MeasureCommand(INDEX_FIRST_PERSON,
                Optional.of(new Height(VALID_HEIGHT_AMY)),
                Optional.of(new Weight(VALID_WEIGHT_AMY)),
                Optional.of(new BodyFatPercentage(VALID_BODY_FAT_AMY)));

        String expectedMessage = String.format(MeasureCommand.MESSAGE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(measureCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_updateMeasurementsFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson)
                .withHeight(VALID_HEIGHT_AMY)
                .build();

        MeasureCommand measureCommand = new MeasureCommand(INDEX_FIRST_PERSON,
                Optional.of(new Height(VALID_HEIGHT_AMY)),
                Optional.empty(),
                Optional.empty());

        String expectedMessage = String.format(MeasureCommand.MESSAGE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(measureCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        MeasureCommand measureCommand = new MeasureCommand(outOfBoundIndex,
                Optional.of(new Height(VALID_HEIGHT_AMY)),
                Optional.empty(),
                Optional.empty());

        assertCommandFailure(measureCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;

        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        MeasureCommand measureCommand = new MeasureCommand(outOfBoundIndex,
                Optional.of(new Height(VALID_HEIGHT_AMY)),
                Optional.empty(),
                Optional.empty());

        assertCommandFailure(measureCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final MeasureCommand standardCommand = new MeasureCommand(INDEX_FIRST_PERSON,
                Optional.of(new Height(VALID_HEIGHT_AMY)),
                Optional.of(new Weight(VALID_WEIGHT_AMY)),
                Optional.of(new BodyFatPercentage(VALID_BODY_FAT_AMY)));

        MeasureCommand sameValuesCommand = new MeasureCommand(INDEX_FIRST_PERSON,
                Optional.of(new Height(VALID_HEIGHT_AMY)),
                Optional.of(new Weight(VALID_WEIGHT_AMY)),
                Optional.of(new BodyFatPercentage(VALID_BODY_FAT_AMY)));
        assertTrue(standardCommand.equals(sameValuesCommand));

        assertTrue(standardCommand.equals(standardCommand));
        assertFalse(standardCommand.equals(null));
        assertFalse(standardCommand.equals(new ClearCommand()));

        MeasureCommand differentIndexCommand = new MeasureCommand(INDEX_SECOND_PERSON,
                Optional.of(new Height(VALID_HEIGHT_AMY)),
                Optional.of(new Weight(VALID_WEIGHT_AMY)),
                Optional.of(new BodyFatPercentage(VALID_BODY_FAT_AMY)));
        assertFalse(standardCommand.equals(differentIndexCommand));

        MeasureCommand differentMeasurementCommand = new MeasureCommand(INDEX_FIRST_PERSON,
                Optional.of(new Height("170.0")),
                Optional.of(new Weight(VALID_WEIGHT_AMY)),
                Optional.of(new BodyFatPercentage(VALID_BODY_FAT_AMY)));
        assertFalse(standardCommand.equals(differentMeasurementCommand));
    }
}

