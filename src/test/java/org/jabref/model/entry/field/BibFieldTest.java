package org.jabref.model.entry.field;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BibFieldTest {

    @Test
    void bibFieldsConsideredEqualIfUnderlyingFieldIsEqual() {
        BibField fieldOne = new BibField(StandardField.AUTHOR, FieldPriority.IMPORTANT);
        BibField fieldTwo = new BibField(StandardField.AUTHOR, FieldPriority.DETAIL);
        assertTrue(fieldOne.equals(fieldTwo));
    }

    @Test
    void bibFieldsConsideredNotEqualIfUnderlyingFieldNotEqual() {
        BibField fieldOne = new BibField(StandardField.AUTHOR, FieldPriority.IMPORTANT);
        BibField fieldTwo = new BibField(StandardField.TITLE, FieldPriority.IMPORTANT);
        assertFalse(fieldOne.equals(fieldTwo));
    }
}
