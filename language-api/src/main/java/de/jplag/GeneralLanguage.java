package de.jplag;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Common interface for all languages. Each language-front end must provide a concrete language implementation.
 */
public interface GeneralLanguage extends Language {

    @Override
    default boolean supportsCrossLanguage() {
        return true;
    }

    default List<Token> parse(Set<File> files, boolean normalize, boolean isCrossLanguage) throws ParsingException {
        return isCrossLanguage
                ? parseGeneral(files, normalize)
                : parse(files, normalize);
    }

    List<Token> parseGeneral(Set<File> files, boolean normalize) throws ParsingException;
}
