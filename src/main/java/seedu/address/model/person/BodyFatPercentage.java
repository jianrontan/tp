package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a person's body fat percentage.
 * Guarantees: immutable; is valid as declared in {@link #isValidBodyFatPercentage(String)}
 */
public class BodyFatPercentage {

    public static final String MESSAGE_CONSTRAINTS =
            "Body fat percentage should be a number between 1.0 and 75.0, with up to 1 decimal place.";
    public static final String DEFAULT_BODY_FAT_TEXT = "Body fat percentage not set";
    private static final String VALIDATION_REGEX = "\\d+(?:\\.\\d)?";

    public final String value;

    /**
     * Constructs a {@code BodyFatPercentage}.
     */
    public BodyFatPercentage(String bodyFatPercentage) {
        requireNonNull(bodyFatPercentage);
        String normalizedBodyFat = normalize(bodyFatPercentage);
        if (DEFAULT_BODY_FAT_TEXT.equalsIgnoreCase(normalizedBodyFat)) {
            this.value = DEFAULT_BODY_FAT_TEXT;
            return;
        }

        checkArgument(isValidBodyFatPercentage(normalizedBodyFat), MESSAGE_CONSTRAINTS);
        this.value = normalizedBodyFat;
    }

    /**
     * Returns true if a given string is a valid body fat percentage value.
     */
    public static boolean isValidBodyFatPercentage(String test) {
        if (test == null) {
            return false;
        }

        String normalizedTest = normalize(test);
        if (DEFAULT_BODY_FAT_TEXT.equalsIgnoreCase(normalizedTest)) {
            return true;
        }

        if (!normalizedTest.matches(VALIDATION_REGEX)) {
            return false;
        }

        double parsedValue = Double.parseDouble(normalizedTest);
        return parsedValue >= 1.0 && parsedValue <= 75.0;
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

        if (!(other instanceof BodyFatPercentage)) {
            return false;
        }

        BodyFatPercentage otherBodyFatPercentage = (BodyFatPercentage) other;
        return value.equals(otherBodyFatPercentage.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}


