package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a person's height in centimeters.
 * Guarantees: immutable; is valid as declared in {@link #isValidHeight(String)}
 */
public class Height {

    public static final String MESSAGE_CONSTRAINTS =
            "Height should be a number in cm between 50.0 and 300.0, with up to 1 decimal place "
                    + "(e.g. 170, 170.5, 170.).";
    public static final String DEFAULT_HEIGHT_TEXT = "";
    // Accepts: 170, 170.5, 170.
    // Rejects: 170.55, -1, 49.9, 300.1, 170cm
    public static final String VALIDATION_REGEX = "\\d+(?:\\.\\d?)?";

    public final String value;

    /**
     * Constructs a {@code Height}.
     */
    public Height(String height) {
        requireNonNull(height);
        checkArgument(isValidHeight(height), MESSAGE_CONSTRAINTS);
        value = DEFAULT_HEIGHT_TEXT.equals(height) ? DEFAULT_HEIGHT_TEXT : normaliseHeight(height);
    }

    /**
     * Returns true if a given string is a valid height value.
     */
    public static boolean isValidHeight(String test) {
        requireNonNull(test);
        if (DEFAULT_HEIGHT_TEXT.equals(test)) {
            return true;
        }

        if (!test.matches(VALIDATION_REGEX)) {
            return false;
        }

        double parsedValue = Double.parseDouble(test);
        return parsedValue >= 50.0 && parsedValue <= 300.0;
    }

    private static String normaliseHeight(String height) {
        return new BigDecimal(height).setScale(1, RoundingMode.UNNECESSARY).toPlainString();
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

        if (!(other instanceof Height otherHeight)) {
            return false;
        }
        return value.equals(otherHeight.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

