package com.jira.report.controller;

import com.jira.report.model.entity.SprintEntity;
import com.jira.report.service.SprintService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;


@RestController
@Api(value = "Sprint endpoints")
public class SprintController {
    private final SprintService sprintService;

    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @GetMapping(value = "/sprint")
    public Collection<SprintEntity> getSprintTeam() {
        return sprintService.getSprint();
    }

    @GetMapping(value = "/sprint/{teamName}")
    public Collection<SprintEntity> getAllSprintForATeam(@PathVariable("teamName") String teamName) {
        return sprintService.getSprintsTeam(teamName);
    }

    @GetMapping(value = "/sprint/{teamName}/last")
    public SprintEntity getLastSprintOfTeam(@PathVariable("teamName") String teamName) {
        return sprintService.getSprintTeam(teamName);
    }
}
