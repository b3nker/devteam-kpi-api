package com.jiraReportTest.jiraReportTest.controller;

import com.jiraReportTest.jiraReportTest.model.Backlog;
import com.jiraReportTest.jiraReportTest.service.BacklogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class BacklogController {
    @Autowired
    private BacklogService backlogService;

    @RequestMapping(value = "/backlog",method = RequestMethod.GET)
    public Backlog getBacklog(){
        return this.backlogService.getBacklog();
    }
}
