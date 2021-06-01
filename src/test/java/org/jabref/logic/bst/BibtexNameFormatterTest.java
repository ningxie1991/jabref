package org.jabref.logic.bst;

import org.jabref.model.entry.AuthorList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BibtexNameFormatterTest {

    @Test
    public void testUmlautsFullNames() {
        AuthorList al = AuthorList
                .parse("Charles Louis Xavier Joseph de la Vall{\\'e}e Poussin");

        assertEquals("de~laVall{\\'e}e~PoussinCharles Louis Xavier~Joseph", BibtexNameFormatter.formatName(al
                .getAuthor(0), "{vv}{ll}{jj}{ff}", Assertions::fail));
    }

    @Test
    public void testUmlautsAbbreviations() {
        AuthorList al = AuthorList
                .parse("Charles Louis Xavier Joseph de la Vall{\\'e}e Poussin");

        assertEquals("de~la Vall{\\'e}e~Poussin, C.~L. X.~J.", BibtexNameFormatter.formatName(al
                .getAuthor(0), "{vv~}{ll}{, jj}{, f.}", Assertions::fail));
    }

    @Test
    public void testUmlautsAbbreviationsWithQuestionMark() {
        AuthorList al = AuthorList
                .parse("Charles Louis Xavier Joseph de la Vall{\\'e}e Poussin");

        assertEquals("de~la Vall{\\'e}e~Poussin, C.~L. X.~J?", BibtexNameFormatter.formatName(al
                .getAuthor(0), "{vv~}{ll}{, jj}{, f}?", Assertions::fail));
    }

    private void assertNameFormat(String string, String string2, int which, String format) {
        assertEquals(string, BibtexNameFormatter.formatName(string2, which, format,
                Assertions::fail));
    }

    @ParameterizedTest(name = "string={0}, string2={1}")
    @CsvSource({
            "'Meyer, J?', 'Jonathan Meyer and Charles Louis Xavier Joseph de la Vall{\\'e}e Poussin'",
            "'Masterly, {\\'{E}}?', '{\\'{E}}douard Masterly'",
            "'{\\\"{U}}nderwood, U?', 'Ulrich {\\\"{U}}nderwood and Ned {\\~N}et and Paul {\\={P}}ot'",
            "'Victor, P.~{\\\\'E}?', 'Paul {\\'E}mile Victor and and de la Cierva y Codorn{\\’\\i}u, Juan'"
    })
    public void assertNameFormatA(String string, String string2) {
        assertNameFormat(string, string2, 1, "{vv~}{ll}{, jj}{, f}?");
    }

    @ParameterizedTest(name = "string={0}, string2={1}")
    @CsvSource({
            "'J.~Meyer', 'Jonathan Meyer and Charles Louis Xavier Joseph de la Vall{\\'e}e Poussin'",
            "'{\\'{E}}.~Masterly', '{\\'{E}}douard Masterly'",
            "'U.~{\\\"{U}}nderwood', 'Ulrich {\\\"{U}}nderwood and Ned {\\~N}et and Paul {\\={P}}ot'",
            "'P.~{\\'E}. Victor', 'Paul {\\'E}mile Victor and and de la Cierva y Codorn{\\’\\i}u, Juan'"
    })
    public void assertNameFormatB(String string, String string2) {
        assertNameFormat(string, string2, 1, "{f.~}{vv~}{ll}{, jj}");
    }

    @ParameterizedTest(name = "string={0}, string2={1}")
    @CsvSource({
            "'Jonathan Meyer', 'Jonathan Meyer and Charles Louis Xavier Joseph de la Vall{\\'e}e Poussin'",
            "'{\\'{E}}douard Masterly', '{\\'{E}}douard Masterly'",
            "'Ulrich {\\\"{U}}nderwood', 'Ulrich {\\\"{U}}nderwood and Ned {\\~N}et and Paul {\\={P}}ot'",
            "'Paul~{\\'E}mile Victor', 'Paul {\\'E}mile Victor and and de la Cierva y Codorn{\\’\\i}u, Juan'"
    })
    public void assertNameFormatC(String string, String string2) {
        assertNameFormat(string, string2, 1, "{ff }{vv }{ll}{ jj}");
    }

    @Test
    public void matchingBraceConsumedForCompleteWords() {
        StringBuilder sb = new StringBuilder();
        assertEquals(6, BibtexNameFormatter.consumeToMatchingBrace(sb, "{HELLO} {WORLD}"
                .toCharArray(), 0));
        assertEquals("{HELLO}", sb.toString());
    }

    @Test
    public void matchingBraceConsumedForBracesInWords() {
        StringBuilder sb = new StringBuilder();
        assertEquals(18, BibtexNameFormatter.consumeToMatchingBrace(sb, "{HE{L{}L}O} {WORLD}"
                .toCharArray(), 12));
        assertEquals("{WORLD}", sb.toString());
    }

    @Test
    public void testConsumeToMatchingBrace() {
        StringBuilder sb = new StringBuilder();
        assertEquals(10, BibtexNameFormatter.consumeToMatchingBrace(sb, "{HE{L{}L}O} {WORLD}"
                .toCharArray(), 0));
        assertEquals("{HE{L{}L}O}", sb.toString());
    }

    @Test
    public void testGetFirstCharOfString() {
        assertEquals("C", BibtexNameFormatter.getFirstCharOfString("Charles"));
        assertEquals("V", BibtexNameFormatter.getFirstCharOfString("Vall{\\'e}e"));
        assertEquals("{\\'e}", BibtexNameFormatter.getFirstCharOfString("{\\'e}"));
        assertEquals("{\\'e", BibtexNameFormatter.getFirstCharOfString("{\\'e"));
        assertEquals("E", BibtexNameFormatter.getFirstCharOfString("{E"));
    }

    @Test
    public void testNumberOfChars() {
        assertEquals(6, BibtexNameFormatter.numberOfChars("Vall{\\'e}e", -1));
        assertEquals(2, BibtexNameFormatter.numberOfChars("Vall{\\'e}e", 2));
        assertEquals(1, BibtexNameFormatter.numberOfChars("Vall{\\'e}e", 1));
        assertEquals(6, BibtexNameFormatter.numberOfChars("Vall{\\'e}e", 6));
        assertEquals(6, BibtexNameFormatter.numberOfChars("Vall{\\'e}e", 7));
        assertEquals(8, BibtexNameFormatter.numberOfChars("Vall{e}e", -1));
        assertEquals(6, BibtexNameFormatter.numberOfChars("Vall{\\'e this will be skipped}e", -1));
    }
}
