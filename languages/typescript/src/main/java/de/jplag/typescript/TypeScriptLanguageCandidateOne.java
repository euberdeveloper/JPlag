package de.jplag.typescript;

import de.jplag.*;
import org.kohsuke.MetaInfServices;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Normal 1:1
 */
@MetaInfServices(Language.class)
public class TypeScriptLanguageCandidateOne extends AbstractGeneralTypeScriptLanguage {

    @Override
    public List<Token> parseGeneral(Set<File> files, boolean normalize) throws ParsingException {
        List<Token> specificTokens = this.parse(files, normalize);
        return specificTokens.stream().map(token -> {
            TokenType type = switch (token.getType()) {
                case TypeScriptTokenType.IMPORT -> GeneralTokenType.PACKAGE;
                case TypeScriptTokenType.EXPORT -> null;
                case TypeScriptTokenType.NAMESPACE_BEGIN -> null;
                case TypeScriptTokenType.NAMESPACE_END -> null;
                case TypeScriptTokenType.CLASS_BEGIN -> GeneralTokenType.CLASS_BEGIN;
                case TypeScriptTokenType.CLASS_END -> GeneralTokenType.CLASS_END;
                case TypeScriptTokenType.INTERFACE_BEGIN -> GeneralTokenType.INTERFACE_BEGIN;
                case TypeScriptTokenType.INTERFACE_END -> GeneralTokenType.INTERFACE_END;
                case TypeScriptTokenType.ENUM_BEGIN -> GeneralTokenType.ENUM_BEGIN;
                case TypeScriptTokenType.ENUM_END -> GeneralTokenType.ENUM_END;
                case TypeScriptTokenType.METHOD_BEGIN -> GeneralTokenType.FUN_BEGIN;
                case TypeScriptTokenType.METHOD_END -> GeneralTokenType.FUN_END;
                case TypeScriptTokenType.WHILE_BEGIN -> GeneralTokenType.LOOP_BEGIN;
                case TypeScriptTokenType.WHILE_END -> GeneralTokenType.LOOP_END;
                case TypeScriptTokenType.FOR_BEGIN -> GeneralTokenType.LOOP_BEGIN;
                case TypeScriptTokenType.FOR_END -> GeneralTokenType.LOOP_END;
                case TypeScriptTokenType.ASSIGNMENT -> GeneralTokenType.ASSIGN;
                case TypeScriptTokenType.IF_BEGIN -> GeneralTokenType.IF_BEGIN;
                case TypeScriptTokenType.IF_END -> GeneralTokenType.IF_END;
                case TypeScriptTokenType.SWITCH_BEGIN -> GeneralTokenType.SWITCH_BEGIN;
                case TypeScriptTokenType.SWITCH_END -> GeneralTokenType.SWITCH_END;
                case TypeScriptTokenType.SWITCH_CASE -> GeneralTokenType.SWITCH_CASE;
                case TypeScriptTokenType.TRY_BEGIN -> GeneralTokenType.TRY_BEGIN;
                case TypeScriptTokenType.CATCH_BEGIN -> GeneralTokenType.CATCH_BEGIN;
                case TypeScriptTokenType.CATCH_END -> GeneralTokenType.CATCH_END;
                case TypeScriptTokenType.FINALLY_BEGIN -> GeneralTokenType.FINALLY_BEGIN;
                case TypeScriptTokenType.FINALLY_END -> GeneralTokenType.FINALLY_END;
                case TypeScriptTokenType.BREAK -> GeneralTokenType.BREAK;
                case TypeScriptTokenType.RETURN -> GeneralTokenType.RETURN;
                case TypeScriptTokenType.THROW -> GeneralTokenType.THROW;
                case TypeScriptTokenType.CONTINUE -> GeneralTokenType.CONTINUE;
                case TypeScriptTokenType.FUNCTION_CALL -> GeneralTokenType.CALL;
                case TypeScriptTokenType.ENUM_MEMBER -> null;
                case TypeScriptTokenType.CONSTRUCTOR_BEGIN -> null;
                case TypeScriptTokenType.CONSTRUCTOR_END -> null;
                case TypeScriptTokenType.DECLARATION -> GeneralTokenType.VARDEF;
                case SharedTokenType.FILE_END -> SharedTokenType.FILE_END;
                default-> null;
            };
            return type == null ? null : new Token(type, token.getFile(), token.getLine(), token.getColumn(), token.getLength());
        }).filter(Objects::nonNull).toList();
    }
}
