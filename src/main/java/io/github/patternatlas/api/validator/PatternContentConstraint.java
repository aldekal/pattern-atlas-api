package io.github.patternatlas.api.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = PatternContentValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PatternContentConstraint {
    String message() default "Pattern content does not comply with PatternSchema";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
