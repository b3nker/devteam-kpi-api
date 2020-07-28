package com.jiraReportTest.jiraReportTest.controller;

import com.jiraReportTest.jiraReportTest.model.Retrospective;
import com.jiraReportTest.jiraReportTest.service.RetrospectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/")
public class RetrospectiveController {
    private final static String ORIGINS = "http://localhost:4200";
    @Autowired
    private RetrospectiveService retrospectiveService;

    @RequestMapping(value = "/retrospective",method = RequestMethod.GET)
    @CrossOrigin(origins = ORIGINS)
    public Collection<Retrospective> getRetrospectives(){
        return this.retrospectiveService.getRetrospectives();
    }

}
