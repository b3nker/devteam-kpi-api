package com.jiraReportTest.jira_report_test.controller;

import com.jiraReportTest.jira_report_test.model.Retrospective;
import com.jiraReportTest.jira_report_test.service.RetrospectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/")
public class RetrospectiveController {
    @Autowired
    private RetrospectiveService retrospectiveService;

    @GetMapping(value = "/retrospective")
    public Collection<Retrospective> getRetrospectives(){
        return this.retrospectiveService.getRetrospectives();
    }

}
