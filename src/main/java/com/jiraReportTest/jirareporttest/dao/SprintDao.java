package com.jiraReportTest.jirareporttest.dao;

import com.jiraReportTest.jirareporttest.dao.api.API;
import com.jiraReportTest.jirareporttest.model.Sprint;
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
