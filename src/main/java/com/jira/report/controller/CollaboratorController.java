package com.jira.report.controller;

import com.jira.report.model.Collaborator;
import com.jira.report.service.CollaboratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
@RestController
@RequestMapping("/")
public class CollaboratorController {
    @Autowired
    private CollaboratorService collaboratorService;


    @GetMapping(value = "/sprint/all")
    public Collection<Collaborator> getAllCollaboratorsPerSprint(){
        return this.collaboratorService.getAllCollaboratorsPerSprint();
    }
}
