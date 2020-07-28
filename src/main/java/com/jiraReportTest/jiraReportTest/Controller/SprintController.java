package com.jiraReportTest.jiraReportTest.Controller;

import com.jiraReportTest.jiraReportTest.Model.Sprint;
import com.jiraReportTest.jiraReportTest.Model.Team;
import com.jiraReportTest.jiraReportTest.Service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping("/")
public class SprintController {
    private final static String ORIGINS = "http://localhost:4200";
    @Autowired
    private SprintService sprintService;

    @RequestMapping(value = "/sprint", method = RequestMethod.GET)
    @CrossOrigin(origins = ORIGINS)
    public Collection<Sprint> getSprintTeam(){
        return this.sprintService.getSprint();
    }

    @RequestMapping(value = "/sprint/{teamName}", method = RequestMethod.GET)
    @CrossOrigin(origins = ORIGINS)
    public Sprint getCollaboratorsPerTeamDuringSprint(@PathVariable("teamName") String teamName){
        return this.sprintService.getSprintTeam(teamName);
    }

}
