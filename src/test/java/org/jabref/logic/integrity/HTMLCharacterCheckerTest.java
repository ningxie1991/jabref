package org.jabref.logic.integrity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTMLCharacterCheckerTest {

    private final HTMLCharacterChecker checker = new HTMLCharacterChecker();
    private final BibEntry entry = new BibEntry();

    private static Stream<Arguments> getParameters() {
        return Stream.of(
                Arguments.of(Collections.emptyList(), new BibEntry().withField(StandardField.TITLE, "Not a single {HTML} character")),
                Arguments.of(Collections.emptyList(), new BibEntry().withField(StandardField.MONTH, "#jan#")),
                Arguments.of(Collections.emptyList(), new BibEntry().withField(StandardField.AUTHOR, "A. Einstein and I. Newton")),
                Arguments.of(Collections.emptyList(), new BibEntry().withField(StandardField.URL, "http://www.thinkmind.org/index.php?view=article&amp;articleid=cloud_computing_2013_1_20_20130")),
                Arguments.of(List.of(new IntegrityMessage("HTML encoded character found", new BibEntry().withField(StandardField.AUTHOR, "Lenhard, J&#227;rg"), StandardField.AUTHOR)),
                        new BibEntry().withField(StandardField.AUTHOR, "Lenhard, J&#227;rg")),
                Arguments.of(List.of(new IntegrityMessage("HTML encoded character found", new BibEntry().withField(StandardField.JOURNAL, "&Auml;rling Str&ouml;m for &#8211; &#x2031;"), StandardField.JOURNAL)),
                        new BibEntry().withField(StandardField.JOURNAL, "&Auml;rling Str&ouml;m for &#8211; &#x2031;"))
        );
    }

    @ParameterizedTest
    @MethodSource("getParameters")
    void test(List expectedResult, BibEntry entry) {
        assertEquals(expectedResult, checker.check(entry));
    }
}
