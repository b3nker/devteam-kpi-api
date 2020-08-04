package com.jira.report.dao;

import com.jira.report.dao.api.API;
import com.jira.report.model.Sprint;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.Map;

@Repository
public class SprintDao {
    private final API api;
    private Map<String,Sprint> sprints;

    public SprintDao(API api) {
        this.api = api;
    }

    public void loadSprints(){
        sprints = api.callJiraSprintAPI();
    }

    public Collection<Sprint> getSprints(){
        return sprints.values();
    }

    public Sprint getSprintTeam(String teamName) { return sprints.get(teamName); }

}
