package org.jabref.logic.formatter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormattersTest {

    long expected;

    @Test
    void getConvertersTest() {
        expected = 4;
        assertEquals(expected, Formatters.getConverters().stream().count());
    }

    @Test
    void getCaseChangersTest() {
        expected = 5;
        assertEquals(expected, Formatters.getConverters().stream().count());
    }

    @Test
    void getOthersTest() {
        expected = 13;
        assertEquals(expected, Formatters.getConverters().stream().count());
    }

    @Test
    void getAllTest() {
        expected = 22;
        assertEquals(expected, Formatters.getConverters().stream().count());
    }
}
