package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PlanTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Plan(null));
    }

    @Test
    public void constructor_invalidPlan_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Plan("Bench Press"));
    }

    @Test
    public void constructor_validPlanParsesFlexibleSeparators_success() {
        assertEquals(new Plan("FULL BODY"), new Plan("FULL-BODY"));
        assertEquals(new Plan("FULL BODY"), new Plan("FULL_BODY"));
    }

    @Test
    public void constructor_validPlanParsesRepeatedSeparators_success() {
        //  Multiple consecutive separators should be treated as a single separator
        assertEquals(new Plan("FULL BODY"), new Plan("FULL__BODY"));
        assertEquals(new Plan("FULL BODY"), new Plan("FULL__---___BODY"));
        assertEquals(new Plan("FULL BODY"), new Plan("  FULL    BODY  "));
    }

    @Test
    public void toString_multiWordPlan_displaysWithSpace() {
        //  Separators should be normalized to a single space in the string representation
        assertEquals("FULL BODY", new Plan("FULL_BODY").toString());
    }

    @Test
    public void isValidPlan() {
        // Invalid plans
        assertFalse(Plan.isValidPlan(null));
        assertFalse(Plan.isValidPlan(""));
        assertFalse(Plan.isValidPlan("Bench Press"));

        // Valid plans
        assertTrue(Plan.isValidPlan("PUSH"));
        assertTrue(Plan.isValidPlan("full body"));
        assertTrue(Plan.isValidPlan("FULL-BODY"));
        assertTrue(Plan.isValidPlan("FULL_BODY"));
        assertTrue(Plan.isValidPlan("FULL__BODY"));
        assertTrue(Plan.isValidPlan("FULL__---___BODY"));
        assertTrue(Plan.isValidPlan(Plan.DEFAULT_PLAN_TEXT));
    }
}


