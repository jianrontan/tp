package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;

import java.util.Arrays;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.LocationContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new NoteCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the NoteCommand and returns an
     * NoteCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_LOCATION);

        if (!argMultimap.getValue(PREFIX_LOCATION).isPresent()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        String filterContent = argMultimap.getValue(PREFIX_LOCATION).get();
        String[] locationKeywords = filterContent.split("\\s+");

        return new FilterCommand(new LocationContainsKeywordsPredicate(Arrays.asList(locationKeywords)));
    }
}
