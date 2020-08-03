package com.jira.report.controller;

import com.jira.report.model.Backlog;
import com.jira.report.service.BacklogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class BacklogController {
    @Autowired
    private BacklogService backlogService;

    @GetMapping(value = "/backlog")
    public Backlog getBacklog(){
        return this.backlogService.getBacklog();
    }
}
