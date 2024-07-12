package de.jplag.kotlin;

import de.jplag.*;
import de.jplag.antlr.AbstractAntlrLanguage;
import org.kohsuke.MetaInfServices;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 */
@MetaInfServices(Language.class)
public class KotlinLanguageCandidateOne extends AbstractGeneralKotlinLanguage {

    @Override
    public List<Token> parseGeneral(Set<File> files, boolean normalize) throws ParsingException {
        List<Token> specificTokens = this.parse(files, normalize);
        return specificTokens.stream().map(token -> {
            TokenType type = switch (token.getType()) {
                case KotlinTokenType.PACKAGE -> GeneralTokenType.PACKAGE;
                case KotlinTokenType.IMPORT -> GeneralTokenType.IMPORT;
                case KotlinTokenType.CLASS_DECLARATION -> null;
                case KotlinTokenType.OBJECT_DECLARATION -> null;
                case KotlinTokenType.COMPANION_DECLARATION -> null;
                case KotlinTokenType.TYPE_PARAMETER -> GeneralTokenType.GENERIC;
                case KotlinTokenType.CONSTRUCTOR -> null;
                case KotlinTokenType.CLASS_BODY_BEGIN -> GeneralTokenType.CLASS_BEGIN;
                case KotlinTokenType.CLASS_BODY_END -> GeneralTokenType.CLASS_END;
                case KotlinTokenType.ENUM_CLASS_BODY_BEGIN -> GeneralTokenType.ENUM_BEGIN;
                case KotlinTokenType.ENUM_CLASS_BODY_END -> GeneralTokenType.ENUM_END;
                case KotlinTokenType.PROPERTY_DECLARATION -> GeneralTokenType.VARDEF;
                case KotlinTokenType.INITIALIZER -> null;
                case KotlinTokenType.INITIALIZER_BODY_START -> null;
                case KotlinTokenType.INITIALIZER_BODY_END -> null;
                case KotlinTokenType.FUNCTION -> null;
                case KotlinTokenType.GETTER -> null;
                case KotlinTokenType.SETTER -> null;
                case KotlinTokenType.FUNCTION_PARAMETER -> GeneralTokenType.VARDEF;
                case KotlinTokenType.FUNCTION_BODY_BEGIN -> GeneralTokenType.FUN_BEGIN;
                case KotlinTokenType.FUNCTION_BODY_END -> GeneralTokenType.FUN_END;
                case KotlinTokenType.FUNCTION_LITERAL_BEGIN -> GeneralTokenType.FUN_BEGIN;
                case KotlinTokenType.FUNCTION_LITERAL_END -> GeneralTokenType.FUN_END;
                case KotlinTokenType.FOR_EXPRESSION_BEGIN -> GeneralTokenType.LOOP_BEGIN;
                case KotlinTokenType.FOR_EXPRESSION_END -> GeneralTokenType.LOOP_END;
                case KotlinTokenType.IF_EXPRESSION_BEGIN -> GeneralTokenType.IF_BEGIN;
                case KotlinTokenType.IF_EXPRESSION_END -> GeneralTokenType.IF_END;
                case KotlinTokenType.WHILE_EXPRESSION_START -> GeneralTokenType.LOOP_BEGIN;
                case KotlinTokenType.WHILE_EXPRESSION_END -> GeneralTokenType.LOOP_END;
                case KotlinTokenType.DO_WHILE_EXPRESSION_START -> GeneralTokenType.LOOP_BEGIN;
                case KotlinTokenType.DO_WHILE_EXPRESSION_END -> GeneralTokenType.LOOP_END;
                case KotlinTokenType.TRY_EXPRESSION -> null;
                case KotlinTokenType.TRY_BODY_START -> GeneralTokenType.TRY_BEGIN;
                case KotlinTokenType.TRY_BODY_END -> GeneralTokenType.TRY_BEGIN;
                case KotlinTokenType.CATCH -> null;
                case KotlinTokenType.CATCH_BODY_START -> GeneralTokenType.CATCH_BEGIN;
                case KotlinTokenType.CATCH_BODY_END -> GeneralTokenType.CATCH_END;
                case KotlinTokenType.FINALLY -> null;
                case KotlinTokenType.FINALLY_BODY_START -> GeneralTokenType.FINALLY_BEGIN;
                case KotlinTokenType.FINALLY_BODY_END -> GeneralTokenType.FINALLY_END;
                case KotlinTokenType.WHEN_EXPRESSION_START -> null;
                case KotlinTokenType.WHEN_EXPRESSION_END -> null;
                case KotlinTokenType.WHEN_CONDITION -> null;
                case KotlinTokenType.CONTROL_STRUCTURE_BODY_START -> null;
                case KotlinTokenType.CONTROL_STRUCTURE_BODY_END -> null;
                case KotlinTokenType.VARIABLE_DECLARATION -> null;
                case KotlinTokenType.ENUM_ENTRY -> null;
                case KotlinTokenType.FUNCTION_INVOCATION -> GeneralTokenType.CALL;
                case KotlinTokenType.CREATE_OBJECT -> null;
                case KotlinTokenType.ASSIGNMENT -> GeneralTokenType.ASSIGN;
                case KotlinTokenType.THROW -> GeneralTokenType.THROW;
                case KotlinTokenType.RETURN -> GeneralTokenType.RETURN;
                case KotlinTokenType.CONTINUE -> GeneralTokenType.CONTINUE;
                case KotlinTokenType.BREAK -> GeneralTokenType.BREAK;
                case SharedTokenType.FILE_END -> SharedTokenType.FILE_END;
                default -> null;
            };
            return type == null ? null : new Token(type, token.getFile(), token.getLine(), token.getColumn(), token.getLength());
        }).filter(Objects::nonNull).toList();
    }
}
