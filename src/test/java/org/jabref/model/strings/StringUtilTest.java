package org.jabref.model.strings;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilTest {

    @Test
    void StringUtilClassIsSmall() throws Exception {
        Path path = Path.of("src", "main", "java", StringUtil.class.getName().replace('.', '/') + ".java");
        int lineCount = Files.readAllLines(path, StandardCharsets.UTF_8).size();

        assertTrue(lineCount <= 761, "StringUtil increased in size to " + lineCount + ". "
                + "We try to keep this class as small as possible. "
                + "Thus think twice if you add something to StringUtil.");
    }

    @ParameterizedTest(name = "expectedResult={0}, booleanValue={1}")
    @CsvSource({
            "0, FALSE",
            "1, TRUE"
    })
    void testBooleanToBinaryString(String expectedResult, boolean booleanValue) {
        assertEquals(expectedResult, StringUtil.booleanToBinaryString(booleanValue));
    }

    @ParameterizedTest(name = "expectedResult={0}, toQuote={1}, specials={2}, quoteChar={3}")
    @CsvSource({
            "'a::', 'a:', '', ':'", // simple
            "'a::', 'a:', , ':'", // null quotation
            "'', , ';', ':'", // null string
            "'a:::;', 'a:;', ';', ':'", // quotation character
            "'a::b:%c:;', 'a:b%c;', '%;', ':'" // more complicated
    })
    void testQuote(String expectedResult, String toQuote, String specials, char quoteChar) {
        assertEquals(expectedResult, StringUtil.quote(toQuote, specials, quoteChar));
    }

    @ParameterizedTest(name = "expectedResult={0}, toQuote={1}, specials={2}, quoteChar={3}")
    @CsvSource({
            "newline, '\r', newline", // Mac < v9
            "newline, '\r\n', newline", // Windows
            "newline, '\n', newline" // Unix
    })
    void testUnifyLineBreaks(String expectedResult, String s, String newline) {

        assertEquals(expectedResult, StringUtil.unifyLineBreaks(s, newline));
    }

    @ParameterizedTest(name = "expectedResult={0}, toQuote={1}, specials={2}, quoteChar={3}")
    @CsvSource({
            "aa.bib, aa, bib",
            ".login.bib, .login, bib",
            "a.bib, a.bib, bib",
            "a.bib, a.bib, BIB",
            "a.bib, a, bib",
            "a.bb, a.bb, bib",
            "'', , bib"
    })
    void testGetCorrectFileName(String expectedResult, String orgName, String defaultExtension) {
        assertEquals(expectedResult, StringUtil.getCorrectFileName(orgName, defaultExtension));
    }

    @ParameterizedTest(name = "expectedResult={0}, toQuote={1}")
    @CsvSource({
            "'&#33;', !",
            "'&#33;&#33;&#33;', !!!"
    })
    void testQuoteForHTML(String expectedResult, String toQuote) {
        assertEquals(expectedResult, StringUtil.quoteForHTML(toQuote));
    }

    @ParameterizedTest(name = "expectedResult={0}, s={1}")
    @CsvSource({
            "ABC, {ABC}",
            "ABC, {{ABC}}",
            "{abc}, {abc}",
            "ABCDEF, {ABC}{DEF}"
    })
    void testRemoveBracesAroundCapitals(String expectedResult, String s) {
        assertEquals(expectedResult, StringUtil.removeBracesAroundCapitals(s));
    }

    @ParameterizedTest(name = "expectedResult={0}, s={1}")
    @CsvSource({
            "{ABC}, ABC",
            "{ABC}, {ABC}",
            "abc, abc",
            "'#ABC#', '#ABC#'",
            "'{ABC} def {EFG}', 'ABC def EFG'"
    })
    void testPutBracesAroundCapitals(String expectedResult, String s) {
        assertEquals(expectedResult, StringUtil.putBracesAroundCapitals(s));
    }

    @ParameterizedTest(name = "expectedResult={0}, toShave={1}")
    @CsvSource({
            "'', ",
            "'', ''",
            "aaa, '   aaa\t\t\n\r'",
            "a, '  \"a\"    '",
            "a, '  {a}    '",
            "{a}, '  {{a}}    '",
            "{a}, '  \"{a}\"    '",
            "\"{a\"}, '  \"{a\"}    '",
    })
    void testShaveString(String expectedResult, String toShave) {
        assertEquals(expectedResult, StringUtil.shaveString(toShave));
    }

    @Test
    void testJoin() {
        String[] s = {"ab", "cd", "ed"};
        assertEquals("ab\\cd\\ed", StringUtil.join(s, "\\", 0, s.length));

        assertEquals("cd\\ed", StringUtil.join(s, "\\", 1, s.length));

        assertEquals("ed", StringUtil.join(s, "\\", 2, s.length));

        assertEquals("", StringUtil.join(s, "\\", 3, s.length));

        assertEquals("", StringUtil.join(new String[]{}, "\\", 0, 0));
    }

    @Test
    void testStripBrackets() {
        assertEquals("foo", StringUtil.stripBrackets("[foo]"));
        assertEquals("[foo]", StringUtil.stripBrackets("[[foo]]"));
        assertEquals("", StringUtil.stripBrackets(""));
        assertEquals("[foo", StringUtil.stripBrackets("[foo"));
        assertEquals("]", StringUtil.stripBrackets("]"));
        assertEquals("", StringUtil.stripBrackets("[]"));
        assertEquals("f[]f", StringUtil.stripBrackets("f[]f"));
        assertEquals(null, StringUtil.stripBrackets(null));
    }

    @Test
    void testGetPart() {
        // Should be added
    }

    @Test
    void testFindEncodingsForString() {
        // Unused in JabRef, but should be added in case it finds some use
    }

    @Test
    void testWrap() {
        String newline = "newline";
        assertEquals("aaaaa" + newline + "\tbbbbb" + newline + "\tccccc",
                StringUtil.wrap("aaaaa bbbbb ccccc", 5, newline));
        assertEquals("aaaaa bbbbb" + newline + "\tccccc", StringUtil.wrap("aaaaa bbbbb ccccc", 8, newline));
        assertEquals("aaaaa bbbbb" + newline + "\tccccc", StringUtil.wrap("aaaaa bbbbb ccccc", 11, newline));
        assertEquals("aaaaa bbbbb ccccc", StringUtil.wrap("aaaaa bbbbb ccccc", 12, newline));
        assertEquals("aaaaa" + newline + "\t" + newline + "\tbbbbb" + newline + "\t" + newline + "\tccccc",
                StringUtil.wrap("aaaaa\nbbbbb\nccccc", 12, newline));
        assertEquals(
                "aaaaa" + newline + "\t" + newline + "\t" + newline + "\tbbbbb" + newline + "\t" + newline + "\tccccc",
                StringUtil.wrap("aaaaa\n\nbbbbb\nccccc", 12, newline));
        assertEquals("aaaaa" + newline + "\t" + newline + "\tbbbbb" + newline + "\t" + newline + "\tccccc",
                StringUtil.wrap("aaaaa\r\nbbbbb\r\nccccc", 12, newline));
    }

    @Test
    void testDecodeStringDoubleArray() {
        assertArrayEquals(new String[][]{{"a", "b"}, {"c", "d"}}, StringUtil.decodeStringDoubleArray("a:b;c:d"));
        assertArrayEquals(new String[][]{{"a", ""}, {"c", "d"}}, StringUtil.decodeStringDoubleArray("a:;c:d"));
        // arrays first differed at element [0][1]; expected: null<null> but was: java.lang.String<null>
        // assertArrayEquals(stringArray2res, StringUtil.decodeStringDoubleArray(encStringArray2));
        assertArrayEquals(new String[][]{{"a", ":b"}, {"c;", "d"}}, StringUtil.decodeStringDoubleArray("a:\\:b;c\\;:d"));
    }

    @Test
    void testIsInCurlyBrackets() {
        assertFalse(StringUtil.isInCurlyBrackets(""));
        assertFalse(StringUtil.isInCurlyBrackets(null));
        assertTrue(StringUtil.isInCurlyBrackets("{}"));
        assertTrue(StringUtil.isInCurlyBrackets("{a}"));
        assertTrue(StringUtil.isInCurlyBrackets("{a{a}}"));
        assertTrue(StringUtil.isInCurlyBrackets("{{\\AA}sa {\\AA}Stor{\\aa}}"));
        assertFalse(StringUtil.isInCurlyBrackets("{"));
        assertFalse(StringUtil.isInCurlyBrackets("}"));
        assertFalse(StringUtil.isInCurlyBrackets("a{}a"));
        assertFalse(StringUtil.isInCurlyBrackets("{\\AA}sa {\\AA}Stor{\\aa}"));
    }

    @Test
    void testIsInSquareBrackets() {
        assertFalse(StringUtil.isInSquareBrackets(""));
        assertFalse(StringUtil.isInSquareBrackets(null));
        assertTrue(StringUtil.isInSquareBrackets("[]"));
        assertTrue(StringUtil.isInSquareBrackets("[a]"));
        assertFalse(StringUtil.isInSquareBrackets("["));
        assertFalse(StringUtil.isInSquareBrackets("]"));
        assertFalse(StringUtil.isInSquareBrackets("a[]a"));
    }

    @Test
    void testIsInCitationMarks() {
        assertFalse(StringUtil.isInCitationMarks(""));
        assertFalse(StringUtil.isInCitationMarks(null));
        assertTrue(StringUtil.isInCitationMarks("\"\""));
        assertTrue(StringUtil.isInCitationMarks("\"a\""));
        assertFalse(StringUtil.isInCitationMarks("\""));
        assertFalse(StringUtil.isInCitationMarks("a\"\"a"));
    }

    @Test
    void testIntValueOfSingleDigit() {
        assertEquals(1, StringUtil.intValueOf("1"));
        assertEquals(2, StringUtil.intValueOf("2"));
        assertEquals(8, StringUtil.intValueOf("8"));
    }

    @Test
    void testIntValueOfLongString() {
        assertEquals(1234567890, StringUtil.intValueOf("1234567890"));
    }

    @Test
    void testIntValueOfStartWithZeros() {
        assertEquals(1234, StringUtil.intValueOf("001234"));
    }

    @Test
    void testIntValueOfExceptionIfStringContainsLetter() {
        assertThrows(NumberFormatException.class, () -> StringUtil.intValueOf("12A2"));
    }

    @Test
    void testIntValueOfExceptionIfStringNull() {
        assertThrows(NumberFormatException.class, () -> StringUtil.intValueOf(null));
    }

    @Test
    void testIntValueOfExceptionfIfStringEmpty() {
        assertThrows(NumberFormatException.class, () -> StringUtil.intValueOf(""));
    }

    @Test
    void testIntValueOfWithNullSingleDigit() {
        assertEquals(Optional.of(1), StringUtil.intValueOfOptional("1"));
        assertEquals(Optional.of(2), StringUtil.intValueOfOptional("2"));
        assertEquals(Optional.of(8), StringUtil.intValueOfOptional("8"));
    }

    @Test
    void testIntValueOfWithNullLongString() {
        assertEquals(Optional.of(1234567890), StringUtil.intValueOfOptional("1234567890"));
    }

    @Test
    void testIntValueOfWithNullStartWithZeros() {
        assertEquals(Optional.of(1234), StringUtil.intValueOfOptional("001234"));
    }

    @Test
    void testIntValueOfWithNullExceptionIfStringContainsLetter() {
        assertEquals(Optional.empty(), StringUtil.intValueOfOptional("12A2"));
    }

    @Test
    void testIntValueOfWithNullExceptionIfStringNull() {
        assertEquals(Optional.empty(), StringUtil.intValueOfOptional(null));
    }

    @Test
    void testIntValueOfWithNullExceptionfIfStringEmpty() {
        assertEquals(Optional.empty(), StringUtil.intValueOfOptional(""));
    }

    @Test
    void testLimitStringLengthShort() {
        assertEquals("Test", StringUtil.limitStringLength("Test", 20));
    }

    @Test
    void testLimitStringLengthLimiting() {
        assertEquals("TestTes...", StringUtil.limitStringLength("TestTestTestTestTest", 10));
        assertEquals(10, StringUtil.limitStringLength("TestTestTestTestTest", 10).length());
    }

    @Test
    void testLimitStringLengthNullInput() {
        assertEquals("", StringUtil.limitStringLength(null, 10));
    }

    @Test
    void testReplaceSpecialCharacters() {
        assertEquals("Hallo Arger", StringUtil.replaceSpecialCharacters("Hallo Arger"));
        assertEquals("aaAeoeeee", StringUtil.replaceSpecialCharacters("åÄöéèë"));
    }

    @Test
    void replaceSpecialCharactersWithNonNormalizedUnicode() {
        assertEquals("Modele", StringUtil.replaceSpecialCharacters("Modèle"));
    }

    @Test
    void testRepeatSpaces() {
        assertEquals("", StringUtil.repeatSpaces(0));
        assertEquals(" ", StringUtil.repeatSpaces(1));
        assertEquals("       ", StringUtil.repeatSpaces(7));
    }

    @Test
    void testRepeat() {
        assertEquals("", StringUtil.repeat(0, 'a'));
        assertEquals("a", StringUtil.repeat(1, 'a'));
        assertEquals("aaaaaaa", StringUtil.repeat(7, 'a'));
    }

    @Test
    void testBoldHTML() {
        assertEquals("<b>AA</b>", StringUtil.boldHTML("AA"));
    }

    @Test
    void testBoldHTMLReturnsOriginalTextIfNonNull() {
        assertEquals("<b>AA</b>", StringUtil.boldHTML("AA", "BB"));
    }

    @Test
    void testBoldHTMLReturnsAlternativeTextIfNull() {
        assertEquals("<b>BB</b>", StringUtil.boldHTML(null, "BB"));
    }

    @Test
    void testUnquote() {
        assertEquals("a:", StringUtil.unquote("a::", ':'));
        assertEquals("a:;", StringUtil.unquote("a:::;", ':'));
        assertEquals("a:b%c;", StringUtil.unquote("a::b:%c:;", ':'));
    }

    @Test
    void testCapitalizeFirst() {
        assertEquals("", StringUtil.capitalizeFirst(""));
        assertEquals("Hello world", StringUtil.capitalizeFirst("Hello World"));
        assertEquals("A", StringUtil.capitalizeFirst("a"));
        assertEquals("Aa", StringUtil.capitalizeFirst("AA"));
    }
}
