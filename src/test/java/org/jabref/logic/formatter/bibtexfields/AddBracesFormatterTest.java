package org.jabref.logic.formatter.bibtexfields;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests in addition to the general tests from {@link org.jabref.logic.formatter.FormatterTest}
 */
class AddBracesFormatterTest {

    private AddBracesFormatter formatter;

    @BeforeEach
    public void setUp() {
        formatter = new AddBracesFormatter();
    }

    @ParameterizedTest(name = "input={0}, formattedStr={1}")
    @CsvSource(value = {
            "test, {test}", // formatAddsSingleEnclosingBraces
            "{test, {test", // formatKeepsUnmatchedBracesAtBeginning
            "test}, test}", // formatKeepsUnmatchedBracesAtEnd
            "t, t", // formatKeepsShortString
            "'', ''", // formatKeepsEmptyString
            "{{test}}, {{test}}", // formatKeepsDoubleEnclosingBraces
            "{{{test}}}, {{{test}}}", // formatKeepsTripleEnclosingBraces
            "{A} and {B}, {A} and {B}", // formatKeepsNonMatchingBraces
            "{{A} and {B}}, {{A} and {B}}", // formatKeepsOnlyMatchingBraces
            "{A} and {B}}, {A} and {B}}", // formatDoesNotRemoveBracesInBrokenString, We opt here for a conservative approach although one could argue that "A} and {B}" is also a valid return
            "AB, {AB}" // formatStringWithMinimalRequiredLength
    })
    public void testInputs(String input, String expectedResult) {
        String formattedStr = formatter.format(input);
        assertEquals(expectedResult, formattedStr);
    }

    @Test
    public void formatExample() {
        assertEquals("{In CDMA}", formatter.format(formatter.getExampleInput()));
    }
}
