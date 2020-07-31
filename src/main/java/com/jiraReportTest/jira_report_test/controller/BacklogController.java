package com.jiraReportTest.jira_report_test.controller;

import com.jiraReportTest.jira_report_test.model.Backlog;
import com.jiraReportTest.jira_report_test.service.BacklogService;
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
