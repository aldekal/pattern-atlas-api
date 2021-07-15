package io.github.patternatlas.api.rest.model;

import java.util.UUID;

import org.springframework.hateoas.server.core.Relation;

import io.github.patternatlas.api.entities.designmodel.DesignModelPatternGraphData;
import io.github.patternatlas.api.entities.designmodel.DesignModelPatternInstance;
import io.github.patternatlas.api.entities.PatternLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Relation(value = "pattern", collectionRelation = "patterns")
public class PatternInstanceDTO extends PatternModel {

    protected DesignModelPatternGraphData graphData;

    protected UUID patternLanguageId;

    protected double x;

    protected double y;

    public static PatternInstanceDTO from(DesignModelPatternInstance dmpi) {
        PatternInstanceDTO gpm = new PatternInstanceDTO();

        gpm.setName(dmpi.getPattern().getName());
        gpm.setId(dmpi.getPatternInstanceId());
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
