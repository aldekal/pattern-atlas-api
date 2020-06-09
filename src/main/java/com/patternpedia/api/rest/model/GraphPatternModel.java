package com.patternpedia.api.rest.model;

import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.designmodel.DesignModelPatternGraphData;
import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;


@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Relation(value = "pattern", collectionRelation = "patterns")
public class GraphPatternModel extends PatternModel {

    protected DesignModelPatternGraphData graphData;


    public static GraphPatternModel from(DesignModelPatternInstance dmpi) {
        GraphPatternModel gpm = new GraphPatternModel();


        gpm.setName(dmpi.getPattern().getName());
        gpm.setId(dmpi.getId().getPatternInstanceId());
        gpm.setPattern(dmpi.getPattern());
        gpm.setUri(dmpi.getPattern().getUri());
        gpm.setIconUrl(dmpi.getPattern().getIconUrl());

        PatternLanguage patternLanguage = dmpi.getPattern().getPatternLanguage();
        gpm.setPatternLanguageId(patternLanguage.getId());
        gpm.setPatternLanguageName(patternLanguage.getName());

        gpm.setGraphData(dmpi.getGraphData());

        return gpm;
    }
}
