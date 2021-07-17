package io.github.patternatlas.api.rest.model.shared;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class RatingModelMultiRequest extends RatingModelRequest {

    private RatingType ratingType;
}
