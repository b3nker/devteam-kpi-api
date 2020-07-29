package com.jiraReportTest.jiraReportTest.controller;

import com.jiraReportTest.jiraReportTest.model.Release;
import com.jiraReportTest.jiraReportTest.service.ReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/")
public class ReleaseController {
    private final static String ORIGINS = "http://localhost:4200";
    @Autowired
    private ReleaseService releaseService;

    @RequestMapping(value = "/release",method = RequestMethod.GET)
    public Collection<Release> getRelease(){
        return this.releaseService.getRelease();
    }
}
