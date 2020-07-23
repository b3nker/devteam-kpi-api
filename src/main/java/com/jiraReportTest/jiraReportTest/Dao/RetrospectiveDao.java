package com.jiraReportTest.jiraReportTest.Dao;

import com.jiraReportTest.jiraReportTest.Dao.API.API;
import com.jiraReportTest.jiraReportTest.Model.Retrospective;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;

@Repository
public class RetrospectiveDao {
    private static HashMap<String, Retrospective> retrospectives;

    static {
        retrospectives = API.callJiraRetrospectiveAPI();
    }

    public Collection<Retrospective> getRetrospectives() {
        return this.retrospectives.values();
    }
}
