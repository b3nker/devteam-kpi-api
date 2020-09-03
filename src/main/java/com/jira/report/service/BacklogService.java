package com.jira.report.service;

import com.jira.report.dao.BacklogDao;
import com.jira.report.dao.api.API;
import com.jira.report.model.entity.BacklogEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BacklogService {
    private static Logger LOGGER = LoggerFactory.getLogger(BacklogService.class);

    private final BacklogDao backlogDao;
    private final API api;

    public BacklogService(BacklogDao backlogDao,
                          API api) {
        this.backlogDao = backlogDao;
        this.api = api;
    }

    @Transactional
    public void loadBacklog(){
        LOGGER.info("Removing data from the backlog collection");
        backlogDao.deleteAll();
        LOGGER.info("Starting to construct Backlog object");
        BacklogEntity backlog = api.callJiraBacklogAPI();
        backlogDao.save(backlog);
        LOGGER.info("Finished constructing Backlog object");
    }

    @Transactional(readOnly = true)
    public BacklogEntity getBacklog(){
        //Only one BacklogEntity should be present in the database
        return backlogDao.findAll().get(0);
    }
}
