package com.jira.report.controller;

import com.jira.report.model.entity.RetrospectiveEntity;
import com.jira.report.service.RetrospectiveService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class RetrospectiveController {
    private final RetrospectiveService retrospectiveService;

    public RetrospectiveController(RetrospectiveService retrospectiveService) {
        this.retrospectiveService = retrospectiveService;
    }

    @GetMapping(value = "/retrospective")
    public List<RetrospectiveEntity> getRetrospectives(){
        return this.retrospectiveService.getRetrospectives();
    }

}
