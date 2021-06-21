package io.github.patternatlas.api.entities;

import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
public class PatternSectionSchema implements Comparable<PatternSectionSchema> {

    @Id
    @GeneratedValue(generator = "pg-uuid")
    private UUID id;

    private String label;

    private String name;

    private String type;

    private Integer position;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private PatternSchema patternSchema;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany
    private List<PatternSection> patternSections;

    @Override
    public int compareTo(PatternSectionSchema o) {
        return position.compareTo(o.position);
    }
}
