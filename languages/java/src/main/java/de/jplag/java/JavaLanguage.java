package de.jplag.java;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import de.jplag.*;
import org.kohsuke.MetaInfServices;

/**
 * Language for Java 9 and newer.
 */
@MetaInfServices(Language.class)
public class JavaLanguage implements GeneralLanguage {
    private static final String IDENTIFIER = "java";
    public static final int JAVA_VERSION = 21;

    private final Parser parser;

    public JavaLanguage() {
        parser = new Parser();
    }

    @Override
    public String[] suffixes() {
        return new String[]{".java", ".JAVA"};
    }

    @Override
    public String getName() {
        return "Javac based AST plugin";
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public int minimumTokenMatch() {
        return 9;
    }

    @Override
    public List<Token> parse(Set<File> files, boolean normalize) throws ParsingException {
        return this.parser.parse(files);
    }

    @Override
    public boolean tokensHaveSemantics() {
        return true;
    }

    @Override
    public boolean supportsNormalization() {
        return true;
    }

    @Override
    public String toString() {
        return this.getIdentifier();
    }

    @Override
    public List<Token> parseGeneral(Set<File> files, boolean normalize) throws ParsingException {
        List<Token> specificTokens = this.parse(files, normalize);
        return specificTokens.stream().map(token -> {
            TokenType type = switch (token.getType()) {
                case JavaTokenType.J_PACKAGE -> GeneralTokenType.PACKAGE;
                case JavaTokenType.J_IMPORT -> GeneralTokenType.IMPORT;
                case JavaTokenType.J_CLASS_BEGIN -> GeneralTokenType.CLASS_BEGIN;
                case JavaTokenType.J_CLASS_END -> GeneralTokenType.CLASS_END;
                case JavaTokenType.J_METHOD_BEGIN -> GeneralTokenType.FUN_BEGIN;
                case JavaTokenType.J_METHOD_END -> GeneralTokenType.FUN_END;
                case JavaTokenType.J_VARDEF -> GeneralTokenType.VARDEF;
                case JavaTokenType.J_SYNC_BEGIN-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_SYNC_END-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_LOOP_BEGIN -> GeneralTokenType.LOOP_BEGIN;
                case JavaTokenType.J_LOOP_END -> GeneralTokenType.LOOP_END;
                case JavaTokenType.J_SWITCH_BEGIN -> GeneralTokenType.SWITCH_BEGIN;
                case JavaTokenType.J_SWITCH_END -> GeneralTokenType.SWITCH_END;
                case JavaTokenType.J_CASE -> GeneralTokenType.SWITCH_CASE;
                case JavaTokenType.J_TRY_BEGIN -> GeneralTokenType.TRY_BEGIN;
                case JavaTokenType.J_TRY_END -> GeneralTokenType.TRY_END;
                case JavaTokenType.J_CATCH_BEGIN -> GeneralTokenType.CATCH_BEGIN;
                case JavaTokenType.J_CATCH_END -> GeneralTokenType.CATCH_END;
                case JavaTokenType.J_FINALLY_BEGIN -> GeneralTokenType.FINALLY_BEGIN;
                case JavaTokenType.J_FINALLY_END -> GeneralTokenType.FINALLY_END;
                case JavaTokenType.J_IF_BEGIN -> GeneralTokenType.IF_BEGIN;
                case JavaTokenType.J_IF_END -> GeneralTokenType.IF_END;
                case JavaTokenType.J_COND-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_BREAK -> GeneralTokenType.BREAK;
                case JavaTokenType.J_CONTINUE-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_RETURN -> GeneralTokenType.RETURN;
                case JavaTokenType.J_THROW -> GeneralTokenType.THROW;
                case JavaTokenType.J_IN_CLASS_BEGIN -> GeneralTokenType.CLASS_BEGIN;
                case JavaTokenType.J_IN_CLASS_END -> GeneralTokenType.CLASS_END;
                case JavaTokenType.J_APPLY -> GeneralTokenType.CALL;
                case JavaTokenType.J_NEWCLASS -> GeneralTokenType.CALL;
                case JavaTokenType.J_NEWARRAY-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_ASSIGN -> GeneralTokenType.ASSIGN;
                case JavaTokenType.J_INTERFACE_BEGIN -> GeneralTokenType.INTERFACE_BEGIN;
                case JavaTokenType.J_INTERFACE_END -> GeneralTokenType.INTERFACE_END;
                case JavaTokenType.J_CONSTR_BEGIN-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_CONSTR_END-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_VOID-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_ARRAY_INIT_BEGIN-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_ARRAY_INIT_END-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_ENUM_BEGIN -> GeneralTokenType.ENUM_BEGIN;
                case JavaTokenType.J_ENUM_CLASS_BEGIN-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_ENUM_END -> GeneralTokenType.ENUM_END;
                case JavaTokenType.J_GENERIC -> GeneralTokenType.GENERIC;
                case JavaTokenType.J_ASSERT-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_ANNO-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_ANNO_MARKER-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_ANNO_M_BEGIN-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_ANNO_M_END-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_ANNO_T_BEGIN-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_ANNO_T_END-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_ANNO_C_BEGIN-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_ANNO_C_END-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_REQUIRES-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_PROVIDES-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_EXPORTS-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_MODULE_BEGIN-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_MODULE_END-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_YIELD-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_DEFAULT-> GeneralTokenType.PIPPO;
                case JavaTokenType.J_RECORD_BEGIN -> GeneralTokenType.CLASS_BEGIN;
                case JavaTokenType.J_RECORD_END -> GeneralTokenType.CLASS_END;
                case SharedTokenType.FILE_END -> SharedTokenType.FILE_END;
                default-> GeneralTokenType.PIPPO;
            };
            return type == null ? null : new Token(type, token.getFile(), token.getLine(), token.getColumn(), token.getLength());
        }).filter(Objects::nonNull).toList();
    }
}
