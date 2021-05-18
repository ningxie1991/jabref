package org.jabref.logic.integrity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Stream;

import org.jabref.logic.l10n.Localization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

public class ISBNCheckerTest {

    private final ISBNChecker checker = new ISBNChecker();

    @Test
    void isbnAcceptsValidInput() {
        assertEquals(Optional.empty(), checker.checkValue("0-201-53082-1"));
    }

    @Test
    void isbnAcceptsNumbersAndCharacters() {
        assertEquals(Optional.empty(), checker.checkValue("0-9752298-0-X"));
    }

    @Test
    void isbnDoesNotAcceptRandomInput() {
        assertNotEquals(Optional.empty(), checker.checkValue("Some other stuff"));
    }

    @Test
    void isbnDoesNotAcceptInvalidInput() {
        assertNotEquals(Optional.empty(), checker.checkValue("0-201-53082-2"));
    }

    @Test
    void fuzzTest() throws IOException {
        int FUZZ_INPUTS = -1;

        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c","echo '0-000-00000-0' | /Users/nasdas/Developer/radamsa/radamsa/bin/radamsa -n " + FUZZ_INPUTS);
        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                assertTrue(checker.checkValue(line).isEmpty() ||
                        checker.checkValue(line).equals(Optional.of(Localization.lang("incorrect format"))) ||
                        checker.checkValue(line).equals(Optional.of(Localization.lang("incorrect control digit")))
                );
                System.out.println(
                        line
                );
            }
        }
    }

    @ParameterizedTest
    @MethodSource("provideBoundaryArgumentsForISBN13")
    public void checkISBNValue(Optional optValue, String id) {
        assertEquals(optValue, checker.checkValue(id));
    }

    private static Stream<Arguments> provideBoundaryArgumentsForISBN13() {
        return Stream.of(
                Arguments.of(Optional.empty(), "978-0-306-40615-7"),
                Arguments.of(Optional.of(Localization.lang("incorrect control digit")), "978-0-306-40615-2"),
                Arguments.of(Optional.of(Localization.lang("incorrect format")), "978_0_306_40615_7")
        );
    }
}
