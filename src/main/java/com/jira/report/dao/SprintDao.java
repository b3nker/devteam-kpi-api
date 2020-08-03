package com.jira.report.dao;

import com.jira.report.dao.api.API;
import com.jira.report.model.Sprint;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.Map;

@Repository
public class SprintDao {
    private static Map<String,Sprint> sprints;

    static{
        sprints = API.callJiraSprintAPI();

    }

    public Collection<Sprint> getSprints(){
        return sprints.values();
    }

    public Sprint getSprintTeam(String teamName) { return sprints.get(teamName); }

}
