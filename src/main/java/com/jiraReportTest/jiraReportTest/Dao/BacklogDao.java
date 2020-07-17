package com.jiraReportTest.jiraReportTest.Dao;

import com.jiraReportTest.jiraReportTest.Model.Backlog;
import com.jiraReportTest.jiraReportTest.Model.Sprint;
import org.springframework.stereotype.Repository;

@Repository
public class BacklogDao {
    private static Backlog backlog;

    static{
        backlog = JiraAPI.callJiraBacklogAPI();

    }

    public Backlog getBacklog(){
        return this.backlog;
    }
}
