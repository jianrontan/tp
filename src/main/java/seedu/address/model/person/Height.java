package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a person's height in centimeters.
 * Guarantees: immutable; is valid as declared in {@link #isValidHeight(String)}
 */
public class Height {

    public static final String MESSAGE_CONSTRAINTS =
            "Height should be a number in cm between 50.0 and 300.0, with up to 1 decimal place.";
    public static final String DEFAULT_HEIGHT_TEXT = "Height not set";
    private static final String VALIDATION_REGEX = "\\d+(?:\\.\\d)?";

    public final String value;

    /**
     * Constructs a {@code Height}.
     */
    public Height(String height) {
        requireNonNull(height);
        String normalizedHeight = normalize(height);
        if (DEFAULT_HEIGHT_TEXT.equalsIgnoreCase(normalizedHeight)) {
            this.value = DEFAULT_HEIGHT_TEXT;
            return;
        }

        checkArgument(isValidHeight(normalizedHeight), MESSAGE_CONSTRAINTS);
        this.value = normalizedHeight;
    }

    /**
     * Returns true if a given string is a valid height value.
     */
    public static boolean isValidHeight(String test) {
        if (test == null) {
            return false;
        }

        String normalizedTest = normalize(test);
        if (DEFAULT_HEIGHT_TEXT.equalsIgnoreCase(normalizedTest)) {
            return true;
        }

        if (!normalizedTest.matches(VALIDATION_REGEX)) {
            return false;
        }

        double parsedValue = Double.parseDouble(normalizedTest);
        return parsedValue >= 50.0 && parsedValue <= 300.0;
    }

    private static String normalize(String text) {
        return text.trim().replaceAll("\\s+", " ");
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

        if (!(other instanceof Height)) {
            return false;
        }

        Height otherHeight = (Height) other;
        return value.equals(otherHeight.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

