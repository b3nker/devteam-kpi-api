package com.jiraReportTest.jiraReportTest.dao;

import com.jiraReportTest.jiraReportTest.dao.api.API;
import com.jiraReportTest.jiraReportTest.model.Sprint;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.HashMap;

@Repository
public class SprintDao {
    private static HashMap<String,Sprint> sprints;

    static{
        sprints = API.callJiraSprintAPI();

    }

    public Collection<Sprint> getSprints(){
        return sprints.values();
    }

    public Sprint getSprintTeam(String teamName) { return sprints.get(teamName); }

}
