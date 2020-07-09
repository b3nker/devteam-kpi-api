package com.jiraReportTest.jiraReportTest.Controller;

import com.jiraReportTest.jiraReportTest.Model.Collaborator;
import com.jiraReportTest.jiraReportTest.Service.CollaboratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
@RestController
@RequestMapping("/")
public class CollaboratorController {

    @Autowired
    private CollaboratorService collaboratorService;


    @RequestMapping(value = "/sprint/all",method = RequestMethod.GET)
    @CrossOrigin(origins ="http://localhost:4200")
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
