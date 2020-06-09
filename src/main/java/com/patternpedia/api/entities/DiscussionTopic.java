package com.patternpedia.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.patternpedia.api.rest.model.Status;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
public class DiscussionTopic {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    private String title;

    private String description;

    private Status status;

    private Date date;

    private Double x;

    private Double y;

    private Double width;

    private Double height;

    private String fill;

    private UUID imageId;

    @JsonIgnore
    @OneToMany(mappedBy = "discussionTopic",  cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DiscussionComment> discussionComments;
}

