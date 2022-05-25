package io.github.patternatlas.api.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.github.patternatlas.api.entities.issue.Issue;
import io.github.patternatlas.api.entities.issue.IssueRating;
import io.github.patternatlas.api.entities.issue.author.IssueAuthor;
import io.github.patternatlas.api.entities.issue.comment.IssueComment;
import io.github.patternatlas.api.entities.issue.comment.IssueCommentRating;
import io.github.patternatlas.api.entities.issue.evidence.IssueEvidence;
import io.github.patternatlas.api.entities.issue.evidence.IssueEvidenceRating;
import io.github.patternatlas.api.entities.shared.AuthorConstant;
import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.entities.user.role.Privilege;
import io.github.patternatlas.api.entities.user.role.PrivilegeConstant;
import io.github.patternatlas.api.entities.user.role.Role;
import io.github.patternatlas.api.entities.user.role.RoleConstant;
import io.github.patternatlas.api.repositories.IssueAuthorRepository;
import io.github.patternatlas.api.repositories.IssueCommentRatingRepository;
import io.github.patternatlas.api.repositories.IssueCommentRepository;
import io.github.patternatlas.api.repositories.IssueEvidenceRatingRepository;
import io.github.patternatlas.api.repositories.IssueEvidenceRepository;
import io.github.patternatlas.api.repositories.IssueRatingRepository;
import io.github.patternatlas.api.repositories.IssueRepository;
import io.github.patternatlas.api.rest.model.issue.IssueModelRequest;
import io.github.patternatlas.api.rest.model.shared.AuthorModelRequest;
import io.github.patternatlas.api.rest.model.shared.CommentModel;
import io.github.patternatlas.api.rest.model.shared.EvidenceModel;
import io.github.patternatlas.api.rest.model.shared.RatingModelRequest;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {

    private IssueRepository issueRepository;
    private IssueRatingRepository issueRatingRepository;
    private IssueAuthorRepository issueAuthorRepository;
    private IssueCommentRepository issueCommentRepository;
    private IssueCommentRatingRepository issueCommentRatingRepository;
    private IssueEvidenceRepository issueEvidenceRepository;
    private IssueEvidenceRatingRepository issueEvidenceRatingRepository;
    private UserService userService;
    private RoleService roleService;
    private PrivilegeService privilegeService;

    Logger logger = LoggerFactory.getLogger(IssueServiceImpl.class);

    public IssueServiceImpl(
            IssueRepository issueRepository,
            IssueRatingRepository issueRatingRepository,
            IssueAuthorRepository issueAuthorRepository,
            IssueCommentRepository issueCommentRepository,
            IssueCommentRatingRepository issueCommentRatingRepository,
            IssueEvidenceRepository issueEvidenceRepository,
            IssueEvidenceRatingRepository issueEvidenceRatingRepository,
            UserService userService,
            RoleService roleService,
            PrivilegeService privilegeService
    ) {
        this.issueRepository = issueRepository;
        this.issueRatingRepository = issueRatingRepository;
        this.issueAuthorRepository = issueAuthorRepository;
        this.issueCommentRepository = issueCommentRepository;
        this.issueCommentRatingRepository = issueCommentRatingRepository;
        this.issueEvidenceRepository = issueEvidenceRepository;
        this.issueEvidenceRatingRepository = issueEvidenceRatingRepository;
        this.userService = userService;
        this.roleService = roleService;
        this.privilegeService = privilegeService;
    }

    @Override
    @Transactional
    public Issue saveIssue(Issue issue) {
        return this.issueRepository.save(issue);
    }

    /**
     * CRUD Issue
     */
    @Override
    @Transactional
    public Issue createIssue(IssueModelRequest issueModelRequest, UUID userId) {
        Issue issue = new Issue(issueModelRequest);
        if (null == issue)
            throw new RuntimeException("Issue to create is null");
        if (this.issueRepository.existsByName(issueModelRequest.getName()))
            throw new EntityExistsException(String.format("Issue name %s already exist!", issueModelRequest.getName()));
        if (this.issueRepository.existsByUri(issueModelRequest.getUri()))
            throw new EntityExistsException(String.format("Issue uri %s already exist!", issueModelRequest.getUri()));

        Issue newIssue = this.issueRepository.save(issue);
        UserEntity user = this.userService.getUserById(userId);

        Privilege readIssuePrivilege = this.privilegeService.createPrivilege(PrivilegeConstant.ISSUE_READ + '_' + newIssue.getId());
        Privilege updateIssuePrivilege = this.privilegeService.createPrivilege(PrivilegeConstant.ISSUE_EDIT + '_' + newIssue.getId());
        Privilege deleteIssuePrivilege = this.privilegeService.createPrivilege(PrivilegeConstant.ISSUE_DELETE + '_' + newIssue.getId());
        Privilege commentIssuePrivilege = this.privilegeService.createPrivilege(PrivilegeConstant.ISSUE_COMMENT + '_' + newIssue.getId());
        Privilege voteIssuePrivilege = this.privilegeService.createPrivilege(PrivilegeConstant.ISSUE_VOTE + '_' + newIssue.getId());
        Privilege evidenceIssuePrivilege = this.privilegeService.createPrivilege(PrivilegeConstant.ISSUE_EVIDENCE + '_' + newIssue.getId());
        Privilege toPatternCandidate = this.privilegeService.createPrivilege(PrivilegeConstant.ISSUE_TO_PATTERN_CANDIDATE + '_' + newIssue.getId());

        Role helper = this.roleService.createRole(RoleConstant.HELPER + "_ISSUE_" + newIssue.getId(), Arrays.asList(
            readIssuePrivilege, 
            commentIssuePrivilege, voteIssuePrivilege, evidenceIssuePrivilege
        ));
        Role maintainer = this.roleService.createRole(RoleConstant.MAINTAINER + "_ISSUE_" + newIssue.getId(), Arrays.asList(
            readIssuePrivilege, updateIssuePrivilege, 
            commentIssuePrivilege, voteIssuePrivilege, evidenceIssuePrivilege
        ));
        Role owner = this.roleService.createRole(RoleConstant.OWNER + "_ISSUE_" + newIssue.getId(), Arrays.asList(
            readIssuePrivilege, updateIssuePrivilege, deleteIssuePrivilege, toPatternCandidate,
            commentIssuePrivilege, voteIssuePrivilege, evidenceIssuePrivilege
        ));

        newIssue.getAuthors().add(new IssueAuthor(newIssue, user, AuthorConstant.OWNER));
        user.getRoles().add(owner);
        return this.issueRepository.save(newIssue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Issue> getAllIssues() {
        return this.issueRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Issue getIssueById(UUID issueId) {
        return this.issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Issue with ID %s not found!", issueId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Issue getIssueByURI(String uri) {
        return this.issueRepository.findByUri(uri)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Issue with URI %s not found!", uri)));
    }

    @Override
    @Transactional
    public Issue updateIssue(UUID issueId, UUID userId, IssueModelRequest issueModelRequest) {
        if (issueModelRequest == null) {
            throw new RuntimeException("Issue to update is null!");
        }
        Issue issue = this.getIssueById(issueId);

        // UPDATE issue fields
        if (this.issueRepository.existsByUri(issueModelRequest.getUri())) {
            Issue issueUri = this.getIssueByURI(issueModelRequest.getUri());
            // NOT uri & name change
            if (!issueUri.getId().equals(issue.getId())) {
                throw new EntityExistsException(String.format("Issue uri %s already exist!", issueModelRequest.getUri()));
            }
        }
        issue.updateIssue(issueModelRequest);
        return this.issueRepository.save(issue);
    }

    @Override
    @Transactional
    public Issue updateIssueRating(UUID issueId, UUID userId, RatingModelRequest ratingModelRequest) {
        Issue issue = this.getIssueById(issueId);
        UserEntity user = this.userService.getUserById(userId);
        IssueRating issueRating = new IssueRating(issue, user, ratingModelRequest.getRating());
        if (this.issueRatingRepository.existsByIdAndRating(issueRating.getId(), issueRating.getRating())) {
            issueRating.setRating(0);
        }
        issueRating = this.issueRatingRepository.save(issueRating);
        return issueRating.getIssue();
    }

    @Override
    @Transactional
    public void deleteIssue(UUID issueId) {
        this.roleService.deleteAllRolesByResourceId(issueId);
        this.privilegeService.deleteAllPrivilegesByResourceId(issueId);
        this.issueRepository.deleteById(issueId);
    }

    /**
     * Author
     */
    @Override
    @Transactional
    public Issue saveIssueAuthor(UUID issueId, AuthorModelRequest authorModelRequest) {
        Issue issue = this.getIssueById(issueId);
        UserEntity user = userService.getUserById(authorModelRequest.getUserId());
        IssueAuthor issueAuthor = new IssueAuthor(issue, user, authorModelRequest.getAuthorRole());
        issueAuthor = this.issueAuthorRepository.save(issueAuthor);

        // Save roles associated with the author
        // Get requested role
        List<Role> authorRoles = this.roleService.findAllFromEntityForAuthorRole(issue.getId(),
                authorModelRequest.getAuthorRole());
        user.getRoles().addAll(authorRoles);
        this.userService.saveUser(user);


        return issueAuthor.getIssue();
    }

    @Override
    @Transactional
    public Issue deleteIssueAuthor(UUID issueId, UUID userId) {
        Issue issue = this.getIssueById(issueId);
        UserEntity user = this.userService.getUserById(userId);
        IssueAuthor issueAuthor = new IssueAuthor(issue, user);
        if (this.issueAuthorRepository.existsById(issueAuthor.getId())) {
            List<Role> roles = this.userService.getAllRolesFromEntity(issueId) // find all roles for issue
                    .stream().filter((role) -> role.getUsers().contains(user)) // filter for roles for current user
                    .collect(Collectors.toList());

            user.getRoles().removeAll(roles); // delete only those

            this.userService.saveUser(user);
            this.issueAuthorRepository.deleteById(issueAuthor.getId());
        }
        return this.getIssueById(issueId);
    }

    /**
     * Comment
     */
    @Override
    @Transactional
    public Issue createComment(UUID issueId, UUID userId, CommentModel commentModel) {
        Issue issue = this.getIssueById(issueId);
        UserEntity user = this.userService.getUserById(userId);

        IssueComment comment = this.issueCommentRepository.save(new IssueComment(commentModel.getText(), issue, user));
        return comment.getIssue();
    }

    @Override
    @Transactional(readOnly = true)
    public IssueComment getCommentById(UUID commentId) {
        return this.issueCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Issue comment with ID %s not found!", commentId)));
    }

    @Override
    @Transactional
    public Issue updateComment(UUID issueId, UUID commentId, UUID userId, CommentModel commentModel) {
        if (commentModel == null)
            throw new RuntimeException("Issue comment to update is null!");
        IssueComment comment = this.existsIssueComment(issueId, commentId, userId);
        comment.updateComment(commentModel.getText());
        comment = this.issueCommentRepository.save(comment);
        return comment.getIssue();
    }

    @Override
    @Transactional
    public Issue updateIssueCommentRating(UUID issueId, UUID commentId, UUID userId, RatingModelRequest ratingModelRequest) {
        IssueComment issueComment = this.getCommentById(commentId);
        UserEntity user = this.userService.getUserById(userId);
        IssueCommentRating issueCommentRating = new IssueCommentRating(issueComment, user, ratingModelRequest.getRating());
        if (this.issueCommentRatingRepository.existsByIdAndRating(issueCommentRating.getId(), issueCommentRating.getRating())) {
            issueCommentRating.setRating(0);
        }
        issueCommentRating = this.issueCommentRatingRepository.save(issueCommentRating);
        return issueCommentRating.getIssueComment().getIssue();
    }

    @Override
    @Transactional
    public Issue deleteComment(UUID issueId, UUID commentId, UUID userId) {
        this.existsIssueComment(issueId, commentId, userId);
        this.issueCommentRepository.deleteById(commentId);
        return this.getIssueById(issueId);
    }

    // Check if Issue comment exists
    private IssueComment existsIssueComment(UUID issueId, UUID commentId, UUID userId) {
        IssueComment issueComment = this.getCommentById(commentId);
        // CORRECT Issue
        if (!issueComment.getIssue().equals(this.getIssueById(issueId)))
            throw new EntityNotFoundException(String.format("Issue comment with id %s does not belong to issue with id %s", commentId, issueId));
        // CORRECT user
        if (!issueComment.getUser().equals(this.userService.getUserById(userId)))
            throw new EntityNotFoundException(String.format("Issue comment with id %s does not belong to user with id %s", commentId, userId));
        return issueComment;
    }

    /**
     * Evidence
     */
    @Override
    @Transactional
    public Issue createEvidence(UUID issueId, UUID userId, EvidenceModel evidenceModel) {
        Issue issue = this.getIssueById(issueId);
        UserEntity user = this.userService.getUserById(userId);

        IssueEvidence issueEvidence = new IssueEvidence(evidenceModel.getTitle(), evidenceModel.getContext(), evidenceModel.getType(), evidenceModel.getSupporting(), evidenceModel.getSource(), issue, user);
        issueEvidence = this.issueEvidenceRepository.save(issueEvidence);
        return issueEvidence.getIssue();
    }

    @Override
    @Transactional(readOnly = true)
    public IssueEvidence getEvidenceById(UUID issueEvidenceId) {
        return this.issueEvidenceRepository.findById(issueEvidenceId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Issue comment with ID %s not found!", issueEvidenceId)));
    }

    @Override
    @Transactional
    public Issue updateEvidence(UUID issueId, UUID evidenceId, UUID userId, EvidenceModel evidenceModel) {
        if (evidenceModel == null)
            throw new RuntimeException("Issue comment to update is null!");
        IssueEvidence issueEvidence = this.existsIssueEvidence(issueId, evidenceId, userId);
        // UPDATE issue evidence
        issueEvidence.updateEvidence(evidenceModel.getTitle(), evidenceModel.getContext(), evidenceModel.getType(), evidenceModel.getSupporting(), evidenceModel.getSource());
        issueEvidence = this.issueEvidenceRepository.save(issueEvidence);
        return issueEvidence.getIssue();
    }

    @Override
    @Transactional
    public Issue updateIssueEvidenceRating(UUID issueId, UUID evidenceID, UUID userId, RatingModelRequest ratingModelRequest) {
        Issue issue = this.getIssueById(issueId);
        IssueEvidence issueEvidence = this.getEvidenceById(evidenceID);
        UserEntity user = this.userService.getUserById(userId);
        IssueEvidenceRating issueEvidenceRating = new IssueEvidenceRating(issueEvidence, user, ratingModelRequest.getRating());
        if (this.issueEvidenceRatingRepository.existsByIdAndRating(issueEvidenceRating.getId(), issueEvidenceRating.getRating())) {
            issueEvidenceRating.setRating(0);
        }
        issueEvidenceRating = this.issueEvidenceRatingRepository.save(issueEvidenceRating);
        return issueEvidenceRating.getIssueEvidence().getIssue();
    }

    @Override
    @Transactional
    public Issue deleteEvidence(UUID issueId, UUID evidenceId, UUID userId) {
        this.existsIssueEvidence(issueId, evidenceId, userId);
        this.issueEvidenceRepository.deleteById(evidenceId);
        return this.getIssueById(issueId);
    }

    private IssueEvidence existsIssueEvidence(UUID issueId, UUID commentId, UUID userId) {
        IssueEvidence issueEvidence = this.getEvidenceById(commentId);
        // CORRECT Issue
        if (!issueEvidence.getIssue().equals(this.getIssueById(issueId)))
            throw new EntityNotFoundException(String.format("Issue comment with id %s does not belong to issue with id %s", commentId, issueId));
        // CORRECT user
        if (!issueEvidence.getUser().equals(this.userService.getUserById(userId)))
            throw new EntityNotFoundException(String.format("Issue comment with id %s does not belong to user with id %s", commentId, userId));
        return issueEvidence;
    }
}