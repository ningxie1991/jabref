package org.jabref.logic.formatter.casechanger;

import java.util.Collections;

import org.jabref.logic.protectedterms.ProtectedTermsLoader;
import org.jabref.logic.protectedterms.ProtectedTermsPreferences;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests in addition to the general tests from {@link org.jabref.logic.formatter.FormatterTest}
 */
public class ProtectTermsFormatterTest {

    private ProtectTermsFormatter formatter;

    @BeforeEach
    public void setUp() {
        formatter = new ProtectTermsFormatter(
                new ProtectedTermsLoader(new ProtectedTermsPreferences(ProtectedTermsLoader.getInternalLists(),
                        Collections.emptyList(), Collections.emptyList(), Collections.emptyList())));
    }

    @ParameterizedTest(name = "input={0}, formattedStr={1}")
    @CsvSource(value = {
            "VLSI, {VLSI}", // testSingleWord
            "{VLSI}, {VLSI}", // testDoNotProtectAlreadyProtected
            "VLsI, VLsI", // testCaseSensitivity
            "3GPP 3G, {3GPP} {3G}", // testCorrectOrderingOfTerms
            "VLSI {VLSI}, {VLSI} {VLSI}",
            "{BPEL}, {BPEL}",
            "{Testing BPEL Engine Performance: A Survey}, {Testing BPEL Engine Performance: A Survey}"
    })
    public void testInputs(String input, String expectedResult) {
        String formattedStr = formatter.format(input);
        assertEquals(expectedResult, formattedStr);
    }

    @Test
    public void formatExample() {
        assertEquals("In {CDMA}", formatter.format(formatter.getExampleInput()));
    }
}
