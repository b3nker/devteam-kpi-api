package com.jiraReportTest.jiraReportTest.Dao;

import com.jiraReportTest.jiraReportTest.Model.Backlog;
import com.jiraReportTest.jiraReportTest.Model.Sprint;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;

@Repository
public class BacklogDao {
    private static Backlog backlog;

    static{
        try {
            backlog = JiraAPI.callJiraBacklogAPI();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public Backlog getBacklog(){
        return this.backlog;
    }
}
