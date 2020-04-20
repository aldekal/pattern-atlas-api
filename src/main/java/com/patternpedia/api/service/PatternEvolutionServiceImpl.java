package com.patternpedia.api.service;

import com.patternpedia.api.entities.evolution.CommentPatternEvolution;
import com.patternpedia.api.entities.evolution.PatternEvolution;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.entities.rating.RatingPatternEvolution;
import com.patternpedia.api.exception.NullPatternException;
import com.patternpedia.api.exception.PatternLanguageNotFoundException;
import com.patternpedia.api.exception.PatternNotFoundException;
import com.patternpedia.api.repositories.CommentPatternEvolutionRepository;
import com.patternpedia.api.repositories.PatternEvolutionRepository;
import com.patternpedia.api.repositories.RatingPatternEvolutionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PatternEvolutionServiceImpl implements PatternEvolutionService {

    private PatternEvolutionRepository patternEvolutionRepository;
    private RatingPatternEvolutionRepository ratingPatternEvolutionRepository;
    private CommentPatternEvolutionRepository commentPatternEvolutionRepository;
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(PatternEvolutionServiceImpl.class);

    public PatternEvolutionServiceImpl(
            PatternEvolutionRepository patternEvolutionRepository,
            RatingPatternEvolutionRepository ratingPatternEvolutionRepository,
            UserService userService,
            CommentPatternEvolutionRepository commentPatternEvolutionRepository
    ) {
        this.patternEvolutionRepository = patternEvolutionRepository;
        this.ratingPatternEvolutionRepository = ratingPatternEvolutionRepository;
        this.commentPatternEvolutionRepository = commentPatternEvolutionRepository;
        this.userService = userService;
    }

    /**
     * CRUD PatternEvolution
     */
    @Override
    @Transactional
    public PatternEvolution createPatternEvolution(PatternEvolution patternEvolution) {
        if (null == patternEvolution) {
            throw new NullPatternException();
        }

//        if (null == patternEvolution.getPatternLanguage()) {
//            throw new NullPatternLanguageException();
//        }

        return this.patternEvolutionRepository.save(patternEvolution);
    }

    @Override
    @Transactional
    public PatternEvolution updatePatternEvolution(PatternEvolution patternEvolution) {
        if (null == patternEvolution) {
            throw new NullPatternException();
        }
        if (!this.patternEvolutionRepository.existsById(patternEvolution.getId())) {
            throw new PatternLanguageNotFoundException(String.format("PatternEvolution %s not found", patternEvolution.getId()));
        }

        logger.info(String.format("Update PatternEvolution: %s", patternEvolution.toString()));
        return this.patternEvolutionRepository.save(patternEvolution);
    }

    @Override
    @Transactional
    public void deletePatternEvolution(UUID patternEvolutionId) {
        PatternEvolution patternEvolution = this.getPatternEvolutionById(patternEvolutionId);
        if (null == patternEvolution) {
            throw new NullPatternException();
        }

        // patternEvolution.setPatternViews(null);
        // this.patternEvolutionRepository.save(patternEvolution);
        this.patternEvolutionRepository.deleteById(patternEvolutionId);
    }

    @Override
    @Transactional(readOnly = true)
    public PatternEvolution getPatternEvolutionById(UUID patternEvolutionId) {
        return this.patternEvolutionRepository.findById(patternEvolutionId)
                .orElseThrow(() -> new PatternNotFoundException(patternEvolutionId));
    }

    @Override
    @Transactional(readOnly = true)
    public PatternEvolution getPatternEvolutionByUri(String uri) {
        return this.patternEvolutionRepository.findByUri(uri)
                .orElseThrow(() -> new PatternNotFoundException(String.format("Pattern with URI %s not found!", uri)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatternEvolution> getAllPatternEvolutions() {
        return this.patternEvolutionRepository.findAll();
    }

    /**
     * Voting PatternEvolution
     *
     * @param rating up, down
     * @apiNote If user already up- or downvoted and does so again it neutralises vote.
     */
    @Override
    @Transactional
    public PatternEvolution userRating(UUID patternEvolutionId, UUID userId, String rating) {
        PatternEvolution patternEvolution = this.getPatternEvolutionById(patternEvolutionId);
        UserEntity user = this.userService.getUserById(userId);

        logger.info(String.format("User %s updates rating for PatternEvolution %s", user.getId(), patternEvolution.getId()));

        RatingPatternEvolution ratingPatternEvolution = new RatingPatternEvolution(patternEvolution, user);

        if (this.ratingPatternEvolutionRepository.existsById(ratingPatternEvolution.getId())) {
            ratingPatternEvolution = this.ratingPatternEvolutionRepository.findByPatternEvolutionAndUser(patternEvolution, user);
            logger.info(String.format("Rating for user %s exists with rating %s", user.getId(), ratingPatternEvolution.getRating()));
        } else {
            logger.info(String.format("Rating for user %s does not exist", user.getId()));
        }

        if (rating.equals("up")) {
            if (ratingPatternEvolution.getRating() == 1) {
                ratingPatternEvolution.setRating(0);
                logger.info(String.format("User %s deleted up", user.getId()));
            } else {
                ratingPatternEvolution.setRating(1);
                logger.info(String.format("User %s rated up", user.getId()));
            }

        } else if (rating.equals("down")) {
            if (ratingPatternEvolution.getRating() == -1) {
                ratingPatternEvolution.setRating(0);
                logger.info(String.format("User %s deleted down", user.getId()));
            } else {
                ratingPatternEvolution.setRating(-1);
                logger.info(String.format("User %s rated down", user.getId()));
            }
        } else {
            logger.info(String.format("Wrong rating value: %s", rating));
            return null;
        }

        patternEvolution.getUserRating().add(ratingPatternEvolution);
        int patternEvolutionRating = patternEvolution.getUserRating().stream().mapToInt(RatingPatternEvolution::getRating).sum();
        patternEvolution.setRating(patternEvolutionRating);
        logger.info(String.format("New rating is: %d", patternEvolutionRating));

        return this.updatePatternEvolution(patternEvolution);
    }

    /**
     * Comment PatternEvolution
     */
    @Override
    @Transactional
    public PatternEvolution createComment(UUID patternEvolutionId, UUID userId, CommentPatternEvolution commentPatternEvolution) {
        PatternEvolution patternEvolution = this.getPatternEvolutionById(patternEvolutionId);
        UserEntity user = this.userService.getUserById(userId);

        CommentPatternEvolution comment = new CommentPatternEvolution(commentPatternEvolution.getText());
        patternEvolution.addComment(comment, user);

        return this.updatePatternEvolution(patternEvolution);
    }


}
