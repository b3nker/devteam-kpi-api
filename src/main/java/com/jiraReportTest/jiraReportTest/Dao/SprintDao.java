package com.jiraReportTest.jiraReportTest.Dao;

import com.jiraReportTest.jiraReportTest.Model.Sprint;
import com.jiraReportTest.jiraReportTest.Model.Team;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class SprintDao {
    private static Sprint sprint;

    static{
        sprint = JiraAPI.callJiraSprintAPI();

    }

    public Sprint getSprint(){
        return this.sprint;
    }

}
