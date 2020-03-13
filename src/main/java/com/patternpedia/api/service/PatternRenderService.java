package com.patternpedia.api.service;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.rest.model.AlgorithmType;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

public interface PatternRenderService {

    @Transactional (readOnly = false)
    Object renderContent (Pattern pattern);

    @Transactional (readOnly = false)
    AlgorithmType checkForAlgorithmInput (String content);

    @Transactional (readOnly = false)
    String renderQuantikz (String content) throws IOException;

}
