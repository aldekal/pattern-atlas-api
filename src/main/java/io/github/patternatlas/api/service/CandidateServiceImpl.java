package io.github.patternatlas.api.service;

import io.github.patternatlas.api.entities.candidate.Candidate;
import io.github.patternatlas.api.entities.candidate.CandidateRating;
import io.github.patternatlas.api.entities.candidate.comment.CandidateComment;
import io.github.patternatlas.api.entities.candidate.author.CandidateAuthor;
import io.github.patternatlas.api.entities.candidate.comment.CandidateCommentRating;
import io.github.patternatlas.api.entities.candidate.evidence.CandidateEvidence;
import io.github.patternatlas.api.entities.candidate.evidence.CandidateEvidenceRating;
import io.github.patternatlas.api.entities.issue.Issue;
import io.github.patternatlas.api.entities.issue.evidence.IssueEvidence;
import io.github.patternatlas.api.entities.shared.AuthorConstant;
import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.entities.user.role.Privilege;
import io.github.patternatlas.api.entities.user.role.PrivilegeConstant;
import io.github.patternatlas.api.entities.user.role.Role;
import io.github.patternatlas.api.entities.user.role.RoleConstant;
import io.github.patternatlas.api.repositories.CandidateAuthorRepository;
import io.github.patternatlas.api.repositories.CandidateCommentRatingRepository;
import io.github.patternatlas.api.repositories.CandidateCommentRepository;
import io.github.patternatlas.api.repositories.CandidateEvidenceRatingRepository;
import io.github.patternatlas.api.repositories.CandidateEvidenceRepository;
import io.github.patternatlas.api.repositories.CandidateRatingRepository;
import io.github.patternatlas.api.repositories.CandidateRepository;
import io.github.patternatlas.api.rest.model.candidate.CandidateModelRequest;
import io.github.patternatlas.api.rest.model.shared.AuthorModel;
import io.github.patternatlas.api.rest.model.shared.AuthorModelRequest;
import io.github.patternatlas.api.rest.model.shared.CommentModel;
import io.github.patternatlas.api.rest.model.shared.EvidenceModel;
import io.github.patternatlas.api.rest.model.shared.RatingModelMultiRequest;
import io.github.patternatlas.api.rest.model.shared.RatingModelRequest;
import io.github.patternatlas.api.rest.model.shared.RatingType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {

    private CandidateRepository candidateRepository;
    private CandidateRatingRepository candidateRatingRepository;
    private CandidateAuthorRepository candidateAuthorRepository;
    private CandidateCommentRepository candidateCommentRepository;
    private CandidateCommentRatingRepository candidateCommentRatingRepository;
    private CandidateEvidenceRepository candidateEvidenceRepository;
    private CandidateEvidenceRatingRepository candidateEvidenceRatingRepository;
    private PatternLanguageService patternLanguageService;
    private UserService userService;
    private RoleService roleService;
    private PrivilegeService privilegeService;
    private IssueService issueService;

    Logger logger = LoggerFactory.getLogger(CandidateServiceImpl.class);

    public CandidateServiceImpl(
            CandidateRepository candidateRepository,
            CandidateRatingRepository candidateRatingRepository,
            CandidateAuthorRepository candidateAuthorRepository,
            CandidateCommentRepository candidateCommentRepository,
            CandidateCommentRatingRepository candidateCommentRatingRepository,
            CandidateEvidenceRepository candidateEvidenceRepository,
            CandidateEvidenceRatingRepository candidateEvidenceRatingRepository,
            PatternLanguageService patternLanguageService,
            UserService userService,
            RoleService roleService,
            PrivilegeService privilegeService,
            IssueService issueService
    ) {
        this.candidateRepository = candidateRepository;
        this.candidateRatingRepository = candidateRatingRepository;
        this.candidateAuthorRepository = candidateAuthorRepository;
        this.candidateCommentRepository = candidateCommentRepository;
        this.candidateCommentRatingRepository = candidateCommentRatingRepository;
        this.candidateEvidenceRepository = candidateEvidenceRepository;
        this.candidateEvidenceRatingRepository = candidateEvidenceRatingRepository;
        this.patternLanguageService = patternLanguageService;
        this.userService = userService;
        this.roleService = roleService;
        this.privilegeService = privilegeService;
        this.issueService = issueService;
    }

    @Override
    @Transactional
    public Candidate saveCandidate(Candidate candidate) {
        return this.candidateRepository.save(candidate);
    }

    /**
     * CRUD Candidate
     */
    @Override
    @Transactional
    public Candidate createCandidate(CandidateModelRequest candidateModelRequest, UUID userId) {
        Candidate candidate = new Candidate(candidateModelRequest);
        UserEntity user = this.userService.getUserById(userId);
        if (null == candidate)
            throw new RuntimeException("Candidate to create is null");
        if (this.candidateRepository.existsByName(candidate.getName()))
            throw new EntityExistsException(String.format("Candidate name %s already exist!", candidateModelRequest.getName()));
        if (this.candidateRepository.existsByUri(candidateModelRequest.getUri()))
            throw new EntityExistsException(String.format("Candidate uri %s already exist!", candidateModelRequest.getUri()));

        // ISSUE TO PATTERN
        Candidate newCandidate = this.candidateRepository.save(candidate);

        Privilege readCandidatePrivilege = this.privilegeService.createPrivilege(PrivilegeConstant.PATTERN_CANDIDATE_READ + '_' + newCandidate.getId());
        Privilege updateCandidatePrivilege = this.privilegeService.createPrivilege(PrivilegeConstant.PATTERN_CANDIDATE_EDIT + '_' + newCandidate.getId());
        Privilege deleteCandidatePrivilege = this.privilegeService.createPrivilege(PrivilegeConstant.PATTERN_CANDIDATE_DELETE + '_' + newCandidate.getId());
        Privilege commentCandidatePrivilege = this.privilegeService.createPrivilege(PrivilegeConstant.PATTERN_CANDIDATE_COMMENT + '_' + newCandidate.getId());
        Privilege voteCandidatePrivilege = this.privilegeService.createPrivilege(PrivilegeConstant.PATTERN_CANDIDATE_VOTE + '_' + newCandidate.getId());
        Privilege evidenceCandidatePrivilege = this.privilegeService.createPrivilege(PrivilegeConstant.PATTERN_CANDIDATE_EVIDENCE + '_' + newCandidate.getId());
        Privilege toApprovedPattern = this.privilegeService.createPrivilege(PrivilegeConstant.PATTERN_CANDIDATE_TO_PATTERN + '_' + newCandidate.getId());

        Role helper = this.roleService.createRole(RoleConstant.HELPER + "_PATTERN_CANDIDATE_" + newCandidate.getId(), Arrays.asList(
            readCandidatePrivilege,
            commentCandidatePrivilege, voteCandidatePrivilege, evidenceCandidatePrivilege
        ));
        Role maintainer = this.roleService.createRole(RoleConstant.MAINTAINER + "_PATTERN_CANDIDATE_" + newCandidate.getId(), Arrays.asList(
            readCandidatePrivilege, updateCandidatePrivilege,
            commentCandidatePrivilege, voteCandidatePrivilege, evidenceCandidatePrivilege
        ));
        Role owner = this.roleService.createRole(RoleConstant.OWNER + "_PATTERN_CANDIDATE_" + newCandidate.getId(), Arrays.asList(
            readCandidatePrivilege, updateCandidatePrivilege, deleteCandidatePrivilege, toApprovedPattern,
            commentCandidatePrivilege, voteCandidatePrivilege, evidenceCandidatePrivilege
        ));

        if (candidateModelRequest.getIssueId() != null) {
            logger.info("Issue to Candidate request");
            Issue issue = this.issueService.getIssueById(candidateModelRequest.getIssueId());
            for (IssueEvidence issueEvidence: issue.getEvidences()) {
                CandidateEvidence evidence = new CandidateEvidence(issueEvidence, newCandidate, user);
                newCandidate.getEvidences().add(evidence);
            }
            this.issueService.deleteIssue(candidateModelRequest.getIssueId());
        }
        // ADD author
        if (candidateModelRequest.getAuthors() != null) {
            for (AuthorModel authorModel : candidateModelRequest.getAuthors()) {
                newCandidate.getAuthors().add(new CandidateAuthor(newCandidate, this.userService.getUserById(authorModel.getUserId()), authorModel.getAuthorRole() + "_PATTERN_CANDIDATE_" + newCandidate.getId()));
                UserEntity u = this.userService.getUserById(authorModel.getUserId());
                switch (authorModel.getAuthorRole()) {
                    case AuthorConstant.HELPER:
                        user.getRoles().add(helper);
                        this.userService.saveUser(u);
                        break;
                    case AuthorConstant.MAINTAINER:
                        user.getRoles().add(maintainer);
                        this.userService.saveUser(u);
                        break;
                    case AuthorConstant.OWNER:
                        if (null != u.getRoles()) {
                            u.getRoles().add(owner);
                        } else {
                            u.setRoles(new HashSet<>(Arrays.asList(owner)));
                        }
                        this.userService.saveUser(u);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid author role: " + authorModel.getAuthorRole());
                }
            }
        } else {
            newCandidate.getAuthors().add(new CandidateAuthor(newCandidate, this.userService.getUserById(userId), AuthorConstant.OWNER));
            user.getRoles().add(owner);
        }

        // ADD pattern language
        if (candidateModelRequest.getPatternLanguageId() != null && this.patternLanguageService.getPatternLanguageById(candidateModelRequest.getPatternLanguageId()) != null)
            newCandidate.setPatternLanguage(this.patternLanguageService.getPatternLanguageById(candidateModelRequest.getPatternLanguageId()));

        return this.candidateRepository.save(newCandidate);
    }

    @Override
    @Transactional
    public List<Candidate> getAllCandidates() {
        return this.candidateRepository.findAll();
    }

    @Override
    @Transactional
    public List<Candidate> getAllCandidatesByLanguageId(UUID languageId) {
        return this.candidateRepository.findAllByLanguageId(languageId);
    }

    @Override
    @Transactional
    public Candidate getCandidateById(UUID candidateId) {
        return this.candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Candidate with ID %s not found!", candidateId)));
    }

    @Override
    @Transactional
    public Candidate getCandidateByURI(String uri) {
        return this.candidateRepository.findByUri(uri)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Candidate with URI %s not found!", uri)));
    }

    @Override
    @Transactional
    public Candidate updateCandidate(UUID candidateId, UUID userId, CandidateModelRequest candidateModelRequest) {
        if (candidateModelRequest == null) {
            throw new RuntimeException("Candidate to update is null!");
        }
        Candidate candidate = this.getCandidateById(candidateId);
        UserEntity user = this.userService.getUserById(userId);

        if (this.candidateRepository.existsByUri(candidateModelRequest.getUri())) {
            Candidate candidateByURI = this.getCandidateByURI(candidateModelRequest.getUri());
            // NOT uri & name change
            if (!candidateByURI.getId().equals(candidate.getId())) {
                throw new EntityExistsException(String.format("Candidate uri %s already exist!", candidateModelRequest.getUri()));
            }
        }
        // ADD pattern language
        if (candidateModelRequest.getPatternLanguageId() != null && this.patternLanguageService.getPatternLanguageById(candidateModelRequest.getPatternLanguageId()) != null)
            candidate.setPatternLanguage(this.patternLanguageService.getPatternLanguageById(candidateModelRequest.getPatternLanguageId()));

        // UPDATE issue fields
        candidate.updateCandidate(candidateModelRequest);

        return this.candidateRepository.save(candidate);
    }

    @Override
    @Transactional
    public Candidate updateCandidateRating(UUID candidateId, UUID userId, RatingModelMultiRequest ratingModelMultiRequest) {
        Candidate candidate = this.getCandidateById(candidateId);
        UserEntity user = this.userService.getUserById(userId);
        CandidateRating candidateRating = new CandidateRating(candidate, user);
        if (this.candidateRatingRepository.existsById(candidateRating.getId())) {
            candidateRating = this.candidateRatingRepository.getOne(candidateRating.getId());
        }
        if (ratingModelMultiRequest.getRatingType().equals(RatingType.READABILITY)) {
            candidateRating.setReadability(ratingModelMultiRequest.getRating());

        } else if (ratingModelMultiRequest.getRatingType().equals(RatingType.UNDERSTANDABILITY)) {
            candidateRating.setUnderstandability(ratingModelMultiRequest.getRating());

        } else if (ratingModelMultiRequest.getRatingType().equals(RatingType.APPROPRIATENESS)) {
            candidateRating.setAppropriateness(ratingModelMultiRequest.getRating());

        } else {
            throw new EntityExistsException(String.format("Rating type does not exists", ratingModelMultiRequest.getRatingType()));
        }
        CandidateRating updatedCandidateRating = this.candidateRatingRepository.save(candidateRating);
        return updatedCandidateRating.getCandidate();
    }

    @Override
    @Transactional
    public void deleteCandidate(UUID candidateId, UUID userId) {
        UserEntity user = this.userService.getUserById(userId);

        this.roleService.deleteAllRolesByResourceId(candidateId);
        this.privilegeService.deleteAllPrivilegesByResourceId(candidateId);
        user.removeRole(candidateId.toString());
        this.candidateRepository.deleteById(candidateId);

    }

    /**
     * Author
     */
    @Override
    @Transactional
    public Candidate saveCandidateAuthor(UUID candidateId, AuthorModelRequest authorModelRequest) {
        Candidate candidate = this.getCandidateById(candidateId);
        UserEntity user = this.userService.getUserById(authorModelRequest.getUserId());
        CandidateAuthor candidateAuthor = new CandidateAuthor(candidate, user, authorModelRequest.getAuthorRole());
        candidateAuthor = this.candidateAuthorRepository.save(candidateAuthor);

        // Save roles associated with author
        List<Role> authorRoles = this.roleService.findAllFromEntityForAuthorRole(candidate.getId(),
                authorModelRequest.getAuthorRole());
        user.getRoles().addAll(authorRoles);
        this.userService.saveUser(user);

        return candidateAuthor.getCandidate();
    }

    @Override
    @Transactional
    public Candidate deleteCandidateAuthor(UUID candidateId, UUID userId) {
        Candidate candidate = this.getCandidateById(candidateId);
        UserEntity user = this.userService.getUserById(userId);
        CandidateAuthor candidateAuthor = new CandidateAuthor(candidate, user);
        if (this.candidateAuthorRepository.existsById(candidateAuthor.getId())) {
            List<Role> roles = this.userService.getAllRolesFromEntity(candidateId) // find all roles for candidate
                    .stream().filter((role) -> role.getUsers().contains(user)) // filter for roles for current user
                    .collect(Collectors.toList());

            user.getRoles().removeAll(roles); // delete only those

            this.candidateAuthorRepository.deleteById(candidateAuthor.getId());
            this.userService.saveUser(user);
        }
        return this.getCandidateById(candidateId);
    }

    /**
     * Comment
     */
    @Override
    @Transactional
    public Candidate createComment(UUID candidateId, UUID userId, CommentModel commentModel) {
        Candidate candidate = this.getCandidateById(candidateId);
        UserEntity user = this.userService.getUserById(userId);

        CandidateComment comment = new CandidateComment(commentModel.getText(), candidate, user);
        comment = this.candidateCommentRepository.save(comment);
        return comment.getCandidate();
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateComment getCommentById(UUID candidateCommentId) {
        return this.candidateCommentRepository.findById(candidateCommentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Candidate comment with ID %s not found!", candidateCommentId)));
    }

    @Override
    @Transactional
    public Candidate updateComment(UUID candidateId, UUID commentId, UUID userId, CommentModel commentModel) {
        if (commentModel == null)
            throw new RuntimeException("Candidate comment to update is null!");
        // Used to check if candidate actually exists
        CandidateComment candidateComment = this.authCandidateComment(candidateId, commentId, userId);

        // UPDATE issue comment
        candidateComment.updateComment(commentModel.getText());
        candidateComment = this.candidateCommentRepository.save(candidateComment);
        return candidateComment.getCandidate();
    }

    @Override
    @Transactional
    public Candidate updateCandidateCommentRating(UUID candidateId, UUID commentId, UUID userId, RatingModelRequest ratingModelRequest) {
        Candidate candidate = this.getCandidateById(candidateId);
        CandidateComment candidateComment = this.getCommentById(commentId);
        UserEntity user = this.userService.getUserById(userId);
        CandidateCommentRating candidateCommentRating = new CandidateCommentRating(candidateComment, user, ratingModelRequest.getRating());
        if (this.candidateCommentRatingRepository.existsByIdAndRating(candidateCommentRating.getId(), candidateCommentRating.getRating())) {
            candidateCommentRating.setRating(0);
        }
        candidateCommentRating = this.candidateCommentRatingRepository.save(candidateCommentRating);
        return candidateCommentRating.getCandidateComment().getCandidate();
    }

    @Override
    @Transactional
    public Candidate deleteComment(UUID candidateId, UUID commentId, UUID userId) {
        this.authCandidateComment(candidateId, commentId, userId);
        this.candidateCommentRepository.deleteById(commentId);
        return this.getCandidateById(candidateId);
    }

    private CandidateComment authCandidateComment(UUID candidateId, UUID commentId, UUID userId) {
        CandidateComment candidateComment = this.getCommentById(commentId);
        // CORRECT Issue
        if (!candidateComment.getCandidate().equals(this.getCandidateById(candidateId)))
            throw new EntityNotFoundException(String.format("Candidate comment with id %s does not belong to candidate with id %s", commentId, candidateId));
        // CORRECT user
        if (!candidateComment.getUser().equals(this.userService.getUserById(userId)))
            throw new EntityNotFoundException(String.format("Candidate comment with id %s does not belong to user with id %s", commentId, userId));
        return candidateComment;
    }

    /**
     * Evidence
     */
    @Override
    @Transactional
    public Candidate createEvidence(UUID candidateId, UUID userId, EvidenceModel evidenceModel) {
        Candidate candidate = this.getCandidateById(candidateId);
        UserEntity user = this.userService.getUserById(userId);

        CandidateEvidence candidateEvidence = new CandidateEvidence(
                evidenceModel.getTitle(),
                evidenceModel.getContext(),
                evidenceModel.getType(),
                evidenceModel.getSupporting(),
                evidenceModel.getSource(),
                candidate, user);
        candidateEvidence = this.candidateEvidenceRepository.save(candidateEvidence);
        return candidateEvidence.getCandidate();
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateEvidence getEvidenceById(UUID evidenceId) {
        return this.candidateEvidenceRepository.findById(evidenceId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Candidate evidence with ID %s not found!", evidenceId)));
    }

    @Override
    @Transactional
    public Candidate updateEvidence(UUID candidateId, UUID evidenceId, UUID userId, EvidenceModel evidenceModel) {
        if (evidenceModel == null)
            throw new RuntimeException("Candidate evidence to update is null!");
        CandidateEvidence candidateEvidence = this.existsCandidateEvidence(candidateId, evidenceId, userId);
        // UPDATE Candidate evidence
        candidateEvidence.updateEvidence(evidenceModel.getTitle(), evidenceModel.getContext(), evidenceModel.getType(), evidenceModel.getSupporting(), evidenceModel.getSource());
        candidateEvidence = this.candidateEvidenceRepository.save(candidateEvidence);
        return candidateEvidence.getCandidate();
    }

    @Override
    @Transactional
    public Candidate updateCandidateEvidenceRating(UUID candidateId, UUID evidenceID, UUID userId, RatingModelRequest ratingModelRequest) {
        Candidate candidate = this.getCandidateById(candidateId);
        CandidateEvidence candidateEvidence = this.getEvidenceById(evidenceID);
        UserEntity user = this.userService.getUserById(userId);
        CandidateEvidenceRating candidateEvidenceRating = new CandidateEvidenceRating(candidateEvidence, user, ratingModelRequest.getRating());
        if (this.candidateEvidenceRatingRepository.existsByIdAndRating(candidateEvidenceRating.getId(), candidateEvidenceRating.getRating())) {
            candidateEvidenceRating.setRating(0);
        }
        candidateEvidenceRating = this.candidateEvidenceRatingRepository.save(candidateEvidenceRating);
        return candidateEvidenceRating.getCandidateEvidence().getCandidate();
    }

    @Override
    @Transactional
    public Candidate deleteEvidence(UUID candidateId, UUID evidenceId, UUID userId) {
        this.existsCandidateEvidence(candidateId, evidenceId, userId);
        this.candidateEvidenceRepository.deleteById(evidenceId);
        return this.getCandidateById(candidateId);
    }

    private CandidateEvidence existsCandidateEvidence(UUID candidateId, UUID evidenceId, UUID userId) {
        CandidateEvidence candidateEvidence = this.getEvidenceById(evidenceId);
        // CORRECT Evidence
        if (!candidateEvidence.getCandidate().equals(this.getCandidateById(candidateId)))
            throw new EntityNotFoundException(String.format("Candidate comment with id %s does not belong to candidate with id %s", evidenceId, candidateId));
        // CORRECT user
        if (!candidateEvidence.getUser().equals(this.userService.getUserById(userId)))
            throw new EntityNotFoundException(String.format("Candidate comment with id %s does not belong to user with id %s", evidenceId, userId));
        return candidateEvidence;
    }
}
