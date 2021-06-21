package io.github.ust.quantil.patternatlas.api.entities.designmodel;

import java.util.HashMap;
import java.util.Map;

import io.github.ust.quantil.patternatlas.api.rest.model.FileDTO;
import lombok.Data;

@Data
public class AggregationData {

    private DesignModelPatternInstance source;

    private DesignModelPatternInstance target;

    private Map<String, Object> templateContext = new HashMap<>();

    private FileDTO result;

    public AggregationData(DesignModelPatternInstance source, DesignModelPatternInstance target) {
        this.source = source;
        this.target = target;
    }
}
