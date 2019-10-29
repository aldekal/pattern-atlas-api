package com.patternpedia.api.util;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.repositories.PatternLanguageRepository;
import com.patternpedia.api.repositories.PatternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntegrationTestHelper {

    @Autowired
    private PatternRepository patternRepository;

    @Autowired
    private PatternLanguageRepository patternLanguageRepository;

    public Pattern createOrGetPattern(Pattern p) {
        if (null != p.getUri() && this.patternRepository.existsByUri(p.getUri())) {
            return this.patternRepository.findByUri(p.getUri()).get();
        } else {
            return this.patternRepository.save(p);
        }
    }

    public PatternLanguage createOrGetPatternLanguage(PatternLanguage patternLanguage) {
        if (null != patternLanguage.getUri() && this.patternLanguageRepository.existsByUri(patternLanguage.getUri())) {
            return this.patternLanguageRepository.findByUri(patternLanguage.getUri()).get();
        } else {
            return this.patternLanguageRepository.save(patternLanguage);
        }
    }

}
