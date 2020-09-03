package com.jira.report.controller;

import com.jira.report.model.entity.BacklogEntity;
import com.jira.report.service.BacklogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BacklogController {
    private final BacklogService backlogService;

    public BacklogController(BacklogService backlogService) {
        this.backlogService = backlogService;
    }

    @GetMapping(value = "/backlog")
    public BacklogEntity getBacklog(){
        return backlogService.getBacklog();
    }
}
