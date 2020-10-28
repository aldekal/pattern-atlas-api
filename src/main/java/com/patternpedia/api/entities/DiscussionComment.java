package com.patternpedia.api.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;



@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class DiscussionComment {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;
    private String text;
    private UUID replyTo;
    private Date date;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private DiscussionTopic discussionTopic;


}
