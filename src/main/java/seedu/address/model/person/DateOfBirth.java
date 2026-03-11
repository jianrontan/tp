package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a person's date of birth in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class DateOfBirth {
    public static final String MESSAGE_CONSTRAINTS =
            "Date of Birth must be in the format DD/MM/YYYY and cannot be in the future.";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public final LocalDate value;
    /**
     * Constructs a {@code DateOfBirth}.
     *
     * @param gender A valid date of birth string.
     */
    public DateOfBirth(String dob) {
        requireNonNull(dob);
        checkArgument(isValidDob(dob), MESSAGE_CONSTRAINTS);
        value = LocalDate.parse(dob, DateOfBirth.FORMATTER);
    }

    /**
     * Returns true if a given string is a valid date of birth string.
     */
    public static boolean isValidDob(String test) {
        boolean isValidDate = true;
        try {
            LocalDate testDate = LocalDate.parse(test, DateOfBirth.FORMATTER);
            if (testDate.isAfter(LocalDate.now())) {
                isValidDate = false;
            }
        } catch (DateTimeParseException e) {
            isValidDate = false;
        }
        return isValidDate;
    }

    @Override
    public String toString() {
        return value.format(DateOfBirth.FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DateOfBirth)) {
            return false;
        }

        DateOfBirth otherDob = (DateOfBirth) other;
        return value.equals(otherDob.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
