package com.jiraReportTest.jirareporttest.controller;

import com.jiraReportTest.jirareporttest.model.Collaborator;
import com.jiraReportTest.jirareporttest.service.CollaboratorService;
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
