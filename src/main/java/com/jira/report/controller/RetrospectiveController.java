package com.jira.report.controller;

import com.jira.report.model.Retrospective;
import com.jira.report.service.RetrospectiveService;
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
