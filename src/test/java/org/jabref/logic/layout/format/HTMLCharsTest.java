package org.jabref.logic.layout.format;

import org.jabref.logic.layout.LayoutFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTMLCharsTest {

    private LayoutFormatter htmlLayout = new HTMLChars();

    @ParameterizedTest
    @MethodSource("formatTests")
    void paramLayoutFormatTest(String expectedString, String inputString) {
        assertEquals(expectedString, htmlLayout.format(inputString));
    }

    private static Stream<Arguments> formatTests() {
        return Stream.of(
                //Basic Formatting Tests
                Arguments.of("", ""),
                Arguments.of("hallo", "hallo"),
                Arguments.of("Réflexions sur le timing de la quantité", "Réflexions sur le timing de la quantité"),

                Arguments.of("%%%", "\\%\\%\\%"),
                Arguments.of("h&aacute;llo", "h\\'allo"),
                Arguments.of("&imath; &imath;", "\\i \\i"),
                Arguments.of("&imath;", "\\i"),
                Arguments.of("&imath;", "\\{i}"),
                Arguments.of("&imath;&imath;", "\\i\\i"),

                Arguments.of("&auml;", "{\\\"{a}}"),
                Arguments.of("&auml;", "{\\\"a}"),
                Arguments.of("&auml;", "\\\"a"),

                Arguments.of("&Ccedil;", "{\\c{C}}"),

                Arguments.of("&Oogon;&imath;", "\\k{O}\\i"),
                Arguments.of("&ntilde; &ntilde; &iacute; &imath; &imath;", "\\~{n} \\~n \\'i \\i \\i"),

                //Latex Highlighting Tests
                Arguments.of("<em>hallo</em>", "\\emph{hallo}"),
                Arguments.of("<em>hallo</em>", "{\\emph hallo}"),
                Arguments.of("<em>hallo</em>", "{\\em hallo}"),

                Arguments.of("<i>hallo</i>", "\\textit{hallo}"),
                Arguments.of("<i>hallo</i>", "{\\textit hallo}"),
                Arguments.of("<i>hallo</i>", "{\\it hallo}"),

                Arguments.of("<b>hallo</b>", "\\textbf{hallo}"),
                Arguments.of("<b>hallo</b>", "{\\textbf hallo}"),
                Arguments.of("<b>hallo</b>", "{\\bf hallo}"),

                Arguments.of("<sup>hallo</sup>", "\\textsuperscript{hallo}"),
                Arguments.of("<sub>hallo</sub>", "\\textsubscript{hallo}"),

                Arguments.of("<u>hallo</u>", "\\underline{hallo}"),
                Arguments.of("<s>hallo</s>", "\\sout{hallo}"),
                Arguments.of("<tt>hallo</tt>", "\\texttt{hallo}"),

                //Equations Tests
                Arguments.of("&dollar;", "\\$"),
                Arguments.of("&sigma;", "$\\sigma$"),
                Arguments.of("A 32&nbsp;mA &Sigma;&Delta;-modulator", "A 32~{mA} {$\\Sigma\\Delta$}-modulator"),

                //NewLine Tests
                Arguments.of("a<br>b", "a\nb"),
                Arguments.of("a<p>b", "a\n\nb"),

                //TODO: Add test cases for individual chars..

                //TextQuoteSingle Test
                Arguments.of("&#39;", "{\\textquotesingle}"),

                //Unknown Commands Tests
                Arguments.of("aaaa", "\\aaaa"),
                Arguments.of("bbbb", "\\aaaa{bbbb}"),
                Arguments.of("aaaa", "\\aaaa{}")
        );
    }
}
