package com.jira.report.dao;

import com.jira.report.dao.api.API;
import com.jira.report.model.Retrospective;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.Map;

@Slf4j
@Repository
public class RetrospectiveDao {
    private Map<String, Retrospective> retrospectives;
    private final API api;

    public RetrospectiveDao(API api) {
        this.api = api;
    }

    public void loadRetrospectives(){
        log.info("Starting to construct Retrospective objects");
        retrospectives = api.callJiraRetrospectiveAPI();
        log.info("Finished constructing Retrospective objects");

    }

    public Collection<Retrospective> getRetrospectives() {
        return retrospectives.values();
    }
}
