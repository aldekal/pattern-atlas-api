package com.patternpedia.api.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PatternContentValidator.class)
@Target( { ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface PatternContentConstraint {
    String message() default "Pattern content does not comply with PatternSchema";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
