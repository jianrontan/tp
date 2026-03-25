package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes;
import static seedu.address.logic.commands.CommandTestUtil.BODY_FAT_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.HEIGHT_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.WEIGHT_DESC_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BODY_FAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HEIGHT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEIGHT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.MeasureCommand;
import seedu.address.model.person.BodyFatPercentage;
import seedu.address.model.person.Height;
import seedu.address.model.person.Weight;

public class MeasureCommandParserTest {

    private final MeasureCommandParser parser = new MeasureCommandParser();

    @Test
    public void parse_missingIndex_failure() {
        assertParseFailure(parser, HEIGHT_DESC_AMY,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MeasureCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noMeasurementPrefix_failure() {
        assertParseFailure(parser, "1", MeasureCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidHeight_failure() {
        assertParseFailure(parser, "1 " + PREFIX_HEIGHT + "49.9", Height.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidWeight_failure() {
        assertParseFailure(parser, "1 " + PREFIX_WEIGHT + "10.0", Weight.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidBodyFat_failure() {
        assertParseFailure(parser, "1 " + PREFIX_BODY_FAT + "90.0", BodyFatPercentage.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicatePrefix_failure() {
        assertParseFailure(parser, "1 " + HEIGHT_DESC_AMY + " " + PREFIX_HEIGHT + "170.0",
                getErrorMessageForDuplicatePrefixes(PREFIX_HEIGHT));
    }

    @Test
    public void parse_validSinglePrefix_success() {
        MeasureCommand expectedCommand = new MeasureCommand(INDEX_FIRST_PERSON,
                Optional.of(new Height("165.5")),
                Optional.empty(),
                Optional.empty());

        assertParseSuccess(parser, "1" + HEIGHT_DESC_AMY, expectedCommand);
    }

    @Test
    public void parse_validMultiplePrefixes_success() {
        MeasureCommand expectedCommand = new MeasureCommand(INDEX_FIRST_PERSON,
                Optional.of(new Height("165.5")),
                Optional.of(new Weight("58.0")),
                Optional.of(new BodyFatPercentage("22.5")));

        assertParseSuccess(parser, "1" + HEIGHT_DESC_AMY + WEIGHT_DESC_AMY + BODY_FAT_DESC_AMY, expectedCommand);
    }
}

