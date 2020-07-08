package com.jiraReportTest.jiraReportTest.Controller;

import com.jiraReportTest.jiraReportTest.Model.Team;
import com.jiraReportTest.jiraReportTest.Service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/")
public class TeamController {

    @Autowired
    private TeamService teamService;
    private String teamName;

    @RequestMapping(value = "/teams/sprint",method = RequestMethod.GET)
    @CrossOrigin(origins ="http://localhost:4200")
    public Collection<Team> getTeamsPerSprint(){
        return this.teamService.getTeamsPerSprint();
    }



    @RequestMapping(value = "/{teamName}/sprint", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:4200")
    public Team getCollaboratorsPerTeamDuringSprint(@PathVariable("teamName") String teamName){
        return this.teamService.getCollaboratorsPerTeamDuringSprint(teamName);
    }

    /*
    @RequestMapping(value = "/teams/week",method = RequestMethod.GET)
    @CrossOrigin(origins ="http://localhost:4200")
    public Collection<Team> getTeamsPerWeek(){
        return this.teamService.getTeamsPerWeek();
    }

    @RequestMapping(value = "/{teamName}/week", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:4200")
    public Team getCollaboratorsPerTeamDuringWeek(@PathVariable("teamName") String teamName){
        return this.teamService.getCollaboratorsPerTeamDuringWeek(teamName);
    }
    */
}
