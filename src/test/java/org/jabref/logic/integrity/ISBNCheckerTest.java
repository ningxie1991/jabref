package org.jabref.logic.integrity;

import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.text.html.Option;

import org.jabref.logic.l10n.Localization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ISBNCheckerTest {

    private final ISBNChecker checker = new ISBNChecker();

    @ParameterizedTest
    @MethodSource("provideISBN")
    public void checkISBNValue(Optional optValue, String id) {
        assertEquals(optValue, checker.checkValue(id));
    }

    private static Stream<Arguments> provideISBN() {
        return Stream.of(
                // ISBN-13
                Arguments.of(Optional.empty(), "978-0-306-40615-7"),
                Arguments.of(Optional.of(Localization.lang("incorrect control digit")), "978-0-306-40615-2"),
                Arguments.of(Optional.of(Localization.lang("incorrect format")), "978_0_306_40615_7"),
                // ISBN-10
                Arguments.of(Optional.empty(), "0-201-53082-1"),
                Arguments.of(Optional.empty(), "0-9752298-0-X"),
                Arguments.of(Optional.of(Localization.lang("incorrect format")), "Some other stuff"),
                Arguments.of(Optional.of(Localization.lang("incorrect control digit")), "0-201-53082-2")
        );
    }
}
