package com.jira.report.service;

import com.jira.report.dao.RetrospectiveDao;
import com.jira.report.dao.api.API;
import com.jira.report.model.entity.RetrospectiveEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class RetrospectiveService {
    private static Logger LOGGER = LoggerFactory.getLogger(RetrospectiveService.class);

    private final RetrospectiveDao retrospectiveDao;
    private final API api;

    public RetrospectiveService(RetrospectiveDao retrospectiveDao,
                                API api) {
        this.retrospectiveDao = retrospectiveDao;
        this.api = api;
    }

    @Transactional
    public void loadRetrospectives(){
        LOGGER.info("Starting to construct Retrospective objects");
        Collection<RetrospectiveEntity> retrospectives = api.callJiraRetrospectiveAPI().values();
        retrospectiveDao.saveAll(retrospectives);
        LOGGER.info("Finished constructing Retrospective objects");
    }

    @Transactional(readOnly = true)
    public List<RetrospectiveEntity> getRetrospectives(){
        return retrospectiveDao.findAll();
    }
}
