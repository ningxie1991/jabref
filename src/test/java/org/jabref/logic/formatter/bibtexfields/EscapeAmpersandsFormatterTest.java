package org.jabref.logic.formatter.bibtexfields;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EscapeAmpersandsFormatterTest {

    private EscapeAmpersandsFormatter formatter;

    @BeforeEach
    void setUp() {
        formatter = new EscapeAmpersandsFormatter();
    }

    @ParameterizedTest(name = "input={0}, formattedStr={1}")
    @CsvSource(value = {
            "Lorem ipsum, Lorem ipsum", // formatReturnsSameTextIfNoAmpersandsPresent
            "Lorem&ipsum, Lorem\\&ipsum", // formatEscapesAmpersandsIfPresent
            "\\newcommand[1]{Lorem ipsum}, \\newcommand[1]{Lorem ipsum}", // formatReturnsSameTextInNewUserDefinedLatexCommandIfNoAmpersandsPresent
            "\\textbf{Lorem\\&ipsum}, \\textbf{Lorem\\&ipsum}", // formatReturnsSameTextInLatexCommandIfOneAmpersandPresent
    })
    public void testInputs(String input, String expectedResult) {
        String formattedStr = formatter.format(input);
        assertEquals(expectedResult, formattedStr);
    }

    @Test
    void formatExample() {
        assertEquals("Text \\& with \\&ampersands", formatter.format(formatter.getExampleInput()));
    }
}
