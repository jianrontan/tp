package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a person's body weight in kilograms.
 * Guarantees: immutable; is valid as declared in {@link #isValidWeight(String)}
 */
public class Weight {

    public static final String MESSAGE_CONSTRAINTS =
            "Weight should be a number in kg between 20.0 and 500.0, with up to 1 decimal place.";
    public static final String DEFAULT_WEIGHT_TEXT = "Weight not set";
    private static final String VALIDATION_REGEX = "\\d+(?:\\.\\d)?";

    public final String value;

    /**
     * Constructs a {@code Weight}.
     */
    public Weight(String weight) {
        requireNonNull(weight);
        String normalizedWeight = normalize(weight);
        if (DEFAULT_WEIGHT_TEXT.equalsIgnoreCase(normalizedWeight)) {
            this.value = DEFAULT_WEIGHT_TEXT;
            return;
        }

        checkArgument(isValidWeight(normalizedWeight), MESSAGE_CONSTRAINTS);
        this.value = normalizedWeight;
    }

    /**
     * Returns true if a given string is a valid weight value.
     */
    public static boolean isValidWeight(String test) {
        if (test == null) {
            return false;
        }

        String normalizedTest = normalize(test);
        if (DEFAULT_WEIGHT_TEXT.equalsIgnoreCase(normalizedTest)) {
            return true;
        }

        if (!normalizedTest.matches(VALIDATION_REGEX)) {
            return false;
        }

        double parsedValue = Double.parseDouble(normalizedTest);
        return parsedValue >= 20.0 && parsedValue <= 500.0;
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


