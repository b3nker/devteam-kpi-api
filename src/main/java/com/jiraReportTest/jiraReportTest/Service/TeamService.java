package com.jiraReportTest.jiraReportTest.Service;


import com.jiraReportTest.jiraReportTest.Dao.TeamDao;
import com.jiraReportTest.jiraReportTest.Model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TeamService {
    @Autowired
    private TeamDao teamDao;
    /*
    public Collection<Team> getTeamsPerSprint(){
        return this.teamDao.getTeamsPerSprint();
    }

    public Team getCollaboratorsPerTeamDuringSprint(String teamName){
        return this.teamDao.getCollaboratorsPerTeamDuringSprint(teamName);
    }
    /*

    public Team getCollaboratorsPerTeamDuringWeek(String teamName){
        return this.teamDao.getCollaboratorsPerTeamDuringWeek(teamName);
    }

    public Collection<Team> getTeamsPerWeek(){
        return this.teamDao.getTeamsPerWeek();
    }

     */
}
