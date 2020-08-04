package com.jira.report.dao;

import com.jira.report.dao.api.API;
import com.jira.report.model.Retrospective;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.Map;

@Repository
public class RetrospectiveDao {
    private static Map<String, Retrospective> retrospectives;
    private final API api;

    public RetrospectiveDao(API api) {
        this.api = api;
    }

    public void loadRetrospectives(){
        retrospectives = api.callJiraRetrospectiveAPI();
    }

    public Collection<Retrospective> getRetrospectives() {
        return retrospectives.values();
    }
}
