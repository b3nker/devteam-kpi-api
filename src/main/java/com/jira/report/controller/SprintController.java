package com.jira.report.controller;

import com.jira.report.model.entity.SprintEntity;
import com.jira.report.service.SprintService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
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

    @ApiResponse(code = 200, message = "Sprints for all team retrieved")
    @ApiOperation(value = "Get all sprints of all teams")
    @GetMapping(value = "/sprint")
    public Collection<SprintEntity> getSprintTeam() {
        return sprintService.getSprint();
    }

    @ApiResponse(code = 200, message = "Sprints for a team retrieved")
    @ApiOperation(value = "Get all sprints for a team")
    @GetMapping(value = "/sprint/{teamName}")
    public Collection<SprintEntity> getAllSprintForATeam(@PathVariable("teamName") String teamName) {
        return sprintService.getSprintsTeam(teamName);
    }

    @ApiResponse(code = 200, message = "Last sprint retrieved")
    @ApiOperation(value = "Get the last sprint for a team")
    @GetMapping(value = "/sprint/{teamName}/last")
    public SprintEntity getLastSprintOfTeam(@PathVariable("teamName") String teamName) {
        return sprintService.getSprintTeam(teamName);
    }
}
