package com.jiraReportTest.jiraReportTest.Controller;

import com.jiraReportTest.jiraReportTest.Model.Retrospective;
import com.jiraReportTest.jiraReportTest.Service.RetrospectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/")
public class RetrospectiveController {
    @Autowired
    private RetrospectiveService retrospectiveService;

    @RequestMapping(value = "/retrospective",method = RequestMethod.GET)
    @CrossOrigin(origins ="http://localhost:4200")
    public Collection<Retrospective> getRetrospectives(){
        return this.retrospectiveService.getRetrospectives();
    }

}
