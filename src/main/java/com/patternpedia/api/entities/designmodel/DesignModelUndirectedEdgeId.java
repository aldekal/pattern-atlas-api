package com.patternpedia.api.entities.designmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class DesignModelUndirectedEdgeId implements Serializable {
    protected UUID designModelId;
    protected UUID undirectedEdgeId;
}
