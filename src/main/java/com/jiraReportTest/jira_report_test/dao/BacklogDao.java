package com.jiraReportTest.jira_report_test.dao;

import com.jiraReportTest.jira_report_test.dao.api.API;
import com.jiraReportTest.jira_report_test.model.Backlog;
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
