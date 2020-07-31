package com.jiraReportTest.jirareporttest.dao;

import com.jiraReportTest.jirareporttest.dao.api.API;
import com.jiraReportTest.jirareporttest.model.Backlog;
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
