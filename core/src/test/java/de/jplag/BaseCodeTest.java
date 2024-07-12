package de.jplag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import de.jplag.java.JavaLanguage;
import de.jplag.kotlin.KotlinLanguage;
import de.jplag.options.JPlagOptions;
import de.jplag.reporting.reportobject.ReportObjectFactory;
import de.jplag.typescript.TypeScriptLanguageCandidateTwo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.jplag.exceptions.BasecodeException;
import de.jplag.exceptions.ExitException;
import de.jplag.exceptions.RootDirectoryException;

/**
 * Tests the basecode functionality, that allows specifying a shared foundation, from which all submissions were
 * derived. The parts of the submissions that match with the basecode are ignored for the comparison. The basecode
 * feature is tested in combination with different root folders and the subdirectory feature.
 */
public class BaseCodeTest extends TestBase {

    @Test
    @DisplayName("test two submissions with basecode in root folder")
    void testBasecodeUserSubmissionComparison() throws ExitException {
        JPlagResult result = runJPlag("basecode",
                it -> it.withBaseCodeSubmissionDirectory(new File(it.submissionDirectories().iterator().next(), "base")));
        verifyResults(result);
    }

    @Test
    @DisplayName("test basecode that is too small")
    void testTinyBasecode() {
        assertThrows(BasecodeException.class, () -> runJPlag("TinyBasecode",
                it -> it.withBaseCodeSubmissionDirectory(new File(it.submissionDirectories().iterator().next(), "base"))));
    }

    @Test
    @DisplayName("test cross language even if only java")
    void testCrossLanguage() throws ExitException {
        JPlagResult result = runJPlag("basecode",
                it -> it.withLanguageOption(Arrays.asList(new JavaLanguage(), new KotlinLanguage())).withBaseCodeSubmissionDirectory(new File(it.submissionDirectories().iterator().next(), "base")));
        verifyResults(result);
    }

    @Test
    @DisplayName("test cross language even if only java")
    void testCrossLanguagePerDavvero() throws ExitException, FileNotFoundException {
        List<File> submissionDirs = Arrays.stream(Objects.requireNonNull(new File(getBasePath("java-to-typescript-convertai")).listFiles())).toList();
        for (File submissionDir : submissionDirs) {
            List<Language> languages = Arrays.asList(new JavaLanguage(), new TypeScriptLanguageCandidateTwo());
            HashSet<File> submission = new HashSet<>();
            submission.add(submissionDir);
            JPlagOptions options = new JPlagOptions(languages, submission, new HashSet<>()).withMinimumTokenMatch(3);
            JPlagResult result = JPlag.run(options);
            new ReportObjectFactory(new File("C:\\Users\\eugen\\Desktop\\" + submissionDir.getName() + ".zip"))
                    .createAndSaveReport(result);
            System.out.println("ciao");
        }
    }

    @Test
    @DisplayName("test empty submissions with basecode")
    void testEmptySubmission() throws ExitException {
        JPlagResult result = runJPlag("emptysubmission",
                it -> it.withBaseCodeSubmissionDirectory(new File(it.submissionDirectories().iterator().next(), "base")));
        verifyResults(result);
    }

    protected void verifyResults(JPlagResult result) {
        assertEquals(2, result.getNumberOfSubmissions());
        assertEquals(1, result.getAllComparisons().size());
        assertEquals(1, result.getAllComparisons().get(0).matches().size());
        assertEquals(1, result.getSimilarityDistribution()[81]);
        assertEquals(0.8125, result.getAllComparisons().get(0).similarity(), DELTA);
    }

    @Test
    @DisplayName("test external basecode that is not in a root folder")
    void testBasecodePathComparison() throws ExitException {
        JPlagResult result = runJPlag("basecode", it -> it.withBaseCodeSubmissionDirectory(new File(BASE_PATH, "basecode-base")));
        assertEquals(3, result.getNumberOfSubmissions()); // "basecode/base" is now a user submission.
    }

    @Test
    @DisplayName("test invalid root folder")
    void testInvalidRoot() {
        assertThrows(RootDirectoryException.class, () -> runJPlagWithDefaultOptions("WrongRoot"));
    }

    @Test
    @DisplayName("test invalid basecode folder")
    void testInvalidBasecode() {
        assertThrows(BasecodeException.class, () -> runJPlag("basecode",
                it -> it.withBaseCodeSubmissionDirectory(new File(it.submissionDirectories().iterator().next(), "WrongBasecode"))));
    }

    @Test
    @DisplayName("test external basecode with subdirectory")
    void testSubdirectoryGlobalBasecode() throws ExitException {
        String basecode = getBasePath("SubdirectoryBase");
        JPlagResult result = runJPlag("SubdirectoryDuplicate",
                it -> it.withSubdirectoryName("src").withBaseCodeSubmissionDirectory(new File(basecode)));
        verifySimpleSubdirectoryDuplicate(result, 3, 3);
    }

    @Test
    @DisplayName("test basecode in root folder with subdirectory")
    void testSubdirectoryLocalBasecode() throws ExitException {
        JPlagResult result = runJPlag("SubdirectoryDuplicate",
                it -> it.withSubdirectoryName("src").withBaseCodeSubmissionDirectory(new File(it.submissionDirectories().iterator().next(), "Base")));
        verifySimpleSubdirectoryDuplicate(result, 2, 1);
    }

    protected void verifySimpleSubdirectoryDuplicate(JPlagResult result, int submissions, int comparisons) {
        result.getSubmissions().getSubmissions().forEach(this::hasSubdirectoryRoot);
        hasSubdirectoryRoot(result.getSubmissions().getBaseCode());

        assertEquals(submissions, result.getNumberOfSubmissions());
        assertEquals(comparisons, result.getAllComparisons().size());
        assertEquals(1, result.getAllComparisons().get(0).matches().size());
        assertEquals(1, result.getSimilarityDistribution()[94]);
        assertEquals(0.9473, result.getAllComparisons().get(0).similarity(), DELTA);
    }

    private void hasSubdirectoryRoot(Submission submission) {
        assertEquals("src", submission.getRoot().getName());
    }
}
