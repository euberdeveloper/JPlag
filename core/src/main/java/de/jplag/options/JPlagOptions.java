package de.jplag.options;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jplag.JPlag;
import de.jplag.Language;
import de.jplag.clustering.ClusteringOptions;
import de.jplag.exceptions.BasecodeException;
import de.jplag.merging.MergingOptions;
import de.jplag.reporting.jsonfactory.serializer.LanguageSerializer;
import de.jplag.util.FileUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * This record defines the options to configure {@link JPlag}.
 * @param languages Languages to use when parsing the submissions.
 * @param minimumTokenMatch Tunes the comparison sensitivity by adjusting the minimum token required to be counted as
 * matching section. A smaller {@code <n>} increases the sensitivity but might lead to more false-positives.
 * @param submissionDirectories Directories with new submissions. These must be checked for plagiarism.
 * @param oldSubmissionDirectories Directories with old submissions to check against.
 * @param baseCodeSubmissionDirectory Directory containing the base code.
 * @param subdirectoryName Example: If the subdirectoryName is 'src', only the code inside submissionDir/src of each
 * submission will be used for comparison.
 * @param fileSuffixes List of file suffixes that should be included.
 * @param exclusionFileName Name of the file that contains the names of files to exclude from comparison.
 * @param similarityMetric The similarity metric determines how the minimum similarity threshold required for a
 * comparison (of two submissions) is calculated. This affects which comparisons are stored and thus make it into the
 * result object.
 * @param similarityThreshold Similarity value (must be between 0 and 1). Comparisons (of submissions pairs) with a
 * similarity below this threshold will be ignored. The default value of 0 allows all matches to be stored. This affects
 * which comparisons are stored and thus make it into the result object. See also {@link #similarityMetric()}.
 * @param maximumNumberOfComparisons The maximum number of comparisons that will be shown in the generated report. If
 * set to {@link #SHOW_ALL_COMPARISONS} all comparisons will be shown.
 * @param clusteringOptions Clustering options
 * @param debugParser If true, submissions that cannot be parsed will be stored in a separate directory.
 */
public record JPlagOptions(@JsonSerialize(using = LanguageSerializer.class) List<Language> languages,
        @JsonProperty("min_token_match") Integer minimumTokenMatch, @JsonProperty("submission_directories") Set<File> submissionDirectories,
        @JsonProperty("old_directories") Set<File> oldSubmissionDirectories, @JsonProperty("base_directory") File baseCodeSubmissionDirectory,
        @JsonProperty("subdirectory_name") String subdirectoryName, @JsonProperty("file_suffixes") List<String> fileSuffixes,
        @JsonProperty("exclusion_file_name") String exclusionFileName, @JsonProperty("similarity_metric") SimilarityMetric similarityMetric,
        @JsonProperty("similarity_threshold") double similarityThreshold, @JsonProperty("max_comparisons") int maximumNumberOfComparisons,
        @JsonProperty("cluster") ClusteringOptions clusteringOptions, boolean debugParser, @JsonProperty("merging") MergingOptions mergingOptions,
        @JsonProperty("normalize") boolean normalize) {

    public static final double DEFAULT_SIMILARITY_THRESHOLD = 0;
    public static final int DEFAULT_SHOWN_COMPARISONS = 500;
    public static final int SHOW_ALL_COMPARISONS = 0;
    public static final SimilarityMetric DEFAULT_SIMILARITY_METRIC = SimilarityMetric.AVG;
    public static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final String ERROR_FOLDER = "errors";

    private static final Logger logger = LoggerFactory.getLogger(JPlagOptions.class);

    public JPlagOptions(List<Language> languages, Integer minimumTokenMatch, Set<File> submissionDirectories, Set<File> oldSubmissionDirectories,
            File baseCodeSubmissionDirectory, String subdirectoryName, List<String> fileSuffixes, String exclusionFileName,
            SimilarityMetric similarityMetric, double similarityThreshold, int maximumNumberOfComparisons, ClusteringOptions clusteringOptions,
            boolean debugParser, MergingOptions mergingOptions, boolean normalize) {
        this.languages = languages;
        this.debugParser = debugParser;
        this.fileSuffixes = fileSuffixes == null || fileSuffixes.isEmpty() ? null : Collections.unmodifiableList(fileSuffixes);
        this.similarityThreshold = normalizeSimilarityThreshold(similarityThreshold);
        this.maximumNumberOfComparisons = normalizeMaximumNumberOfComparisons(maximumNumberOfComparisons);
        this.similarityMetric = similarityMetric;
        this.minimumTokenMatch = normalizeMinimumTokenMatch(minimumTokenMatch);
        this.exclusionFileName = exclusionFileName;
        this.submissionDirectories = submissionDirectories == null ? null : Collections.unmodifiableSet(submissionDirectories);
        this.oldSubmissionDirectories = oldSubmissionDirectories == null ? null : Collections.unmodifiableSet(oldSubmissionDirectories);
        this.baseCodeSubmissionDirectory = baseCodeSubmissionDirectory;
        this.subdirectoryName = subdirectoryName;
        this.clusteringOptions = clusteringOptions;
        this.mergingOptions = mergingOptions;
        this.normalize = normalize;
    }
    public JPlagOptions(Language language, Integer minimumTokenMatch, Set<File> submissionDirectories, Set<File> oldSubmissionDirectories,
                        File baseCodeSubmissionDirectory, String subdirectoryName, List<String> fileSuffixes, String exclusionFileName,
                        SimilarityMetric similarityMetric, double similarityThreshold, int maximumNumberOfComparisons, ClusteringOptions clusteringOptions,
                        boolean debugParser, MergingOptions mergingOptions, boolean normalize) {
        this(getLanguageListFromLanguage(language), minimumTokenMatch, submissionDirectories, oldSubmissionDirectories,
                baseCodeSubmissionDirectory, subdirectoryName, fileSuffixes, exclusionFileName,
                similarityMetric, similarityThreshold, maximumNumberOfComparisons, clusteringOptions,
                debugParser, mergingOptions, normalize
        );
    }
    public JPlagOptions(List<Language> languages, Set<File> submissionDirectories, Set<File> oldSubmissionDirectories) {
        this(languages, null, submissionDirectories, oldSubmissionDirectories, null, null, null, null, DEFAULT_SIMILARITY_METRIC,
                DEFAULT_SIMILARITY_THRESHOLD, DEFAULT_SHOWN_COMPARISONS, new ClusteringOptions(), false, new MergingOptions(), false);
    }
    public JPlagOptions(Language language, Set<File> submissionDirectories, Set<File> oldSubmissionDirectories) {
        this(getLanguageListFromLanguage(language), submissionDirectories, oldSubmissionDirectories);
    }

    public JPlagOptions withLanguageOption(List<Language> languages) {
        return new JPlagOptions(languages, minimumTokenMatch, submissionDirectories, oldSubmissionDirectories, baseCodeSubmissionDirectory,
                subdirectoryName, fileSuffixes, exclusionFileName, similarityMetric, similarityThreshold, maximumNumberOfComparisons,
                clusteringOptions, debugParser, mergingOptions, normalize);
    }
    public JPlagOptions withLanguageOption(Language language) {
        return withLanguageOption(getLanguageListFromLanguage(language));
    }

    public JPlagOptions withDebugParser(boolean debugParser) {
        return new JPlagOptions(languages, minimumTokenMatch, submissionDirectories, oldSubmissionDirectories, baseCodeSubmissionDirectory,
                subdirectoryName, fileSuffixes, exclusionFileName, similarityMetric, similarityThreshold, maximumNumberOfComparisons,
                clusteringOptions, debugParser, mergingOptions, normalize);
    }

    public JPlagOptions withFileSuffixes(List<String> fileSuffixes) {
        return new JPlagOptions(languages, minimumTokenMatch, submissionDirectories, oldSubmissionDirectories, baseCodeSubmissionDirectory,
                subdirectoryName, fileSuffixes, exclusionFileName, similarityMetric, similarityThreshold, maximumNumberOfComparisons,
                clusteringOptions, debugParser, mergingOptions, normalize);
    }

    public JPlagOptions withSimilarityThreshold(double similarityThreshold) {
        return new JPlagOptions(languages, minimumTokenMatch, submissionDirectories, oldSubmissionDirectories, baseCodeSubmissionDirectory,
                subdirectoryName, fileSuffixes, exclusionFileName, similarityMetric, similarityThreshold, maximumNumberOfComparisons,
                clusteringOptions, debugParser, mergingOptions, normalize);
    }

    public JPlagOptions withMaximumNumberOfComparisons(int maximumNumberOfComparisons) {
        return new JPlagOptions(languages, minimumTokenMatch, submissionDirectories, oldSubmissionDirectories, baseCodeSubmissionDirectory,
                subdirectoryName, fileSuffixes, exclusionFileName, similarityMetric, similarityThreshold, maximumNumberOfComparisons,
                clusteringOptions, debugParser, mergingOptions, normalize);
    }

    public JPlagOptions withSimilarityMetric(SimilarityMetric similarityMetric) {
        return new JPlagOptions(languages, minimumTokenMatch, submissionDirectories, oldSubmissionDirectories, baseCodeSubmissionDirectory,
                subdirectoryName, fileSuffixes, exclusionFileName, similarityMetric, similarityThreshold, maximumNumberOfComparisons,
                clusteringOptions, debugParser, mergingOptions, normalize);
    }

    public JPlagOptions withMinimumTokenMatch(Integer minimumTokenMatch) {
        return new JPlagOptions(languages, minimumTokenMatch, submissionDirectories, oldSubmissionDirectories, baseCodeSubmissionDirectory,
                subdirectoryName, fileSuffixes, exclusionFileName, similarityMetric, similarityThreshold, maximumNumberOfComparisons,
                clusteringOptions, debugParser, mergingOptions, normalize);
    }

    public JPlagOptions withExclusionFileName(String exclusionFileName) {
        return new JPlagOptions(languages, minimumTokenMatch, submissionDirectories, oldSubmissionDirectories, baseCodeSubmissionDirectory,
                subdirectoryName, fileSuffixes, exclusionFileName, similarityMetric, similarityThreshold, maximumNumberOfComparisons,
                clusteringOptions, debugParser, mergingOptions, normalize);
    }

    public JPlagOptions withSubmissionDirectories(Set<File> submissionDirectories) {
        return new JPlagOptions(languages, minimumTokenMatch, submissionDirectories, oldSubmissionDirectories, baseCodeSubmissionDirectory,
                subdirectoryName, fileSuffixes, exclusionFileName, similarityMetric, similarityThreshold, maximumNumberOfComparisons,
                clusteringOptions, debugParser, mergingOptions, normalize);
    }

    public JPlagOptions withOldSubmissionDirectories(Set<File> oldSubmissionDirectories) {
        return new JPlagOptions(languages, minimumTokenMatch, submissionDirectories, oldSubmissionDirectories, baseCodeSubmissionDirectory,
                subdirectoryName, fileSuffixes, exclusionFileName, similarityMetric, similarityThreshold, maximumNumberOfComparisons,
                clusteringOptions, debugParser, mergingOptions, normalize);
    }

    public JPlagOptions withBaseCodeSubmissionDirectory(File baseCodeSubmissionDirectory) {
        return new JPlagOptions(languages, minimumTokenMatch, submissionDirectories, oldSubmissionDirectories, baseCodeSubmissionDirectory,
                subdirectoryName, fileSuffixes, exclusionFileName, similarityMetric, similarityThreshold, maximumNumberOfComparisons,
                clusteringOptions, debugParser, mergingOptions, normalize);
    }

    public JPlagOptions withSubdirectoryName(String subdirectoryName) {
        return new JPlagOptions(languages, minimumTokenMatch, submissionDirectories, oldSubmissionDirectories, baseCodeSubmissionDirectory,
                subdirectoryName, fileSuffixes, exclusionFileName, similarityMetric, similarityThreshold, maximumNumberOfComparisons,
                clusteringOptions, debugParser, mergingOptions, normalize);
    }

    public JPlagOptions withClusteringOptions(ClusteringOptions clusteringOptions) {
        return new JPlagOptions(languages, minimumTokenMatch, submissionDirectories, oldSubmissionDirectories, baseCodeSubmissionDirectory,
                subdirectoryName, fileSuffixes, exclusionFileName, similarityMetric, similarityThreshold, maximumNumberOfComparisons,
                clusteringOptions, debugParser, mergingOptions, normalize);
    }

    public JPlagOptions withMergingOptions(MergingOptions mergingOptions) {
        return new JPlagOptions(languages, minimumTokenMatch, submissionDirectories, oldSubmissionDirectories, baseCodeSubmissionDirectory,
                subdirectoryName, fileSuffixes, exclusionFileName, similarityMetric, similarityThreshold, maximumNumberOfComparisons,
                clusteringOptions, debugParser, mergingOptions, normalize);
    }

    public JPlagOptions withNormalize(boolean normalize) {
        return new JPlagOptions(languages, minimumTokenMatch, submissionDirectories, oldSubmissionDirectories, baseCodeSubmissionDirectory,
                subdirectoryName, fileSuffixes, exclusionFileName, similarityMetric, similarityThreshold, maximumNumberOfComparisons,
                clusteringOptions, debugParser, mergingOptions, normalize);
    }

    public boolean hasBaseCode() {
        return baseCodeSubmissionDirectory != null;
    }

    public Set<String> excludedFiles() {
        return Optional.ofNullable(exclusionFileName()).map(this::readExclusionFile).orElse(Collections.emptySet());
    }

    @Override
    public List<String> fileSuffixes() {
        if ((fileSuffixes == null || fileSuffixes.isEmpty()) && !languages.isEmpty()) {
            Set<String> suffixes = new HashSet<>();
            for (Language language : languages) {
                List<String> languageSuffixes = Arrays.stream(language.suffixes()).toList();
                Objects.requireNonNull(languageSuffixes);
                suffixes.addAll(languageSuffixes);
            }
            return suffixes.stream().toList();
        }
        return fileSuffixes == null ? null : Collections.unmodifiableList(fileSuffixes);
    }

    @Override
    public Integer minimumTokenMatch() {
        if (minimumTokenMatch == null && !languages.isEmpty()) {
            return languages.stream()
                    .map(Language::minimumTokenMatch)
                    .min(Integer::compareTo)
                    .orElse(null);
        }
        return minimumTokenMatch;
    }

    private static List<Language> getLanguageListFromLanguage(Language language) {
        return Collections.singletonList(language);
    }

    private Set<String> readExclusionFile(final String exclusionFileName) {
        try (BufferedReader reader = FileUtils.openFileReader(new File(exclusionFileName))) {
            final var excludedFileNames = reader.lines().collect(Collectors.toSet());
            if (logger.isDebugEnabled()) {
                logger.debug("Excluded files:{}{}", System.lineSeparator(), String.join(System.lineSeparator(), excludedFileNames));
            }
            return excludedFileNames;
        } catch (IOException e) {
            logger.error("Could not read exclusion file: " + e.getMessage(), e);
            return Collections.emptySet();
        }
    }

    private static double normalizeSimilarityThreshold(double similarityThreshold) {
        if (similarityThreshold > 1) {
            logger.warn("Maximum threshold of 1 used instead of {}", similarityThreshold);
            return 1;
        }
        if (similarityThreshold < 0) {
            logger.warn("Minimum threshold of 0 used instead of {}", similarityThreshold);
            return 0;
        } else {
            return similarityThreshold;
        }
    }

    private Integer normalizeMaximumNumberOfComparisons(Integer maximumNumberOfComparisons) {
        return Math.max(maximumNumberOfComparisons, SHOW_ALL_COMPARISONS);
    }

    private Integer normalizeMinimumTokenMatch(Integer minimumTokenMatch) {
        return (minimumTokenMatch != null && minimumTokenMatch < 1) ? Integer.valueOf(1) : minimumTokenMatch;
    }

    /**
     * Creates new options to configure {@link JPlag}.
     * @param language Language to use when parsing the submissions.
     * @param minimumTokenMatch Tunes the comparison sensitivity by adjusting the minimum token required to be counted as
     * matching section. A smaller {@code <n>} increases the sensitivity but might lead to more false-positives.
     * @param submissionDirectory Directory with new submissions. These must be checked for plagiarism. To check more than
     * one submission directory, use the default initializer.
     * @param oldSubmissionDirectories Directories with old submissions to check against.
     * @param baseCodeSubmissionName Path name of the directory containing the base code.
     * @param subdirectoryName Example: If the subdirectoryName is 'src', only the code inside submissionDir/src of each
     * submission will be used for comparison.
     * @param fileSuffixes List of file suffixes that should be included.
     * @param exclusionFileName Name of the file that contains the names of files to exclude from comparison.
     * @param similarityMetric The similarity metric determines how the minimum similarity threshold required for a
     * comparison (of two submissions) is calculated. This affects which comparisons are stored and thus make it into the
     * result object.
     * @param similarityThreshold Percentage value (must be between 0 and 100). Comparisons (of submissions pairs) with a
     * similarity below this threshold will be ignored. The default value of 0 allows all matches to be stored. This affects
     * which comparisons are stored and thus make it into the result object. See also {@link #similarityMetric()}.
     * @param maximumNumberOfComparisons The maximum number of comparisons that will be shown in the generated report. If
     * set to {@link #SHOW_ALL_COMPARISONS} all comparisons will be shown.
     * @param clusteringOptions Clustering options
     * @param debugParser If true, submissions that cannot be parsed will be stored in a separate directory.
     * @deprecated Use the default initializer with @{{@link #baseCodeSubmissionDirectory} instead.
     */
    @Deprecated(since = "4.0.0", forRemoval = true)
    public JPlagOptions(Language language, Integer minimumTokenMatch, File submissionDirectory, Set<File> oldSubmissionDirectories,
            String baseCodeSubmissionName, String subdirectoryName, List<String> fileSuffixes, String exclusionFileName,
            SimilarityMetric similarityMetric, double similarityThreshold, int maximumNumberOfComparisons, ClusteringOptions clusteringOptions,
            boolean debugParser, MergingOptions mergingOptions) throws BasecodeException {
        this(getLanguageListFromLanguage(language), minimumTokenMatch, Set.of(submissionDirectory), oldSubmissionDirectories,
                convertLegacyBaseCodeToFile(baseCodeSubmissionName, submissionDirectory), subdirectoryName, fileSuffixes, exclusionFileName,
                similarityMetric, similarityThreshold, maximumNumberOfComparisons, clusteringOptions, debugParser, mergingOptions, false);
    }
    @Deprecated(since = "4.0.0", forRemoval = true)
    public JPlagOptions(List<Language> languages, Integer minimumTokenMatch, File submissionDirectory, Set<File> oldSubmissionDirectories,
                        String baseCodeSubmissionName, String subdirectoryName, List<String> fileSuffixes, String exclusionFileName,
                        SimilarityMetric similarityMetric, double similarityThreshold, int maximumNumberOfComparisons, ClusteringOptions clusteringOptions,
                        boolean debugParser, MergingOptions mergingOptions) throws BasecodeException {
        this(languages, minimumTokenMatch, Set.of(submissionDirectory), oldSubmissionDirectories,
                convertLegacyBaseCodeToFile(baseCodeSubmissionName, submissionDirectory), subdirectoryName, fileSuffixes, exclusionFileName,
                similarityMetric, similarityThreshold, maximumNumberOfComparisons, clusteringOptions, debugParser, mergingOptions, false);
    }

    /**
     * Creates a new options instance with the provided base code submission name
     * @param baseCodeSubmissionName the path or name of the base code submission
     * @return a new options instance with the provided base code submission name
     * @deprecated Use @{{@link #withBaseCodeSubmissionDirectory} instead.
     */
    @Deprecated(since = "4.0.0", forRemoval = true)
    public JPlagOptions withBaseCodeSubmissionName(String baseCodeSubmissionName) {
        File baseCodeDirectory = new File(baseCodeSubmissionName);
        if (baseCodeDirectory.exists()) {
            return this.withBaseCodeSubmissionDirectory(baseCodeDirectory);
        }

        if (submissionDirectories.size() != 1) {
            throw new IllegalArgumentException("Partial path based base code requires exactly one submission directory");
        }
        File submissionDirectory = submissionDirectories.iterator().next();
        try {
            return new JPlagOptions(languages, minimumTokenMatch, submissionDirectory, oldSubmissionDirectories, baseCodeSubmissionName,
                    subdirectoryName, fileSuffixes, exclusionFileName, similarityMetric, similarityThreshold, maximumNumberOfComparisons,
                    clusteringOptions, debugParser, mergingOptions);
        } catch (BasecodeException e) {
            throw new IllegalArgumentException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Converts a legacy base code submission name to a directory path.
     * @deprecated Use the default initializer with @{{@link #baseCodeSubmissionDirectory} instead.
     */
    @Deprecated(since = "4.0.0", forRemoval = true)
    private static File convertLegacyBaseCodeToFile(String baseCodeSubmissionName, File submissionDirectory) throws BasecodeException {
        if (baseCodeSubmissionName == null) {
            return null;
        }
        File baseCodeAsAbsolutePath = new File(baseCodeSubmissionName);
        if (baseCodeAsAbsolutePath.exists()) {
            return baseCodeAsAbsolutePath;
        }
        String normalizedName = baseCodeSubmissionName;
        while (normalizedName.startsWith(File.separator)) {
            normalizedName = normalizedName.substring(1);
        }
        while (normalizedName.endsWith(File.separator)) {
            normalizedName = normalizedName.substring(0, normalizedName.length() - 1);
        }
        if (normalizedName.isEmpty() || normalizedName.contains(File.separator) || normalizedName.contains(".")) {
            throw new BasecodeException(
                    "The basecode directory name \"" + normalizedName + "\" cannot contain dots! Please migrate to the path-based API.");
        }
        return new File(submissionDirectory, baseCodeSubmissionName);
    }

    public boolean languagesSupportNormalization() {
        return languages.stream().allMatch(Language::supportsNormalization);
    }
    public boolean languagesRequireCoreNormalization() {
        return languages.stream().anyMatch(Language::requiresCoreNormalization);
    }
    public boolean languagesExpectSubmissionOrder() {
        return languages.stream().anyMatch(Language::expectsSubmissionOrder);
    }
    public List<File> languagesCustomizeSubmissionOrder(List<File> rootFiles) {
        // TODO: filter for file extension and order only the files matching that specific language
        List<File> result = rootFiles.stream().toList();
        for (Language language : languages) {
            if (language.expectsSubmissionOrder()) {
                result = language.customizeSubmissionOrder(result);
            }
        }
        return result;
    }
    public int languagesMinTokenMatch() {
        return languages.stream().map(Language::minimumTokenMatch).min(Integer::compareTo).orElse(minimumTokenMatch);
    }
    public String languagesNames() {
        List<String> languageNames = languages.stream().map(Language::getName).toList();
        return String.join(", ", languageNames);
    }

}
