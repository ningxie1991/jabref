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

public class ISSNCheckerTest {

    private final ISSNChecker checker = new ISSNChecker();

    @ParameterizedTest
    @MethodSource("provideISSN")
    public void checkISSN(Optional optValue, String providedISSN) {
        assertEquals(optValue, checker.checkValue(providedISSN));
    }

    private static Stream<Arguments> provideISSN() {
        return Stream.of(
                // valid ISSN
                Arguments.of(Optional.empty(), "0020-7217"),
                Arguments.of(Optional.empty(), "2434-561x"),
                Arguments.of(Optional.empty(), ""),
                // invalid issn
                Arguments.of(Optional.of(Localization.lang("incorrect format")), "020-721"),
                Arguments.of(Optional.of(Localization.lang("incorrect format")), "0020-72109"),
                Arguments.of(Optional.of(Localization.lang("incorrect format")), "0020~72109"),
                Arguments.of(Optional.of(Localization.lang("incorrect format")), "Some other stuff"),
                Arguments.of(Optional.of(Localization.lang("incorrect control digit")), "0020-7218")
        );
    }

}
