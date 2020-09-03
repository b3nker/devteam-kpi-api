package com.jira.report.dao;

import com.jira.report.model.entity.CollaboratorEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollaboratorDao extends MongoRepository<CollaboratorEntity, String> {
}
