package com.jira.report.dao;

import com.jira.report.dao.api.API;
import com.jira.report.model.Sprint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.Map;

@Slf4j
@Repository
public class SprintDao {
    private final API api;
    private Map<String,Sprint> sprints;

    public SprintDao(API api) {
        this.api = api;
    }

    public void loadSprints(){
        log.info("Starting to construct Sprints objects");
        sprints = api.callJiraSprintAPI();
        log.info("Finished constructing Sprints objects");
    }

    public Collection<Sprint> getSprints(){
        return sprints.values();

    }

    public Sprint getSprintTeam(String teamName) { return sprints.get(teamName); }

}
