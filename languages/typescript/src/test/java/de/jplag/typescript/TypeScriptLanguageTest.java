package de.jplag.typescript;

import de.jplag.testutils.LanguageModuleTest;
import de.jplag.testutils.datacollector.TestDataCollector;
import de.jplag.testutils.datacollector.TestSourceIgnoredLinesCollector;

public class TypeScriptLanguageTest extends LanguageModuleTest {

    public TypeScriptLanguageTest() {
        super(new TypeScriptLanguage(), TypeScriptTokenType.class);
    }

    @Override
    protected void collectTestData(TestDataCollector collector) {
        collector.testFile("AnonymousVariables.ts");
        collector.testFile("Compact.ts");
        collector.testFile("IfElse.ts");
        collector.testFile("IfElseIf.ts");
        collector.testFile("IfIf.ts");
        collector.testFile("IfWithBraces.ts");
        collector.testFile("IfWithoutBraces.ts");
        collector.testFile("PatternMatching.ts");
        collector.testFile("PatternMatchingManual.ts");
        collector.testFile("StringConcat.ts");
        collector.testFile("StringTemplate.ts");
        collector.testFile("Try.ts");
        collector.testFile("TryWithResource.ts");
        collector.testFile("Verbose.ts");

        /*collector.testFile("simpleTest.ts").testSourceCoverage().testTokenSequence(TypeScriptTokenType.DECLARATION, TypeScriptTokenType.ASSIGNMENT,
                TypeScriptTokenType.DECLARATION, TypeScriptTokenType.ASSIGNMENT, TypeScriptTokenType.FOR_BEGIN, TypeScriptTokenType.ASSIGNMENT,
                TypeScriptTokenType.ASSIGNMENT, TypeScriptTokenType.FUNCTION_CALL, TypeScriptTokenType.FOR_END, TypeScriptTokenType.DECLARATION,
                TypeScriptTokenType.ASSIGNMENT, TypeScriptTokenType.FUNCTION_CALL, TypeScriptTokenType.ASSIGNMENT);
        collector.testFile("forLoops.ts").testTokenSequence(TypeScriptTokenType.DECLARATION, TypeScriptTokenType.ASSIGNMENT,
                TypeScriptTokenType.FOR_BEGIN, TypeScriptTokenType.ASSIGNMENT, TypeScriptTokenType.ASSIGNMENT, TypeScriptTokenType.FUNCTION_CALL,
                TypeScriptTokenType.FOR_END, TypeScriptTokenType.FOR_BEGIN, TypeScriptTokenType.FUNCTION_CALL, TypeScriptTokenType.FOR_END,
                TypeScriptTokenType.FOR_BEGIN, TypeScriptTokenType.FUNCTION_CALL, TypeScriptTokenType.FOR_END);
        collector.testFile("methods.ts").testTokenSequence(TypeScriptTokenType.DECLARATION, TypeScriptTokenType.ASSIGNMENT,
                TypeScriptTokenType.METHOD_BEGIN, TypeScriptTokenType.RETURN, TypeScriptTokenType.METHOD_END, TypeScriptTokenType.DECLARATION,
                TypeScriptTokenType.ASSIGNMENT, TypeScriptTokenType.METHOD_BEGIN, TypeScriptTokenType.RETURN, TypeScriptTokenType.METHOD_END,
                TypeScriptTokenType.DECLARATION, TypeScriptTokenType.ASSIGNMENT, TypeScriptTokenType.METHOD_BEGIN, TypeScriptTokenType.RETURN,
                TypeScriptTokenType.METHOD_END);
        collector.testFile("class.ts").testSourceCoverage().testTokenSequence(TypeScriptTokenType.CLASS_BEGIN, TypeScriptTokenType.DECLARATION,
                TypeScriptTokenType.DECLARATION, TypeScriptTokenType.ASSIGNMENT, TypeScriptTokenType.CONSTRUCTOR_BEGIN,
                TypeScriptTokenType.ASSIGNMENT, TypeScriptTokenType.CONSTRUCTOR_END, TypeScriptTokenType.METHOD_BEGIN, TypeScriptTokenType.RETURN,
                TypeScriptTokenType.METHOD_END, TypeScriptTokenType.METHOD_BEGIN, TypeScriptTokenType.ASSIGNMENT, TypeScriptTokenType.METHOD_END,
                TypeScriptTokenType.METHOD_BEGIN, TypeScriptTokenType.RETURN, TypeScriptTokenType.METHOD_END, TypeScriptTokenType.METHOD_BEGIN,
                TypeScriptTokenType.ASSIGNMENT, TypeScriptTokenType.METHOD_END, TypeScriptTokenType.CLASS_END);
        collector.testFile("if.ts").testSourceCoverage().testTokenSequence(TypeScriptTokenType.IF_BEGIN, TypeScriptTokenType.FUNCTION_CALL,
                TypeScriptTokenType.IF_BEGIN, TypeScriptTokenType.IF_BEGIN, TypeScriptTokenType.FUNCTION_CALL, TypeScriptTokenType.IF_BEGIN,
                TypeScriptTokenType.FUNCTION_CALL, TypeScriptTokenType.IF_END, TypeScriptTokenType.IF_END, TypeScriptTokenType.IF_BEGIN,
                TypeScriptTokenType.FUNCTION_CALL, TypeScriptTokenType.IF_END);
        collector.testFile("allTokens.ts").testCoverages();*/
    }

    @Override
    protected void configureIgnoredLines(TestSourceIgnoredLinesCollector collector) {
        collector.ignoreMultipleLines("/*", "*/");
        collector.ignoreLinesByPrefix("//");
    }
}
