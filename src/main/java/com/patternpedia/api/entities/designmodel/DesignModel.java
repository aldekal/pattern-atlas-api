package com.patternpedia.api.entities.designmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.patternpedia.api.entities.EntityWithURI;
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

//    public void removePattern(Pattern pattern) {
//        for (Iterator<DesignModelPattern> iterator = this.patterns.iterator(); iterator.hasNext(); ) {
//            DesignModelPattern designModelPattern = iterator.next();
//            if (designModelPattern.getDesignModel().equals(this) && designModelPattern.getPattern().equals(pattern)) {
//                iterator.remove();
//                designModelPattern.getPattern().getDesignModels().remove(designModelPattern);
//                designModelPattern.setPattern(null);
//                designModelPattern.setDesignModel(null);
//                break;
//            }
//        }
//    }
//
//    public void removeDirectedEdge(DirectedEdge directedEdge) {
//        for (Iterator<DesignModelDirectedEdge> iterator = this.directedEdges.iterator(); iterator.hasNext(); ) {
//            DesignModelDirectedEdge designModelDirectedEdge = iterator.next();
//            if (designModelDirectedEdge.getDesignModel().equals(this) &&
//                    designModelDirectedEdge.getDirectedEdge().equals(directedEdge)) {
//                iterator.remove();
//                designModelDirectedEdge.getDirectedEdge().getDesignModels().remove(designModelDirectedEdge);
//                designModelDirectedEdge.setDirectedEdge(null);
//                designModelDirectedEdge.setDesignModel(null);
//                break;
//            }
//        }
//    }
}
