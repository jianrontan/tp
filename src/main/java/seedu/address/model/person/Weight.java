package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a person's body weight in kilograms.
 * Guarantees: immutable; is valid as declared in {@link #isValidWeight(String)}
 */
public class Weight {

    public static final String MESSAGE_CONSTRAINTS =
            "Weight should be a number in kg between 20.0 and 500.0, with up to 1 decimal place "
                    + "(e.g. 72, 72.5, 72.).";
    public static final String DEFAULT_WEIGHT_TEXT = "";
    // Accepts: 72, 72.5, 72.
    // Rejects: 72.55, -1, 19.9, 500.1, 70kg
    public static final String VALIDATION_REGEX = "\\d+(?:\\.\\d?)?";

    public final String value;

    /**
     * Constructs a {@code Weight}.
     */
    public Weight(String weight) {
        requireNonNull(weight);
        checkArgument(isValidWeight(weight), MESSAGE_CONSTRAINTS);
        value = DEFAULT_WEIGHT_TEXT.equals(weight) ? DEFAULT_WEIGHT_TEXT : normaliseWeight(weight);
    }

    /**
     * Returns true if a given string is a valid weight value.
     */
    public static boolean isValidWeight(String test) {
        requireNonNull(test);
        if (DEFAULT_WEIGHT_TEXT.equals(test)) {
            return true;
        }

        if (!test.matches(VALIDATION_REGEX)) {
            return false;
        }

        double parsedValue = Double.parseDouble(test);
        return parsedValue >= 20.0 && parsedValue <= 500.0;
    }

    private static String normaliseWeight(String weight) {
        return new BigDecimal(weight).setScale(1, RoundingMode.UNNECESSARY).toPlainString();
    }


    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Weight)) {
            return false;
        }

        Weight otherWeight = (Weight) other;
        return value.equals(otherWeight.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}


