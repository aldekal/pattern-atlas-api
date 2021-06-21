package io.github.patternatlas.api.util.aggregator;

import java.util.Collections;

import io.github.patternatlas.api.entities.designmodel.AggregationData;
import io.github.patternatlas.api.entities.designmodel.ConcreteSolution;
import io.github.patternatlas.api.entities.designmodel.DesignModelPatternInstance;
import io.github.patternatlas.api.rest.model.FileDTO;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@AggregatorMetadata(sourceTypes = {"ActiveMQ-XML"}, targetTypes = {"", "ActiveMQ-XML", "ActiveMQ-Java", "AWS-CloudFormation-JSON", "MessageEndpoint"})
public class ActiveMQXMLAggregator extends ActiveMQAggregator {

    private static final String FILENAME = "camel.xml";
    private static final String MIME_TYPE = "text/xml";
    private static final String WRAPPER_TEMPLATE = CONCRETE_SOLUTION_REPO + "eip-activemq-xml/camel.st";
    private static final String TEMPLATE_KEY = "-template";

    @Override
    public void aggregate(AggregationData aggregationData) {

        StringBuilder camelContext = new StringBuilder();

        DesignModelPatternInstance sourcePattern = aggregationData.getSource();
        ConcreteSolution concreteSolution = sourcePattern.getConcreteSolution();
        String patternInstanceId = sourcePattern.getPatternInstanceId().toString();
        String targetInstanceId = aggregationData.getTarget() == null ? null : aggregationData.getTarget().getPatternInstanceId().toString();

        camelContext.append(aggregationData.getTemplateContext().getOrDefault(patternInstanceId + TEMPLATE_KEY, ""));

        String concreteSolutionTemplate = readFile(concreteSolution.getTemplateUri());

        String idComment = "<!-- " + getIdentifier(sourcePattern) + " -->";
        if (!camelContext.toString().contains(idComment)) {
            camelContext.insert(0, idComment + "\n" + extendVariables(concreteSolutionTemplate, patternInstanceId) + "\n");
        }

        if (targetInstanceId != null) {
            aggregationData.getTemplateContext().put(targetInstanceId + TEMPLATE_KEY, camelContext.toString());
        }

        if (aggregationData.getTarget() != null) {
            addInputOutputChannelContext(aggregationData);

            if ("ActiveMQ-XML".equals(aggregationData.getTarget().getConcreteSolution().getAggregatorType())) {
                return;
            }
        }

        // Render template and wrap into camel context
        String renderedCamelContext = renderTemplate(camelContext.toString(), aggregationData.getTemplateContext());

        if (aggregationData.getTarget() != null && "AWS-CloudFormation-JSON".equals(aggregationData.getTarget().getConcreteSolution().getAggregatorType())) {
            String id = aggregationData.getTarget().getPatternInstanceId().toString();
            aggregationData.getTemplateContext().put(id + "-configuration", renderedCamelContext);
            return;
        }

        String wrapperTemplate = readFile(WRAPPER_TEMPLATE);
        String camelConfig = renderTemplate(wrapperTemplate, Collections.singletonMap("camelContext", renderedCamelContext));

        if (targetInstanceId != null) {
            aggregationData.getTemplateContext().put(targetInstanceId + TEMPLATE_KEY, camelConfig);
        }

        FileDTO aggregationResult = new FileDTO(FILENAME, MIME_TYPE, camelConfig);
        aggregationData.setResult(aggregationResult);
    }
}
