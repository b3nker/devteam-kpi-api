package com.jiraReportTest.jiraReportTest.dao;

import com.jiraReportTest.jiraReportTest.dao.api.API;
import com.jiraReportTest.jiraReportTest.model.Retrospective;
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
        return retrospectives.values();
    }
}
