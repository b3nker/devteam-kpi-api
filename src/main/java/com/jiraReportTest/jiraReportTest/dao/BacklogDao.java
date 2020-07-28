package com.jiraReportTest.jiraReportTest.dao;

import com.jiraReportTest.jiraReportTest.dao.api.API;
import com.jiraReportTest.jiraReportTest.model.Backlog;
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
