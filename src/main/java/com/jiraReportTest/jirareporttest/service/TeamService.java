package com.jiraReportTest.jirareporttest.service;


import com.jiraReportTest.jirareporttest.dao.TeamDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
