package org.jabref.model.texparser;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CitationTest {

    private Path path;
    private Citation citation;
    private static final String LINE_TEXT = "lineText";
    private final int LINE_NUMBER = 10;
    private final int COL_START = 1;
    private final int COL_END = 4;

    @BeforeEach
    public void init() {
        path = Path.of("test");
        citation = new Citation(path, LINE_NUMBER, COL_START, COL_END, LINE_TEXT);
    }

    private static Stream<Arguments> colStartColEndNotInBounds() {
        return Stream.of(
                arguments(-1, LINE_TEXT.length() / 2), // start before 0
                arguments(1, LINE_TEXT.length()) // end after last index
        );
    }

    private static Stream<Arguments> colStartColEndInBounds() {
        return Stream.of(
                arguments(0, LINE_TEXT.length() / 2),
                arguments(1, LINE_TEXT.length() - 1)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    public void constructorLineSmallerEqualZeroTest(int line) {
        Exception e = assertThrows(IllegalArgumentException.class, () -> new Citation(path, line, COL_START, COL_END, LINE_TEXT));
        assertEquals("Line has to be greater than 0.", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    public void constructorLineLargerZeroTest(int line) {
        Citation citation = new Citation(path, line, COL_START, COL_END, LINE_TEXT);
    }

    @ParameterizedTest
    @MethodSource("colStartColEndNotInBounds")
    public void constructorColStartColEndNotInBoundsTest(int colStart, int colEnd) {
        Exception e = assertThrows(IllegalArgumentException.class, () -> new Citation(path, LINE_NUMBER, colStart, colEnd, LINE_TEXT));
        assertEquals("Citation has to be between 0 and line length.", e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("colStartColEndInBounds")
    public void constructorColStartColEndInBoundsTest(int colStart, int colEnd) {
        Citation citation = new Citation(path, LINE_NUMBER, colStart, colEnd, LINE_TEXT);
    }

    @Test
    public void getPathTest() {
        assertEquals(path, citation.getPath());
    }

    @Test
    public void getLineTest() {
        assertEquals(LINE_NUMBER, citation.getLine());
    }

    @Test
    public void getColStartTest() {
        assertEquals(COL_START, citation.getColStart());
    }

    @Test
    public void getColEndTest() {
        assertEquals(COL_END, citation.getColEnd());
    }

    @Test
    public void getLineTextTest() {
        assertEquals(LINE_TEXT, citation.getLineText());
    }

    @Test
    public void getContextTest() {
        assertEquals(LINE_TEXT, citation.getContext());
    }

    @Test
    public void equalsTest() {
        Citation citation1 = new Citation(path, LINE_NUMBER, COL_START, COL_END, "lineText");
        Citation citation2 = null;
        assertTrue(citation.equals(citation1));
        assertFalse(citation.equals(citation2));
    }
}
