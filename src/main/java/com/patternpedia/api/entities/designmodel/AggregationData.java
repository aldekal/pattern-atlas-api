package com.patternpedia.api.entities.designmodel;

import com.patternpedia.api.rest.model.FileDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;


@Data
public class AggregationData {

    private DesignModelPatternInstance source;

    private DesignModelPatternInstance target;

    private DesignModelPatternEdge edge;

    private Map<String, Object> templateContext = new HashMap<>();

    private FileDTO result;


    public AggregationData(DesignModelPatternInstance source, DesignModelPatternInstance target, DesignModelPatternEdge edge) {
        this.source = source;
        this.target = target;
        this.edge = edge;
    }
}
