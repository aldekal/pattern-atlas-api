package com.patternatlas.api.repositories;

import com.patternatlas.api.entities.DiscussionTopic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface DiscussionTopicRepository extends CrudRepository<DiscussionTopic, UUID> {
    List<DiscussionTopic> findDiscussionTopicsByImageId(UUID imageId);
}

