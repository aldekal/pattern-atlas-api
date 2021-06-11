package com.patternatlas.api.entities.designmodel;

import com.patternatlas.api.rest.model.FileDTO;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;


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
