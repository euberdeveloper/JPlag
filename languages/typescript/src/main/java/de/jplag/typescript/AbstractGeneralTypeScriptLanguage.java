package de.jplag.typescript;

import de.jplag.*;
import de.jplag.antlr.AbstractAntlrLanguage;
import org.kohsuke.MetaInfServices;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * This represents the TypeScript language as a language supported by JPlag.
 */
@MetaInfServices(Language.class)
public abstract class AbstractGeneralTypeScriptLanguage
        extends TypeScriptLanguage
        implements GeneralLanguage {}
