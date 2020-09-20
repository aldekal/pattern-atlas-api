package com.patternpedia.api.util.aggregator;

import com.patternpedia.api.entities.designmodel.AggregationData;
import com.patternpedia.api.entities.designmodel.ConcreteSolution;
import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;
import com.patternpedia.api.rest.model.FileDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@AggregatorMetadata(sourceTypes = {"AWS-CloudFormation-JSON"}, targetTypes = {"", "AWS-CloudFormation-JSON"})
public class AWSCloudFormationJsonAggregator extends ActiveMQAggregator {

    private static final String FILENAME = "CloudFormation-Template.json";
    private static final String MIME_TYPE = "application/json";
    private static final String WRAPPER_TEMPLATE = "file:///home/marcel/Dokumente/Studium Softwaretechnik/Vorlesungen/14. Semester/Masterthesis/Pattern Atlas/concrete-solutions/aws-cloudformation-json/cloudformation.st";


    @Override
    public void aggregate(AggregationData aggregationData) {


        DesignModelPatternInstance sourcePattern = aggregationData.getSource();
        ConcreteSolution concreteSolution = sourcePattern.getConcreteSolution();
        String patternInstanceId = sourcePattern.getPatternInstanceId().toString();
        Map<String, Object> templateContext = aggregationData.getTemplateContext();

        String concreteSolutionTemplate = readFile(concreteSolution.getTemplateRef());

        String cloudFormationTemplate = extendVariables(concreteSolutionTemplate, patternInstanceId) + "\n";

        // Make possible Camel XML JSON string compatible
        if (templateContext.containsKey(patternInstanceId + "-configuration")) {
            templateContext.compute(
                    patternInstanceId + "-configuration",
                    (String key, Object value) -> ((String) value).replaceAll("[\r\n\t ]+", " ").replaceAll("\"", "\\\\\"")
            );
        }

        if (aggregationData.getEdge() != null) {
            addInputOutputChannelContext(aggregationData);
        }

        // Render template and wrap into camel context
        String renderedCloudFormationTemplate = renderTemplate(cloudFormationTemplate, templateContext);

        if (renderedCloudFormationTemplate != null && !renderedCloudFormationTemplate.trim().isEmpty()) {
            templateContext.compute("resources", (String key, Object value) -> {
                List resources = new ArrayList<>();
                if (value != null) {
                    resources.addAll((Collection) value);
                }
                resources.add(renderedCloudFormationTemplate);
                return resources;
            });
        }

        if (aggregationData.getTarget() != null && "AWS-CloudFormation-JSON".equals(aggregationData.getTarget().getConcreteSolution().getAggregatorType())) {
            return;
        }

        String wrapperTemplate = readFile(WRAPPER_TEMPLATE);
        String completeTemplate = renderTemplate(wrapperTemplate, templateContext);

        FileDTO aggregationResult = new FileDTO(FILENAME, MIME_TYPE, completeTemplate);
        aggregationData.setResult(aggregationResult);
    }
}
