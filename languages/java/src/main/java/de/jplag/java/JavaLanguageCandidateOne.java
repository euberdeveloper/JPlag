package de.jplag.java;

import de.jplag.*;
import org.kohsuke.MetaInfServices;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 */
@MetaInfServices(Language.class)
public class JavaLanguageCandidateOne extends  AbstractGeneralJavaLanguage {
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
                case JavaTokenType.J_SYNC_BEGIN-> null;
                case JavaTokenType.J_SYNC_END-> null;
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
                case JavaTokenType.J_COND-> null;
                case JavaTokenType.J_BREAK -> GeneralTokenType.BREAK;
                case JavaTokenType.J_CONTINUE-> null;
                case JavaTokenType.J_RETURN -> GeneralTokenType.RETURN;
                case JavaTokenType.J_THROW -> GeneralTokenType.THROW;
                case JavaTokenType.J_IN_CLASS_BEGIN -> GeneralTokenType.CLASS_BEGIN;
                case JavaTokenType.J_IN_CLASS_END -> GeneralTokenType.CLASS_END;
                case JavaTokenType.J_APPLY -> GeneralTokenType.CALL;
                case JavaTokenType.J_NEWCLASS -> GeneralTokenType.CALL;
                case JavaTokenType.J_NEWARRAY-> null;
                case JavaTokenType.J_ASSIGN -> GeneralTokenType.ASSIGN;
                case JavaTokenType.J_INTERFACE_BEGIN -> GeneralTokenType.INTERFACE_BEGIN;
                case JavaTokenType.J_INTERFACE_END -> GeneralTokenType.INTERFACE_END;
                case JavaTokenType.J_CONSTR_BEGIN-> null;
                case JavaTokenType.J_CONSTR_END-> null;
                case JavaTokenType.J_VOID-> null;
                case JavaTokenType.J_ARRAY_INIT_BEGIN-> null;
                case JavaTokenType.J_ARRAY_INIT_END-> null;
                case JavaTokenType.J_ENUM_BEGIN -> GeneralTokenType.ENUM_BEGIN;
                case JavaTokenType.J_ENUM_CLASS_BEGIN-> null;
                case JavaTokenType.J_ENUM_END -> GeneralTokenType.ENUM_END;
                case JavaTokenType.J_GENERIC -> GeneralTokenType.GENERIC;
                case JavaTokenType.J_ASSERT-> null;
                case JavaTokenType.J_ANNO-> null;
                case JavaTokenType.J_ANNO_MARKER-> null;
                case JavaTokenType.J_ANNO_M_BEGIN-> null;
                case JavaTokenType.J_ANNO_M_END-> null;
                case JavaTokenType.J_ANNO_T_BEGIN-> null;
                case JavaTokenType.J_ANNO_T_END-> null;
                case JavaTokenType.J_ANNO_C_BEGIN-> null;
                case JavaTokenType.J_ANNO_C_END-> null;
                case JavaTokenType.J_REQUIRES-> null;
                case JavaTokenType.J_PROVIDES-> null;
                case JavaTokenType.J_EXPORTS-> null;
                case JavaTokenType.J_MODULE_BEGIN-> null;
                case JavaTokenType.J_MODULE_END-> null;
                case JavaTokenType.J_YIELD-> null;
                case JavaTokenType.J_DEFAULT-> null;
                case JavaTokenType.J_RECORD_BEGIN -> GeneralTokenType.CLASS_BEGIN;
                case JavaTokenType.J_RECORD_END -> GeneralTokenType.CLASS_END;
                case SharedTokenType.FILE_END -> SharedTokenType.FILE_END;
                default-> null;
            };
            return type == null ? null : new Token(type, token.getFile(), token.getLine(), token.getColumn(), token.getLength(), token.getSemantics());
        }).filter(Objects::nonNull).toList();
    }
}
