package com.patternpedia.api.service;

import com.patternpedia.api.entities.Pattern;
import javafx.util.Pair;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface PatternRenderService {

    @Transactional (readOnly = false)
    Object renderContent (Pattern pattern);

    @Transactional (readOnly = false)
    Pair<Integer, Integer> getNextOccurance(String content, String begin, String end);

    @Transactional (readOnly = false)
    byte [] renderContentViaAPI(String content, List<String> packages, String output);

    @Transactional (readOnly = false)
    String saveAndUploadFile(byte[] file, String output);
}
