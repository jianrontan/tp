package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLAN;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.PlanCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Plan;

/**
 * Parses input arguments and creates a new PlanCommand object.
 */
public class PlanCommandParser implements Parser<PlanCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the PlanCommand and returns a
     * PlanCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public PlanCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PLAN);
        Index index = parseIndex(argMultimap);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PLAN);

        Optional<String> planInput = argMultimap.getValue(PREFIX_PLAN);
        if (!planInput.isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PlanCommand.MESSAGE_USAGE));
        }

        Plan plan = parsePlan(planInput.get());
        assert plan != null : "Invariant broken: successful parse should always produce a plan.";
        return new PlanCommand(index, plan);
    }

    private Index parseIndex(ArgumentMultimap argMultimap) throws ParseException {
        try {
            return ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PlanCommand.MESSAGE_USAGE), pe);
        }
    }

    private Plan parsePlan(String rawPlan) throws ParseException {
        requireNonNull(rawPlan);
        if (rawPlan.trim().isEmpty()) {
            return Plan.getDefaultPlan();
        }
        return ParserUtil.parsePlanCategory(rawPlan);
    }
}

