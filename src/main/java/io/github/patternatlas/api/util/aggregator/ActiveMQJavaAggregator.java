package io.github.patternatlas.api.util.aggregator;

import java.util.Collections;

import io.github.patternatlas.api.entities.designmodel.AggregationData;
import io.github.patternatlas.api.entities.designmodel.ConcreteSolution;
import io.github.patternatlas.api.entities.designmodel.DesignModelPatternInstance;
import io.github.patternatlas.api.rest.model.FileDTO;

@AggregatorMetadata(sourceTypes = {"ActiveMQ-Java"}, targetTypes = {"", "ActiveMQ-Java", "ActiveMQ-XML"})
public class ActiveMQJavaAggregator extends ActiveMQAggregator {

    private static final String FILENAME = "PatternAtlasRouteBuilder.java";
    private static final String MIME_TYPE = "text/x-java";
    private static final String WRAPPER_TEMPLATE = CONCRETE_SOLUTION_REPO + "eip-activemq-java/camel.st";
    private static final String TEMPLATE_KEY = "-template-jdsl";

    @Override
    public void aggregate(AggregationData aggregationData) {

        StringBuilder camelContext = new StringBuilder();

        DesignModelPatternInstance sourcePattern = aggregationData.getSource();
        ConcreteSolution concreteSolution = sourcePattern.getConcreteSolution();
        String patternInstanceId = sourcePattern.getPatternInstanceId().toString();
        String targetInstanceId = aggregationData.getTarget().getPatternInstanceId().toString();

        camelContext.append(aggregationData.getTemplateContext().getOrDefault(patternInstanceId + TEMPLATE_KEY, ""));

        String concreteSolutionTemplate = readFile(concreteSolution.getTemplateUri());

        String idComment = "/* " + getIdentifier(aggregationData.getSource()) + " */";
        if (!camelContext.toString().contains(idComment)) {
            camelContext.insert(0, "\n" + idComment + extendVariables(concreteSolutionTemplate, patternInstanceId) + "\n");
        }

        aggregationData.getTemplateContext().put(targetInstanceId + TEMPLATE_KEY, camelContext.toString());

        if (aggregationData.getTarget() != null) {

            addInputOutputChannelContext(aggregationData);

            if ("ActiveMQ-Java".equals(aggregationData.getTarget().getConcreteSolution().getAggregatorType())) {
                return;
            }
        }

        // Render template and wrap into camel context
        String renderedCamelContext = renderTemplate(camelContext.toString(), aggregationData.getTemplateContext());

        String wrapperTemplate = readFile(WRAPPER_TEMPLATE);
        String camelConfig = renderTemplate(wrapperTemplate, Collections.singletonMap("camelContext", renderedCamelContext));

        aggregationData.getTemplateContext().put(targetInstanceId + TEMPLATE_KEY, camelContext.toString());

        FileDTO aggregationResult = new FileDTO(FILENAME, MIME_TYPE, camelConfig);
        aggregationData.setResult(aggregationResult);
    }
}
