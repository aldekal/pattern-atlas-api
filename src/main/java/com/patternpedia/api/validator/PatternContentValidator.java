package com.patternpedia.api.validator;

import com.patternpedia.api.entities.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PatternContentValidator implements ConstraintValidator<PatternContentConstraint, Pattern> {

    @Override
    public void initialize(PatternContentConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(Pattern pattern, ConstraintValidatorContext constraintValidatorContext) {
        // Todo: Implement PatternContentValidator.isValid
        //  Check if pattern.getContent() complies with pattern.getPatternSchema()
        return true;
    }


}
