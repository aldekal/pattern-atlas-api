package io.github.patternatlas.api.util;

import io.github.patternatlas.api.entities.user.UserEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RatingHelper {

    public int updateRating(String rating, int oldRating, UserEntity user) {

        if (rating.equals("up")) {
            if (oldRating == 1) {
                log.info(String.format("User %s deleted up", user.getId()));
                return 0;
            } else {
                log.info(String.format("User %s rated up", user.getId()));
                return 1;
            }
        } else if (rating.equals("down")) {
            if (oldRating == -1) {
                log.info(String.format("User %s deleted down", user.getId()));
                return 0;
            } else {
                log.info(String.format("User %s rated down", user.getId()));
                return -1;
            }
        } else {
            log.info(String.format("Wrong rating value: %s", rating));
            return -2;
        }
    }
}
