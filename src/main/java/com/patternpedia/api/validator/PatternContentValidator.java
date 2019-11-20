package com.patternpedia.api.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternSchema;
import com.patternpedia.api.entities.PatternSectionSchema;
import com.patternpedia.api.service.PatternLanguageService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Iterator;
import java.util.Map;

public class PatternContentValidator implements ConstraintValidator<PatternContentConstraint, Pattern> {

    private ObjectMapper objectMapper;
    private PatternLanguageService patternLanguageService;

    public PatternContentValidator(ObjectMapper objectMapper,
                                   PatternLanguageService patternLanguageService) {
        this.objectMapper = objectMapper;
        this.patternLanguageService = patternLanguageService;
    }

    @Override
    public void initialize(PatternContentConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(Pattern pattern, ConstraintValidatorContext constraintValidatorContext) {
        if (null == pattern.getPatternLanguage()) {
            return false;
        }

        PatternSchema patternSchema = this.patternLanguageService.getPatternSchemaByPatternLanguageId(pattern.getPatternLanguage().getId());

        ObjectNode content = this.objectMapper.convertValue(pattern.getContent(), ObjectNode.class);

        // 1. Check if all content fields are valid
        for (Iterator<Map.Entry<String, JsonNode>> it = content.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            if (!isAllowedContentField(entry, patternSchema)) {
                return false;
            }
        }

        // 2. if true, check if there ar fields missing that are defined by schema!
        for (PatternSectionSchema patternSectionSchema : patternSchema.getPatternSectionSchemas()) {
            if (!isSectionPresentInContent(patternSectionSchema, content)) {
                return false;
            }
        }

        return true;
    }

    private boolean isSectionPresentInContent(PatternSectionSchema patternSectionSchema, ObjectNode content) {
        for (Iterator<Map.Entry<String, JsonNode>> it = content.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> contentEntry = it.next();
            if (patternSectionSchema.getName().equals(contentEntry.getKey())) {
                return true;
            }
        }
        return false;
    }

    private boolean isAllowedContentField(Map.Entry<String, JsonNode> contentEntry, PatternSchema patternSchema) {
        for (PatternSectionSchema patternSectionSchema : patternSchema.getPatternSectionSchemas()) {
            if (patternSectionSchema.getName().equals(contentEntry.getKey())) {
                // Todo: Here we have to check if the type of the contentEntry is correct with respect to the type of patternSectionSchema.getType
                // At the moment we accept to find the key
                return true;
            }
        }
        return false;
    }

}
