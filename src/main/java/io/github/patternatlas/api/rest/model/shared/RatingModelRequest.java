package io.github.patternatlas.api.rest.model.shared;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class RatingModelRequest {

    private int rating;
}
