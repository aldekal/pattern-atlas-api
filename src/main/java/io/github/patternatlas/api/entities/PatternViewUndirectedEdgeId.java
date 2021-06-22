package io.github.patternatlas.api.entities;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class PatternViewUndirectedEdgeId implements Serializable {
    protected UUID patternViewId;
    protected UUID undirectedEdgeId;
}
