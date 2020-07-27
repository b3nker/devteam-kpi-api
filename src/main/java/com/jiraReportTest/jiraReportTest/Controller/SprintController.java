package com.jiraReportTest.jiraReportTest.Controller;

import com.jiraReportTest.jiraReportTest.Model.Sprint;
import com.jiraReportTest.jiraReportTest.Model.Team;
import com.jiraReportTest.jiraReportTest.Service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping("/")
public class SprintController {
    @Autowired
    private SprintService sprintService;

    @RequestMapping(value = "/sprint", method = RequestMethod.GET)
    @CrossOrigin(origins = {
            "http://localhost:4200"
    })
    public Collection<Sprint> getSprintTeam(){
        return this.sprintService.getSprint();
    }



}
