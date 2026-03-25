package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BODY_FAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HEIGHT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEIGHT;

import java.util.Optional;

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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_HEIGHT, PREFIX_WEIGHT, PREFIX_BODY_FAT);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MeasureCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_HEIGHT, PREFIX_WEIGHT, PREFIX_BODY_FAT);

        Optional<Height> height = Optional.empty();
        Optional<Weight> weight = Optional.empty();
        Optional<BodyFatPercentage> bodyFatPercentage = Optional.empty();

        if (argMultimap.getValue(PREFIX_HEIGHT).isPresent()) {
            height = Optional.of(ParserUtil.parseHeight(argMultimap.getValue(PREFIX_HEIGHT).get()));
        }
        if (argMultimap.getValue(PREFIX_WEIGHT).isPresent()) {
            weight = Optional.of(ParserUtil.parseWeight(argMultimap.getValue(PREFIX_WEIGHT).get()));
        }
        if (argMultimap.getValue(PREFIX_BODY_FAT).isPresent()) {
            bodyFatPercentage = Optional.of(
                    ParserUtil.parseBodyFatPercentage(argMultimap.getValue(PREFIX_BODY_FAT).get()));
        }

        if (!height.isPresent() && !weight.isPresent() && !bodyFatPercentage.isPresent()) {
            throw new ParseException(MeasureCommand.MESSAGE_NOT_EDITED);
        }

        return new MeasureCommand(index, height, weight, bodyFatPercentage);
    }
}

