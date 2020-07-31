package com.jiraReportTest.jirareporttest.dao;

import com.jiraReportTest.jirareporttest.dao.api.API;
import com.jiraReportTest.jirareporttest.model.Retrospective;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.Map;

@Repository
public class RetrospectiveDao {
    private static Map<String, Retrospective> retrospectives;

    static {
        retrospectives = API.callJiraRetrospectiveAPI();
    }

    public Collection<Retrospective> getRetrospectives() {
        return retrospectives.values();
    }
}
