package com.jira.report.dao;

import com.jira.report.model.entity.RetrospectiveEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetrospectiveDao extends MongoRepository<RetrospectiveEntity, String> {
}
