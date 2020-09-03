package com.jira.report.controller;

import com.jira.report.model.entity.CollaboratorEntity;
import com.jira.report.service.CollaboratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/")
public class CollaboratorController {
    @Autowired
    private CollaboratorService collaboratorService;

    @GetMapping(value = "/sprint/all")
    public Collection<CollaboratorEntity> getAllCollaboratorsPerSprint() {
        return this.collaboratorService.getAllCollaboratorsPerSprint();
    }
}
