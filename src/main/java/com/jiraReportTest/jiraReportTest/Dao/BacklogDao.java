package com.jiraReportTest.jiraReportTest.Dao;

import com.jiraReportTest.jiraReportTest.Dao.API.API;
import com.jiraReportTest.jiraReportTest.Dao.API.JiraAPI;
import com.jiraReportTest.jiraReportTest.Model.Backlog;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;

@Repository
public class BacklogDao {
    private static Backlog backlog;

    static{
        try {
            backlog = API.callJiraBacklogAPI();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public Backlog getBacklog(){
        return this.backlog;
    }
}
