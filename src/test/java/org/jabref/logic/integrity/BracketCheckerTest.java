package org.jabref.logic.integrity;

import java.util.Optional;
import java.util.stream.Stream;

import org.jabref.logic.l10n.Localization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BracketCheckerTest {

    private final BracketChecker checker = new BracketChecker();

    @ParameterizedTest
    @MethodSource("provideInput")
    public void checkBrackets(Optional optValue, String providedInput) {
        assertEquals(optValue, checker.checkValue(providedInput));
    }

    private static Stream<Arguments> provideInput() {
        return Stream.of(
                // valid (bracketed) String
                Arguments.of(Optional.empty(), "x"),
                Arguments.of(Optional.empty(), "{x}"),
                Arguments.of(Optional.empty(), "{x}x{}x{{}}"),
                Arguments.of(Optional.empty(), "test{x}"),
                Arguments.of(Optional.empty(), "{x}test"),
                Arguments.of(Optional.empty(), "test{x}test"),
                Arguments.of(Optional.empty(), ""),
                // invalid string
                Arguments.of(Optional.of(Localization.lang("unexpected closing curly bracket")), "{x}x{}}x{{}}"),
                Arguments.of(Optional.of(Localization.lang("unexpected closing curly bracket")), "}"),
                Arguments.of(Optional.of(Localization.lang("unexpected opening curly bracket")), "{")
        );
    }
}
