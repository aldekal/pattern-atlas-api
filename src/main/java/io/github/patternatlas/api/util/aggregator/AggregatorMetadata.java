package io.github.patternatlas.api.util.aggregator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AggregatorMetadata {

    String[] sourceTypes();

    String[] targetTypes();
}
