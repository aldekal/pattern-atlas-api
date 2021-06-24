package io.github.patternatlas.api.rest.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LatexContent {
    private String content;
    private List<String> latexPackages;
    private String output;

    public LatexContent(String content, List<String> latexPackages, String output) {
        this.content = content;
        this.latexPackages = latexPackages;
        this.output = output;
    }
}
