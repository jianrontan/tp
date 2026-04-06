package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Consumer;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args,
                        PREFIX_NAME,
                        PREFIX_GENDER,
                        PREFIX_DOB,
                        PREFIX_PHONE,
                        PREFIX_EMAIL,
                        PREFIX_ADDRESS,
                        PREFIX_LOCATION,
                        PREFIX_TAG);
        Index index = parseIndex(argMultimap);

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_NAME,
                PREFIX_GENDER,
                PREFIX_DOB,
                PREFIX_PHONE,
                PREFIX_EMAIL,
                PREFIX_ADDRESS,
                PREFIX_LOCATION);

        EditPersonDescriptor editPersonDescriptor = parseDescriptor(argMultimap);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    private Index parseIndex(ArgumentMultimap argMultimap) throws ParseException {
        try {
            return ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }
    }

    private EditPersonDescriptor parseDescriptor(ArgumentMultimap argMultimap) throws ParseException {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        StringJoiner validationErrors = new StringJoiner("\n");

        Optional<String> nameInput = argMultimap.getValue(PREFIX_NAME);
        Optional<String> genderInput = argMultimap.getValue(PREFIX_GENDER);
        Optional<String> dobInput = argMultimap.getValue(PREFIX_DOB);
        Optional<String> phoneInput = argMultimap.getValue(PREFIX_PHONE);
        Optional<String> emailInput = argMultimap.getValue(PREFIX_EMAIL);
        Optional<String> addressInput = argMultimap.getValue(PREFIX_ADDRESS);
        Optional<String> locationInput = argMultimap.getValue(PREFIX_LOCATION);

        // Parse each optional prefixed value and collect all validation failures in one pass.
        parseOptionalField(nameInput, ParserUtil::parseName, editPersonDescriptor::setName, validationErrors);
        parseOptionalField(genderInput, ParserUtil::parseGender, editPersonDescriptor::setGender, validationErrors);
        parseOptionalField(dobInput, ParserUtil::parseDateOfBirth,
                editPersonDescriptor::setDateOfBirth, validationErrors);
        parseOptionalField(phoneInput, ParserUtil::parsePhone, editPersonDescriptor::setPhone, validationErrors);
        parseOptionalField(emailInput, ParserUtil::parseEmail, editPersonDescriptor::setEmail, validationErrors);
        parseOptionalField(addressInput, ParserUtil::parseAddress,
                editPersonDescriptor::setAddress, validationErrors);
        parseOptionalField(locationInput, ParserUtil::parseLocation,
                editPersonDescriptor::setLocation, validationErrors);

        // Tags have special semantics (e.g., a lone empty value means "clear tags"), so keep this path explicit.
        parseTagsIntoDescriptor(argMultimap, editPersonDescriptor, validationErrors);

        // Fail once after all parsing attempts so users get the full list of input issues in one message.
        throwIfAnyValidationErrors(validationErrors);

        return editPersonDescriptor;
    }

    /** Parses tag inputs and appends tag-related validation errors without short-circuiting. */
    private void parseTagsIntoDescriptor(ArgumentMultimap argMultimap,
                                         EditPersonDescriptor editPersonDescriptor,
                                         StringJoiner validationErrors) {
        requireNonNull(argMultimap);
        requireNonNull(editPersonDescriptor);
        requireNonNull(validationErrors);
        try {
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);
        } catch (ParseException pe) {
            validationErrors.add(pe.getMessage());
        }
    }

    /** Throws a single ParseException containing all collected validation messages. */
    private void throwIfAnyValidationErrors(StringJoiner validationErrors) throws ParseException {
        requireNonNull(validationErrors);
        if (validationErrors.length() > 0) {
            throw new ParseException(validationErrors.toString());
        }
    }

    /**
     * Parses one optional field and applies it to the descriptor if present.
     * Any parse failure is appended to {@code validationErrors} so callers can surface all errors at once.
     */
    private <T> void parseOptionalField(Optional<String> rawInput,
                                        FieldParser<T> parser,
                                        Consumer<T> setter,
                                        StringJoiner validationErrors) {
        requireNonNull(rawInput);
        requireNonNull(parser);
        requireNonNull(setter);
        requireNonNull(validationErrors);
        if (!rawInput.isPresent()) {
            return;
        }

        try {
            setter.accept(parser.parse(rawInput.get()));
        } catch (ParseException pe) {
            validationErrors.add(pe.getMessage());
        }
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        requireNonNull(tags);

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

    @FunctionalInterface
    private interface FieldParser<T> {
        T parse(String rawInput) throws ParseException;
    }

}
