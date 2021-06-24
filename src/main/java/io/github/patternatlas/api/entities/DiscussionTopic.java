package io.github.patternatlas.api.entities;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.github.patternatlas.api.rest.model.Status;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @OneToMany(mappedBy = "discussionTopic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DiscussionComment> discussionComments;
}

