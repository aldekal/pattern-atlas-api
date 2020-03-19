package com.patternpedia.api.service;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.rest.model.AlgorithmType;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

public interface PatternRenderService {

    @Transactional (readOnly = false)
    Object renderContent (Pattern pattern);

    @Transactional (readOnly = false)
    AlgorithmType checkForAlgorithmInput (String content);

    @Transactional (readOnly = false)
    String renderQuantikz (String content) throws IOException;

//    @Transactional (readOnly = false)
//    Map<Integer, Integer> getBeginAndEndIndexes(String content, String begin, String end);

    @Transactional (readOnly = false)
    String renderLatex(String content, int begin, int end, AlgorithmType type);
}
