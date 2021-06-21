package io.github.patternatlas.api.entities.issue.rating;

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
public class IssueCommentRatingKey implements Serializable {

    protected UUID issueCommentId;
    protected UUID userId;
}
