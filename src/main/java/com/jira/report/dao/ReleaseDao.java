package com.jira.report.dao;

import com.jira.report.model.entity.ReleaseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseDao extends MongoRepository<ReleaseEntity, String> {
}
