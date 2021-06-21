package io.github.ust.quantil.patternatlas.api.entities.candidate.rating;

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
public class CandidateRatingKey implements Serializable {

    protected UUID candidateId;
    protected UUID userId;
}
