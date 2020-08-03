package com.jira.report.dao;

import com.jira.report.dao.api.API;
import com.jira.report.model.Backlog;
import org.springframework.stereotype.Repository;

@Repository
public class BacklogDao {
    private static Backlog backlog;

    static{
        backlog = API.callJiraBacklogAPI();

    }

    public Backlog getBacklog(){
        return backlog;
    }
}
