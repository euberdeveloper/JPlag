package de.jplag.cli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinTokenMatchTest extends CommandLineInterfaceTest {

    @Test
    void testLanguageDefault() throws CliException {
        // Language defaults not set yet:
        buildOptionsFromCLI(defaultArguments());
        assertFalse(options.languages().isEmpty());
        assertEquals(options.languagesMinTokenMatch(), options.minimumTokenMatch().intValue());
    }

    @Test
    void testInvalidInput() {
        Assertions.assertThrowsExactly(CliException.class, () -> buildOptionsFromCLI(defaultArguments().minTokens("Not an integer...")));
    }

    @Test
    void testUpperBound() {
        String higherThanMax = String.valueOf(((long) Integer.MAX_VALUE) + 1);

        Assertions.assertThrowsExactly(CliException.class, () -> buildOptionsFromCLI(defaultArguments().minTokens(higherThanMax)));
    }

    @Test
    void testLowerBound() throws CliException {
        buildOptionsFromCLI(defaultArguments().minTokens(-1));
        assertEquals(1, options.minimumTokenMatch().intValue());
    }

    @Test
    void testValidThreshold() throws CliException {
        int expectedValue = 50;
        buildOptionsFromCLI(defaultArguments().minTokens(expectedValue));
        assertEquals(expectedValue, options.minimumTokenMatch().intValue());
    }
}
