package com.jiraReportTest.jiraReportTest.Dao;

import com.jiraReportTest.jiraReportTest.Dao.API.API;
import com.jiraReportTest.jiraReportTest.Model.Sprint;
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
        return this.sprints.values();
    }

    public Sprint getSprintTeam(String teamName) { return this.sprints.get(teamName); }

}
