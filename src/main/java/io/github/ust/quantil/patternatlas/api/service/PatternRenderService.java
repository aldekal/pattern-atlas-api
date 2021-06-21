package io.github.ust.quantil.patternatlas.api.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import io.github.ust.quantil.patternatlas.api.entities.Pattern;

public interface PatternRenderService {

    @Transactional(readOnly = false)
    Object renderContent(Pattern pattern, Pattern oldVersion);

    @Transactional(readOnly = false)
    Integer[] getNextOccurance(String content, String begin, String end);

    @Transactional(readOnly = false)
    byte[] renderContentViaAPI(String content, List<String> packages, String output);

    @Transactional(readOnly = false)
    String saveAndUploadFile(byte[] file, String output);
}
