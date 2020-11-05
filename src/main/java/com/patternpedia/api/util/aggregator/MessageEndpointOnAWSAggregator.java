package com.patternpedia.api.util.aggregator;

import com.patternpedia.api.entities.designmodel.AggregationData;
import com.patternpedia.api.rest.model.FileDTO;


@AggregatorMetadata(sourceTypes = {"MessageEndpoint"}, targetTypes = {"AWS-CloudFormation-JSON"})
public class MessageEndpointOnAWSAggregator extends ActiveMQAggregator {

    @Override
    public void aggregate(AggregationData aggregationData) {

        final String[] instructions = {
                "Please perform the following steps for running your Messaging Endpoint on AWS:",
                "1. upload your artefact on Amazon S3",
                "2. replace the S3Bucket and S3Key values in the Cloud Formation template"
        };

        FileDTO aggregationResult = new FileDTO("Instructions for Message Endpoint deployment on AWS.txt", "plain/text", String.join("\n", instructions));
        aggregationData.setResult(aggregationResult);
    }
}
