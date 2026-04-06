package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BODY_FAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HEIGHT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEIGHT;

import java.util.Optional;
import java.util.StringJoiner;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MeasureCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.BodyFatPercentage;
import seedu.address.model.person.Height;
import seedu.address.model.person.Weight;

/**
 * Parses input arguments and creates a new MeasureCommand object.
 */
public class MeasureCommandParser implements Parser<MeasureCommand> {

    @Override
    public MeasureCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_HEIGHT, PREFIX_WEIGHT, PREFIX_BODY_FAT);
        Index index = parseIndex(argMultimap);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_HEIGHT, PREFIX_WEIGHT, PREFIX_BODY_FAT);

        Optional<String> heightInput = argMultimap.getValue(PREFIX_HEIGHT);
        Optional<String> weightInput = argMultimap.getValue(PREFIX_WEIGHT);
        Optional<String> bodyFatInput = argMultimap.getValue(PREFIX_BODY_FAT);

        if (!hasAnyMeasurementPrefix(heightInput, weightInput, bodyFatInput)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MeasureCommand.MESSAGE_USAGE));
        }

        StringJoiner validationErrors = new StringJoiner("\n");

        // Parse each optional prefixed value and collect all validation failures in one pass.
        Height height = parseOptionalValue(heightInput, ParserUtil::parseHeight, validationErrors);
        Weight weight = parseOptionalValue(weightInput, ParserUtil::parseWeight, validationErrors);
        BodyFatPercentage bodyFatPercentage =
                parseOptionalValue(bodyFatInput, ParserUtil::parseBodyFatPercentage, validationErrors);

        throwIfAnyValidationErrors(validationErrors);

        assert hasAnyParsedMeasurementValue(height, weight, bodyFatPercentage)
                : "Invariant broken: successful parse must produce at least one measurement.";

        return new MeasureCommand(index, height, weight, bodyFatPercentage);
    }

    private void throwIfAnyValidationErrors(StringJoiner validationErrors) throws ParseException {
        requireNonNull(validationErrors);
        if (validationErrors.length() > 0) {
            throw new ParseException(validationErrors.toString());
        }
    }

    /** Parses one optional field and returns null when absent or invalid, while collecting parse errors. */
    private <T> T parseOptionalValue(Optional<String> rawInput,
                                     FieldParser<T> parser,
                                     StringJoiner validationErrors) {
        requireNonNull(rawInput);
        requireNonNull(parser);
        requireNonNull(validationErrors);
        if (!rawInput.isPresent()) {
            return null;
        }

        try {
            return parser.parse(rawInput.get());
        } catch (ParseException pe) {
            validationErrors.add(pe.getMessage());
            return null;
        }
    }

    private Index parseIndex(ArgumentMultimap argMultimap) throws ParseException {
        try {
            return ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MeasureCommand.MESSAGE_USAGE), pe);
        }
    }

    private boolean hasAnyMeasurementPrefix(Optional<String> heightInput,
                                            Optional<String> weightInput,
                                            Optional<String> bodyFatInput) {
        return heightInput.isPresent() || weightInput.isPresent() || bodyFatInput.isPresent();
    }

    private boolean hasAnyParsedMeasurementValue(Height height, Weight weight,
                                                 BodyFatPercentage bodyFatPercentage) {
        return height != null || weight != null || bodyFatPercentage != null;
    }

    @FunctionalInterface
    private interface FieldParser<T> {
        T parse(String rawInput) throws ParseException;
    }

}
