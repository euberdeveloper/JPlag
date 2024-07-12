package de.jplag.kotlin;

import de.jplag.GeneralLanguage;
import de.jplag.ParsingException;
import de.jplag.Token;
import org.kohsuke.MetaInfServices;

import de.jplag.antlr.AbstractAntlrLanguage;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * This represents the Kotlin language as a language supported by JPlag.
 */
@MetaInfServices(de.jplag.Language.class)
public class KotlinLanguage extends AbstractAntlrLanguage implements GeneralLanguage {

    private static final String NAME = "Kotlin Parser";
    private static final String IDENTIFIER = "kotlin";
    private static final int DEFAULT_MIN_TOKEN_MATCH = 8;
    private static final String[] FILE_EXTENSIONS = {".kt"};

    public KotlinLanguage() {
        super(new KotlinParserAdapter());
    }

    @Override
    public String[] suffixes() {
        return FILE_EXTENSIONS;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public int minimumTokenMatch() {
        return DEFAULT_MIN_TOKEN_MATCH;
    }

    @Override
    public List<Token> parseGeneral(Set<File> files, boolean normalize) throws ParsingException {
        // TODO
        return parse(files, normalize);
    }
}
