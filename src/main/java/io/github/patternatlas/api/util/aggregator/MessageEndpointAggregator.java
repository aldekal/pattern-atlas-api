package io.github.patternatlas.api.util.aggregator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.github.patternatlas.api.entities.designmodel.AggregationData;
import io.github.patternatlas.api.entities.designmodel.ConcreteSolution;
import io.github.patternatlas.api.entities.designmodel.DesignModelPatternInstance;
import io.github.patternatlas.api.rest.model.FileDTO;

@AggregatorMetadata(sourceTypes = {"MessageEndpoint"}, targetTypes = {"", "AWS-CloudFormation-JSON", "ActiveMQ-XML", "ActiveMQ-Java"})
public class MessageEndpointAggregator extends ActiveMQAggregator {

    private static final String MIME_TYPE = "text/x-java";

    @Override
    public void aggregate(AggregationData aggregationData) {

        DesignModelPatternInstance sourcePattern = aggregationData.getSource();
        ConcreteSolution concreteSolution = sourcePattern.getConcreteSolution();
        String patternInstanceId = sourcePattern.getPatternInstanceId().toString();
        Map<String, Object> templateContext = aggregationData.getTemplateContext();

        String concreteSolutionTemplate = readFile(concreteSolution.getTemplateUri());
        concreteSolutionTemplate = extendVariables(concreteSolutionTemplate, patternInstanceId);

        List<String> aggregationType = Arrays.asList("ActiveMQ-XML", "ActiveMQ-Java");
        boolean isProducer = aggregationData.getTarget() == null || !aggregationType.contains(aggregationData.getTarget().getConcreteSolution().getAggregatorType());

        String filename = isProducer ? "QueueProducer.java" : "QueueConsumer.java";
        templateContext.put("producer", isProducer);

        addInputOutputChannelContext(aggregationData);

        String renderedTemplate = renderTemplate(concreteSolutionTemplate, templateContext);

        FileDTO aggregationResult = new FileDTO(filename, MIME_TYPE, renderedTemplate);
        aggregationData.setResult(aggregationResult);
    }
}
