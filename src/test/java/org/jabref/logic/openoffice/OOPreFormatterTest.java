package org.jabref.logic.openoffice;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OOPreFormatterTest {

    OOPreFormatter formatter = new OOPreFormatter();

    @ParameterizedTest
    @MethodSource("preFormatterTestArguments")
    void preFormatterTest(String expectedString, String inputString){
        assertEquals(expectedString, formatter.format(inputString));
    }

    private static Stream<Arguments> preFormatterTestArguments() {
        return Stream.of(
                // plain format
                Arguments.of("aaa", "aaa"),
                Arguments.of("$", "\\$"),
                Arguments.of("%", "\\%"),
                Arguments.of("\\", "\\\\"),
                // accent format
                Arguments.of("ä", "{\\\"{a}}"),
                Arguments.of("Ä", "{\\\"{A}}"),
                Arguments.of("Ç", "{\\c{C}}"),
                // special commands
                Arguments.of("å", "{\\aa}"),
                Arguments.of("bb", "{\\bb}"),
                Arguments.of("å a", "\\aa a"),
                Arguments.of("å a", "{\\aa a}"),
                Arguments.of("åÅ", "\\aa\\AA"),
                Arguments.of("bb a", "\\bb a"),
                // unsupported special commands
                Arguments.of("ftmch", "\\ftmch"),
                Arguments.of("ftmch", "{\\ftmch}"),
                Arguments.of("ftmchaaa", "{\\ftmch\\aaa}"),
                // equations
                Arguments.of("Σ", "$\\Sigma$"),
                // strip latex commands
                Arguments.of("-", "\\mbox{-}"),
                // general formatting
                Arguments.of("<i>kkk</i>", "\\textit{kkk}"),
                Arguments.of("<i>kkk</i>", "{\\it kkk}"),
                Arguments.of("<i>kkk</i>", "\\emph{kkk}"),
                Arguments.of("<b>kkk</b>", "\\textbf{kkk}"),
                Arguments.of("<smallcaps>kkk</smallcaps>", "\\textsc{kkk}"),
                Arguments.of("<s>kkk</s>", "\\sout{kkk}"),
                Arguments.of("<u>kkk</u>", "\\underline{kkk}"),
                Arguments.of("<tt>kkk</tt>", "\\texttt{kkk}"),
                Arguments.of("<sup>kkk</sup>", "\\textsuperscript{kkk}"),
                Arguments.of("<sub>kkk</sub>", "\\textsubscript{kkk}")
                );
    }
}
