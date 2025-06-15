package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Includes/Excludes pattern filter.
 */
@Getter
public class PatternFilter implements Filter<String> {
    /**
     * include patterns
     */
    private final List<Pattern> includes;
    /**
     * exclude patterns
     */
    private final List<Pattern> excludes;

    /**
     * Default constructor with accepts any behaviour.
     */
    public PatternFilter() {
        //initialize with array list for org.springframework.boot.context.properties.ConfigurationProperties compatibility.
        this(new ArrayList<>(List.of(Pattern.compile(".*"))), new ArrayList<>());
    }

    /**
     * Initialize the {@link #includes} and {@link #excludes} with the arguments.
     *
     * @param includes the includes patterns to use.
     * @param excludes the excludes patterns to use.
     */
    public PatternFilter(@NotNull List<Pattern> includes, @NotNull List<Pattern> excludes) {
        this.includes = includes;
        this.excludes = excludes;
    }

    /**
     * Evaluates if the value is eligible.
     *
     * @param value the value to accept.
     * @return {@code true} when the value is eligible otherwise {@code false}.
     */
    @Override
    public boolean accept(String value) {
        return accept(value, includes) && !accept(value, excludes);
    }

    /**
     * Evaluates if the value is eligible against a list of patterns.
     *
     * @param value    the bean name
     * @param patterns the pattern list to evaluate against
     * @return @return {@code true} when the value is eligible to the patterns otherwise {@code false}.
     */
    private boolean accept(String value, List<Pattern> patterns) {
        Optional<Boolean> reduce = patterns.stream()
                .map(x -> x.matcher(value).find())
                .reduce((x, y) -> x || y);
        return reduce.isPresent() && reduce.get();
    }
}
