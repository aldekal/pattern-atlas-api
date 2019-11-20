package com.patternpedia.api.validator;

import com.patternpedia.api.entities.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ValidatorTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    LocalValidatorFactoryBean validator;

    @Before
    public void setup() {

        SpringConstraintValidatorFactory springConstraintValidatorFactory
                = new SpringConstraintValidatorFactory(webApplicationContext.getAutowireCapableBeanFactory());
        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(springConstraintValidatorFactory);
        validator.setApplicationContext(webApplicationContext);
        validator.afterPropertiesSet();
    }

    @Test
    public void should_have_violations() {

        Pattern pattern = new Pattern();

        Set<ConstraintViolation<Pattern>> violations = validator.validate(pattern);

        assertFalse(violations.isEmpty());
    }

}
