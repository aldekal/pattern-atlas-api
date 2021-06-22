package io.github.patternatlas.api.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.DiscussionTopic;

@RepositoryRestResource(exported = false)
public interface DiscussionTopicRepository extends CrudRepository<DiscussionTopic, UUID> {
    List<DiscussionTopic> findDiscussionTopicsByImageId(UUID imageId);
}

