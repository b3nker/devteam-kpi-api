package com.jiraReportTest.jiraReportTest.controller;

import com.jiraReportTest.jiraReportTest.model.Collaborator;
import com.jiraReportTest.jiraReportTest.service.CollaboratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
@RestController
@RequestMapping("/")
public class CollaboratorController {
    private final static String ORIGINS = "http://localhost:4200";

    @Autowired
    private CollaboratorService collaboratorService;


    @RequestMapping(value = "/sprint/all",method = RequestMethod.GET)
    @CrossOrigin(origins = ORIGINS)
    public Collection<Collaborator> getAllCollaboratorsPerSprint(){
        return this.collaboratorService.getAllCollaboratorsPerSprint();
    }
    /*


    @RequestMapping(value = "/all/week",method = RequestMethod.GET)
    @CrossOrigin(origins ="http://localhost:4200")
    public Collection<Collaborator> getAllCollaboratorsPerWeek(){
        return this.collaboratorService.getAllCollaboratorsPerWeek();
    }

     */
}
