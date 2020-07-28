package com.jiraReportTest.jiraReportTest.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class TeamController {
    /*
    @Autowired
    private TeamService teamService;

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
