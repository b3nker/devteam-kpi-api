package com.jira.report.dao;

import com.jira.report.model.entity.SprintEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface SprintDao extends MongoRepository<SprintEntity, Long> {
    Collection<SprintEntity> findByTeamName(String teamName);
}
