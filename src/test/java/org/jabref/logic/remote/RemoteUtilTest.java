package org.jabref.logic.remote;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemoteUtilTest {

    @ParameterizedTest
    @MethodSource("remoteUtilArguments")
    void remoteUtilTests(int userPort, boolean acceptPort) {
        assertEquals(acceptPort, RemoteUtil.isUserPort(userPort));
    }

    private static Stream<Arguments> remoteUtilArguments() {
        return Stream.of(
                // Port number must be non negative.
                Arguments.of(-55, false),
                // Port number must be outside reserved system range (0-1023).
                Arguments.of(0, false),
                Arguments.of(1023, false),
                // Port number should be below 65535
                // 2 ^ 16 - 1 => 65535
                Arguments.of(65536, false),
                // Port number in between 1024 and 65535 should be valid
                // ports 1024 => 65535
                Arguments.of(1024, true),
                Arguments.of(65535, true)
        );
    }
}
