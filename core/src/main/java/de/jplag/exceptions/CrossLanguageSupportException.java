package de.jplag.exceptions;

import java.io.Serial;

/**
 * Exceptions for problems with the support of cross language plagiarism detection.
 */
public class CrossLanguageSupportException extends ExitException {

    /*@Serial
    private static final long serialVersionUID = 7091658804288889231L; // generated*/

    public CrossLanguageSupportException(String message) {
        super(message);
    }

    public CrossLanguageSupportException(String message, Throwable cause) {
        super(message, cause);
    }
}
