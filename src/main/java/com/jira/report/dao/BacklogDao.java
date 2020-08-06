package com.jira.report.dao;

import com.jira.report.dao.api.API;
import com.jira.report.model.Backlog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
@Slf4j
@Repository
public class BacklogDao {
    private Backlog backlog;

    private final API api;

    public BacklogDao(API api) {
        this.api = api;
    }

    public void loadBacklog(){
        log.info("Starting to construct Backlog object");
        backlog = api.callJiraBacklogAPI();
        log.info("Finished constructing Backlog object");
    }

    public Backlog getBacklog(){
        return backlog;
    }
}
