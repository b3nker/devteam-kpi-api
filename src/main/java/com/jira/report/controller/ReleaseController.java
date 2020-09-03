package com.jira.report.controller;

import com.jira.report.model.entity.ReleaseEntity;
import com.jira.report.service.ReleaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class ReleaseController {
    private final ReleaseService releaseService;

    public ReleaseController(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }

    @GetMapping(value = "/release")
    public List<ReleaseEntity> getRelease() {
        return releaseService.getRelease();
    }
}
