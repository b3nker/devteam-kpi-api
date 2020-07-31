package com.jiraReportTest.jira_report_test.controller;

import com.jiraReportTest.jira_report_test.model.Release;
import com.jiraReportTest.jira_report_test.service.ReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/")
public class ReleaseController {
    @Autowired
    private ReleaseService releaseService;

    @GetMapping(value = "/release")
    public Collection<Release> getRelease(){
        return this.releaseService.getRelease();
    }
}