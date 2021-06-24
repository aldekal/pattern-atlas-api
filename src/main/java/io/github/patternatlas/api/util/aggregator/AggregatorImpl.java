package io.github.patternatlas.api.util.aggregator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.stringtemplate.v4.ST;

import io.github.patternatlas.api.entities.designmodel.DesignModelPatternInstance;
import io.github.patternatlas.api.exception.AggregationException;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public abstract class AggregatorImpl implements Aggregator {

    protected static final String CONCRETE_SOLUTION_REPO = "https://raw.githubusercontent.com/PatternAtlas/pattern-atlas-pattern-implementations/main/";

    protected static final Random RANDOM = new Random();

    protected static String readFile(String url) {
        log.info("Read file from " + url);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            String errorMsg = "Failed to read file from " + url;
            log.error(errorMsg);
            throw new AggregationException(errorMsg);
        }
    }

    protected static String renderTemplate(String concreteSolutionTemplate, Map<String, Object> dataContainer) {
        ST template = new ST(concreteSolutionTemplate, '$', '$');

        template.add("random", RANDOM.nextInt(Integer.MAX_VALUE));

        for (Map.Entry<String, Object> entry : dataContainer.entrySet()) {
            template.add(entry.getKey(), entry.getValue());
        }

        return template.render();
    }

    protected static String extendVariables(String template, String id) {
        return template.replaceAll("\\$(.*?)(input|output|configuration)(.*?)(\\$|:\\{)", "\\$$1" + id + "-$2$3$4");
    }

    protected static String getIdentifier(DesignModelPatternInstance patternInstance) {
        return patternInstance.getPattern().getName().replace(" ", "-").toLowerCase() + "-" + patternInstance.getPatternInstanceId().toString();
    }
}
