package com.jira.report.dao;

import com.jira.report.dao.api.API;
import com.jira.report.model.Retrospective;
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
