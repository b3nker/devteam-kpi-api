package com.jiraReportTest.jiraReportTest.Dao;

import com.jiraReportTest.jiraReportTest.Model.Collaborator;
import com.jiraReportTest.jiraReportTest.Model.Team;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;

@Repository
public class TeamDao {
    private static HashMap<String, Team> teamsSprint;
    /*
    private static HashMap<String,Team> teamsWeek;
     */
    static{
        teamsSprint = JiraAPI.callJiraSprintTeamAPI();
        /*
        teamsWeek = JiraAPI.callJiraWeekTeamAPI();

         */
    }

    public Collection<Team> getTeamsPerSprint(){
        return this.teamsSprint.values();
    }



    public Team getCollaboratorsPerTeamDuringSprint(String teamName){
       return this.teamsSprint.get(teamName);
    }

    /*

    public Collection<Team> getTeamsPerWeek(){
        return this.teamsWeek.values();
    }
    public Team getCollaboratorsPerTeamDuringWeek(String teamName){
        return this.teamsWeek.get(teamName);

    }

     */
}
