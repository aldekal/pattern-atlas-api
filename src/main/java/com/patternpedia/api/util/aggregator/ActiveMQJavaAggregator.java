package com.patternpedia.api.util.aggregator;

import com.patternpedia.api.entities.designmodel.AggregationData;
import com.patternpedia.api.entities.designmodel.ConcreteSolution;
import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;
import com.patternpedia.api.rest.model.FileDTO;

import java.util.Collections;


@AggregatorMetadata(sourceTypes = {"ActiveMQ-Java"}, targetTypes = {"", "ActiveMQ-Java", "ActiveMQ-XML"})
public class ActiveMQJavaAggregator extends ActiveMQAggregator {

    private static final String FILENAME = "PatternAtlasRouteBuilder.java";
    private static final String MIME_TYPE = "text/x-java";
    private static final String WRAPPER_TEMPLATE = "file:///home/marcel/Dokumente/Studium Softwaretechnik/Vorlesungen/14. Semester/Masterthesis/Pattern Atlas/concrete-solutions/eip-activemq-java/camel.st";


    @Override
    public void aggregate(AggregationData aggregationData) {

        StringBuilder camelContext = new StringBuilder();

        DesignModelPatternInstance sourcePattern = aggregationData.getSource();
        ConcreteSolution concreteSolution = sourcePattern.getConcreteSolution();
        String patternInstanceId = sourcePattern.getPatternInstanceId().toString();
        String targetInstanceId = aggregationData.getTarget().getPatternInstanceId().toString();

        camelContext.append(aggregationData.getTemplateContext().getOrDefault(patternInstanceId + "-template", ""));

        String concreteSolutionTemplate = readFile(concreteSolution.getTemplateRef());

        String idComment = "/* " + getIdentifier(aggregationData.getSource()) + " */";
        if (!camelContext.toString().contains(idComment)) {
            camelContext.insert(0, "\n" + idComment + extendVariables(concreteSolutionTemplate, patternInstanceId) + "\n");
        }

        aggregationData.getTemplateContext().put(targetInstanceId + "-template", camelContext.toString());

        if (aggregationData.getEdge() != null) {

            addInputOutputChannelContext(aggregationData);

            if ("ActiveMQ-Java".equals(aggregationData.getTarget().getConcreteSolution().getAggregatorType())) {
                return;
            }
        }


        // Render template and wrap into camel context
        String renderedCamelContext = renderTemplate(camelContext.toString(), aggregationData.getTemplateContext());

        String wrapperTemplate = readFile(WRAPPER_TEMPLATE);
        String camelConfig = renderTemplate(wrapperTemplate, Collections.singletonMap("camelContext", renderedCamelContext));

        aggregationData.getTemplateContext().put(targetInstanceId + "-template", camelContext.toString());

        FileDTO aggregationResult = new FileDTO(FILENAME, MIME_TYPE, camelConfig);
        aggregationData.setResult(aggregationResult);
    }
}
