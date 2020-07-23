package com.jiraReportTest.jiraReportTest.Dao;

import com.jiraReportTest.jiraReportTest.Dao.API.API;
import com.jiraReportTest.jiraReportTest.Dao.API.JiraAPI;
import com.jiraReportTest.jiraReportTest.Model.Sprint;
import org.springframework.stereotype.Repository;

@Repository
public class SprintDao {
    private static Sprint sprint;

    static{
        sprint = API.callJiraSprintAPI();

    }

    public Sprint getSprint(){
        return this.sprint;
    }

}
