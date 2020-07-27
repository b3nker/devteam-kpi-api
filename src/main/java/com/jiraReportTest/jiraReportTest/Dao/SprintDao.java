package com.jiraReportTest.jiraReportTest.Dao;

import com.jiraReportTest.jiraReportTest.Dao.API.API;
import com.jiraReportTest.jiraReportTest.Model.Sprint;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class SprintDao {
    private static Collection<Sprint> sprints;

    static{
        sprints = API.callJiraSprintAPI();

    }

    public Collection<Sprint> getSprint(){
        return this.sprints;
    }

}
