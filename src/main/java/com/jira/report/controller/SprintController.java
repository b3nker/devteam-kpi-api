package com.jira.report.controller;

import com.jira.report.model.Sprint;
import com.jira.report.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping("/")
public class SprintController {
    @Autowired
    private SprintService sprintService;

    @GetMapping(value = "/sprint")
    public Collection<Sprint> getSprintTeam(){
        return this.sprintService.getSprint();
    }

    @GetMapping(value = "/sprint/{teamName}")
    public Sprint getCollaboratorsPerTeamDuringSprint(@PathVariable("teamName") String teamName){
        return this.sprintService.getSprintTeam(teamName);
    }

}
