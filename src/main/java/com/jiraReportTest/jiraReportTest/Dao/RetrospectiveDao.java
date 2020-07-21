package com.jiraReportTest.jiraReportTest.Dao;

import com.jiraReportTest.jiraReportTest.Model.Retrospective;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Repository
public class RetrospectiveDao {
    private static HashMap<String,Retrospective> retrospectives;

    static{
        try {
            retrospectives = JiraAPI.callJiraRestrospectiveAPI();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public Collection<Retrospective> getRetrospectives(){
        return this.retrospectives.values();
    }
}
