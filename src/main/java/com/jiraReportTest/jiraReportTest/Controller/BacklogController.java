package com.jiraReportTest.jiraReportTest.Controller;

import com.jiraReportTest.jiraReportTest.Model.Backlog;
import com.jiraReportTest.jiraReportTest.Model.Collaborator;
import com.jiraReportTest.jiraReportTest.Service.BacklogService;
import com.jiraReportTest.jiraReportTest.Service.CollaboratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
@RestController
@RequestMapping("/")
public class BacklogController {
    private final static String ORIGINS = "http://localhost:4200";
    @Autowired
    private BacklogService backlogService;

    @RequestMapping(value = "/backlog",method = RequestMethod.GET)
    @CrossOrigin(origins = ORIGINS)
    public Backlog getBacklog(){
        return this.backlogService.getBacklog();
    }
}
