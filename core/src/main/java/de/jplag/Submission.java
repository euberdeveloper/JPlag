package de.jplag;

import static de.jplag.SubmissionState.CANNOT_PARSE;
import static de.jplag.SubmissionState.NOTHING_TO_PARSE;
import static de.jplag.SubmissionState.TOO_SMALL;
import static de.jplag.SubmissionState.UNPARSED;
import static de.jplag.SubmissionState.VALID;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jplag.normalization.TokenStringNormalizer;
import de.jplag.options.JPlagOptions;

/**
 * This class represents a single submission, which can either be a single file or a directory containing multiple
 * files. It encapsulates the details and processing logic required to handle the submission files, including parsing,
 * tokenization, and normalization.
 */
public class Submission implements Comparable<Submission> {
    private static final Logger logger = LoggerFactory.getLogger(Submission.class);

    private final String name; // identifier for the submission (a directory or file name).
    private final File submissionRootFile; // Root of the submission, a director or file (including the subdir if used).
    private final boolean isNew; // old submissions are only checked against new ones.
    private final Collection<File> files;
    private final Language language;

    private SubmissionState state; // whether an error occurred during parsing or not
    private List<Token> tokenList; // list of tokens from all files, used for comparison
    private JPlagComparison baseCodeComparison; // Comparison of thus submission with the base code
    private Map<File, Integer> fileTokenCount;

    /**
     * Creates a submission.
     * @param name Identification of the submission (directory or filename).
     * @param submissionRootFile is the submission file, or the root of the submission itself.
     * @param isNew states whether the submission must be checked for plagiarism.
     * @param files are the files of the submissions, if the root is a single file it should just contain one file.
     * @param language is the language of the submission.
     */
    public Submission(String name, File submissionRootFile, boolean isNew, Collection<File> files, Language language) {
        this.name = name;
        this.submissionRootFile = submissionRootFile;
        this.isNew = isNew;
        this.files = files;
        this.language = language;
        tokenList = Collections.emptyList(); // Placeholder, will be replaced when submission is parsed
        state = UNPARSED;
    }

    @Override
    public int compareTo(Submission other) {
        return name.compareTo(other.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Submission otherSubmission)) {
            return false;
        }
        return otherSubmission.getName().equals(name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * @return base code comparison
     */
    public JPlagComparison getBaseCodeComparison() {
        return baseCodeComparison;
    }

    /**
     * @return a list of files this submission consists of.
     */
    public Collection<File> getFiles() {
        return files;
    }

    /**
     * @return name of the submission (directory or file name).
     */
    public String getName() {
        return name;
    }

    /**
     * @return Number of tokens in the parse result.
     */
    public int getNumberOfTokens() {
        return tokenList.size();
    }

    /**
     * @return the unique file of the submission, which is either in a root folder or a subfolder of root folder when the
     * subdirectory option is used.
     */
    public File getRoot() {
        return submissionRootFile;
    }

    /**
     * @param subtractBaseCode If true subtract basecode matches if possible.
     * @return Similarity divisor for the submission.
     */
    int getSimilarityDivisor(boolean subtractBaseCode) {
        int divisor = getNumberOfTokens() - getFiles().size();
        if (subtractBaseCode && baseCodeComparison != null) {
            divisor -= baseCodeComparison.getNumberOfMatchedTokens();
        }
        return divisor;
    }

    /**
     * @return unmodifiable list of tokens generated by parsing the submission.
     */
    public List<Token> getTokenList() {
        return tokenList;
    }

    /**
     * @return Whether a comparison between the submission and the base code is available.
     */
    public boolean hasBaseCodeMatches() {
        return baseCodeComparison != null;
    }

    /**
     * @return the state of the submissions, indicating whether it is valid or has errors.
     */
    public SubmissionState getState() {
        return state;
    }

    /**
     * @return whether the submission is new, That is, must be checked for plagiarism.
     */
    public boolean isNew() {
        return isNew;
    }

    /**
     * Sets the base code comparison
     * @param baseCodeComparison is submissions matches with the base code
     */
    public void setBaseCodeComparison(JPlagComparison baseCodeComparison) {
        this.baseCodeComparison = baseCodeComparison;
    }

    /**
     * Sets the tokens that have been parsed from the files this submission consists of.
     * @param tokenList is the list of these tokens.
     */
    public void setTokenList(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    /**
     * String representation of the code files contained in this submission, annotated with all tokens.
     * @return the annotated code as string.
     */
    public String getTokenAnnotatedSourceCode() {
        return TokenPrinter.printTokens(tokenList, submissionRootFile);
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * This method is used to copy files that can not be parsed to a special folder.
     */
    private void copySubmission() {
        File errorDirectory = createErrorDirectory(language.getIdentifier(), name);
        logger.info("Copying erroneous submission to {}", errorDirectory.getAbsolutePath());
        for (File file : files) {
            try {
                Files.copy(file.toPath(), new File(errorDirectory, file.getName()).toPath());
            } catch (IOException exception) {
                logger.error("Error copying file: " + exception.getMessage(), exception);
            }
        }
    }

    private static File createErrorDirectory(String... subdirectoryNames) {
        File subdirectory = Path.of(JPlagOptions.ERROR_FOLDER, subdirectoryNames).toFile();
        if (!subdirectory.exists()) {
            subdirectory.mkdirs();
        }
        return subdirectory;
    }

    /**
     * Parse files of the submission.
     * @param debugParser specifies if the submission should be copied upon parsing errors.
     * @param normalize specifies if the tokens sequences should be normalized.
     * @param minimalTokens specifies the minimum number of tokens required of a valid submission.
     * @return Whether parsing was successful.
     */
    /* package-private */ boolean parse(boolean debugParser, boolean normalize, int minimalTokens) {
        if (files == null || files.isEmpty()) {
            logger.error("Nothing to parse for submission \"{}\"", name);
            state = NOTHING_TO_PARSE;
            return false;
        }

        try {
            tokenList = language.parse(new HashSet<>(files), normalize);
        } catch (ParsingException e) {
            String shortenedMessage = e.getMessage().replace(submissionRootFile.toString(), name);
            logger.warn("Failed to parse submission {}:{}{}", name, System.lineSeparator(), shortenedMessage);
            state = CANNOT_PARSE;
            if (debugParser) {
                copySubmission();
            }
            return false;
        }

        if (tokenList.size() < minimalTokens) {
            logger.error("Submission {} contains {} tokens, which below the minimum match length {}!", name, tokenList.size(), minimalTokens);
            state = TOO_SMALL;
            return false;
        }

        tokenList = Collections.unmodifiableList(tokenList);
        state = VALID;
        return true;
    }

    /**
     * Perform token sequence normalization, which makes the token sequence invariant to dead code insertion and independent
     * statement reordering.
     */
    void normalize() {
        List<Integer> originalOrder = getOrder(tokenList);
        tokenList = TokenStringNormalizer.normalize(tokenList);
        List<Integer> normalizedOrder = getOrder(tokenList);

        logger.debug("original line order: {}", originalOrder);
        logger.debug("line order after normalization: {}", normalizedOrder);
        Set<Integer> normalizedSet = new HashSet<>(normalizedOrder);
        List<Integer> removed = originalOrder.stream().filter(l -> !normalizedSet.contains(l)).toList();
        logger.debug("removed {} line(s): {}", removed.size(), removed);
    }

    private List<Integer> getOrder(List<Token> tokenList) {
        List<Integer> order = new ArrayList<>(tokenList.size());  // a little too big
        int currentLineNumber = tokenList.get(0).getLine();
        order.add(currentLineNumber);
        for (Token token : tokenList) {
            if (token.getLine() != currentLineNumber) {
                currentLineNumber = token.getLine();
                order.add(currentLineNumber);
            }
        }
        return order;
    }

    /**
     * @return Submission containing shallow copies of its fields.
     */
    public Submission copy() {
        Submission copy = new Submission(name, submissionRootFile, isNew, files, language);
        copy.setTokenList(new ArrayList<>(tokenList));
        copy.setBaseCodeComparison(baseCodeComparison);
        copy.state = state;
        return copy;
    }

    /**
     * @return A mapping of each file in the submission to the number of tokens in the file
     */
    public Map<File, Integer> getTokenCountPerFile() {
        if (this.tokenList == null) {
            return Collections.emptyMap();
        }

        if (fileTokenCount == null) {
            fileTokenCount = new HashMap<>();
            for (File file : this.files) {
                fileTokenCount.put(file, 0);
            }
            for (Token token : this.tokenList) {
                fileTokenCount.put(token.getFile(), fileTokenCount.get(token.getFile()) + 1);
            }
        }
        return fileTokenCount;
    }
}
