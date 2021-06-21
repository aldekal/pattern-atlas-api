package io.github.ust.quantil.patternatlas.api.entities.designmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.ust.quantil.patternatlas.api.entities.EntityWithURI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class DesignModel extends EntityWithURI {

    private URL logo;

    @JsonIgnore
    @OneToMany(mappedBy = "designModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DesignModelPatternInstance> patterns = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "designModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DesignModelPatternEdge> directedEdges;

    @JsonIgnore
    @OneToMany(mappedBy = "designModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DesignModelUndirectedEdge> undirectedEdges;
}
