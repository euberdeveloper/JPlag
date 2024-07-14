package de.jplag.python3;

import de.jplag.*;
import org.kohsuke.MetaInfServices;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/* ASSIGN is VARDEF */
@MetaInfServices(Language.class)
public class PythonLanguageCandidateTwo extends AbstractGeneralPythonLanguage {
    @Override
    public List<Token> parseGeneral(Set<File> files, boolean normalize) throws ParsingException {
        List<Token> specificTokens = this.parse(files, normalize);
        return specificTokens.stream().map(token -> {
            TokenType type = switch (token.getType()) {
                case Python3TokenType.IMPORT -> GeneralTokenType.PACKAGE;
                case Python3TokenType.CLASS_BEGIN -> GeneralTokenType.CLASS_BEGIN;
                case Python3TokenType.CLASS_END -> GeneralTokenType.CLASS_END;
                case Python3TokenType.METHOD_BEGIN -> GeneralTokenType.FUN_BEGIN;
                case Python3TokenType.METHOD_END -> GeneralTokenType.FUN_END;
                case Python3TokenType.ASSIGN -> GeneralTokenType.VARDEF;
                case Python3TokenType.WHILE_BEGIN -> GeneralTokenType.LOOP_BEGIN;
                case Python3TokenType.WHILE_END -> GeneralTokenType.LOOP_END;
                case Python3TokenType.FOR_BEGIN -> GeneralTokenType.LOOP_BEGIN;
                case Python3TokenType.FOR_END -> GeneralTokenType.LOOP_END;
                case Python3TokenType.TRY_BEGIN -> GeneralTokenType.TRY_BEGIN;
                case Python3TokenType.EXCEPT_BEGIN -> GeneralTokenType.CATCH_BEGIN;
                case Python3TokenType.EXCEPT_END -> GeneralTokenType.CATCH_END;
                case Python3TokenType.FINALLY -> null;
                case Python3TokenType.IF_BEGIN -> GeneralTokenType.IF_BEGIN;
                case Python3TokenType.IF_END -> GeneralTokenType.IF_END;
                case Python3TokenType.APPLY -> GeneralTokenType.CALL;
                case Python3TokenType.BREAK -> GeneralTokenType.BREAK;
                case Python3TokenType.CONTINUE -> GeneralTokenType.CONTINUE;
                case Python3TokenType.RETURN -> GeneralTokenType.RETURN;
                case Python3TokenType.RAISE -> GeneralTokenType.THROW;
                case Python3TokenType.DEC_BEGIN -> null;
                case Python3TokenType.DEC_END -> null;
                case Python3TokenType.LAMBDA -> null;
                case Python3TokenType.ARRAY -> null;
                case Python3TokenType.ASSERT -> null;
                case Python3TokenType.YIELD -> null;
                case Python3TokenType.DEL -> null;
                case Python3TokenType.WITH_BEGIN -> null;
                case Python3TokenType.WITH_END -> null;
                case Python3TokenType.ASYNC -> null;
                case Python3TokenType.AWAIT -> null;
                case SharedTokenType.FILE_END -> SharedTokenType.FILE_END;
                default-> null;
            };
            return type == null ? null : new Token(type, token.getFile(), token.getLine(), token.getColumn(), token.getLength(), token.getSemantics());
        }).filter(Objects::nonNull).toList();
    }
}
