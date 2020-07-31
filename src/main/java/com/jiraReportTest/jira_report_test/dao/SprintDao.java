package com.jiraReportTest.jira_report_test.dao;

import com.jiraReportTest.jira_report_test.dao.api.API;
import com.jiraReportTest.jira_report_test.model.Sprint;
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
