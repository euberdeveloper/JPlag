package de.jplag;

public enum GeneralTokenType implements TokenType {
    PACKAGE("PACKAGE"), 
    IMPORT("IMPORT"), 
    CLASS_BEGIN("CLASS{"), 
    CLASS_END("}CLASS"), 
    FUN_BEGIN("FUN{"),
    FUN_END("}FUN"),
    VARDEF("VARDEF"), 
    LOOP_BEGIN("LOOP{"), 
    LOOP_END("}LOOP"), 
    SWITCH_BEGIN("SWITCH{"), 
    SWITCH_END("}SWITCH"), 
    SWITCH_CASE("CASE"), 
    TRY_BEGIN("TRY{"), 
    TRY_END("}TRY"), 
    CATCH_BEGIN("CATCH{"), 
    CATCH_END("}CATCH"), 
    FINALLY_BEGIN("FINALLY{"), 
    FINALLY_END("}FINALLY"), 
    IF_BEGIN("IF{"), 
    IF_END("}IF"), 
    BREAK("BREAK"), 
    CONTINUE("CONTINUE"), 
    RETURN("RETURN"), 
    THROW("THROW"), 
    CALL("CALL"),
    ASSIGN("ASSIGN"), 
    INTERFACE_BEGIN("INTERF{"), 
    INTERFACE_END("}INTERF"), 
    ENUM_BEGIN("ENUM"), 
    ENUM_END("}ENUM"), 
    GENERIC("GENERIC"),

    PIPPO("PIPPO");

    private final String description;

    @Override
    public String getDescription() {
        return this.description;
    }

    GeneralTokenType(String description) {
        this.description = description;
    }
}
