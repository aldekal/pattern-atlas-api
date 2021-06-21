package io.github.ust.quantil.patternatlas.api.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.ust.quantil.patternatlas.api.entities.PatternView;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserAlreadyVotedException extends RuntimeException {

    public UserAlreadyVotedException(String message) {
        super(message);
    }

    public UserAlreadyVotedException(UUID patternViewId) {
        super(String.format("PatternView \"%s\" not found!", patternViewId));
    }

    public UserAlreadyVotedException(PatternView patternView) {
        super(String.format("PatternView \"%s\" not found!", patternView.getId()));
    }
}
