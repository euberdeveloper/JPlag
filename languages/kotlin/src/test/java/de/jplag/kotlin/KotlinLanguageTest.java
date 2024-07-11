package de.jplag.kotlin;

import de.jplag.testutils.LanguageModuleTest;
import de.jplag.testutils.datacollector.TestDataCollector;
import de.jplag.testutils.datacollector.TestSourceIgnoredLinesCollector;

/**
 * Provides tests for the kotlin language module
 */
public class KotlinLanguageTest extends LanguageModuleTest {
    public KotlinLanguageTest() {
        super(new KotlinLanguage(), KotlinTokenType.class);
    }

    @Override
    protected void collectTestData(TestDataCollector collector) {
        collector.testFile("AnonymousVariables.kt");
        collector.testFile("CLI.kt");
        collector.testFile("Compact.kt");
        collector.testFile("IfElse.kt");
        collector.testFile("IfElseIf.kt");
        collector.testFile("IfIf.kt");
        collector.testFile("IfWithBraces.kt");
        collector.testFile("IfWithoutBraces.kt");
        collector.testFile("PatternMatching.kt");
        collector.testFile("PatternMatchingManual.kt");
        collector.testFile("StringConcat.kt");
        collector.testFile("StringTemplate.kt");
        collector.testFile("Try.kt");
        collector.testFile("TryWithResource.kt");
        collector.testFile("Verbose.kt");



        collector.testFile("Complete.kt").testCoverages();
        collector.testFile("Game.kt").testSourceCoverage();

        collector.testFile("HelloWorld.kt").testSourceCoverage().testTokenSequence(KotlinTokenType.PACKAGE, KotlinTokenType.FUNCTION,
                KotlinTokenType.FUNCTION_BODY_BEGIN, KotlinTokenType.FUNCTION_INVOCATION, KotlinTokenType.FUNCTION_BODY_END);

        collector.inlineSource("package de.jplag.kotlin\n").testSourceCoverage().testContainedTokens(KotlinTokenType.PACKAGE);
    }

    @Override
    protected void configureIgnoredLines(TestSourceIgnoredLinesCollector collector) {
        collector.ignoreMultipleLines("/*", "*/");
    }
}
