package com.jiraReportTest.jira_report_test.service;

import com.jiraReportTest.jira_report_test.dao.CollaboratorDao;
import com.jiraReportTest.jira_report_test.model.Collaborator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
@Service
public class CollaboratorService {
    @Autowired
    private CollaboratorDao collaboratorDAO;

    public Collection<Collaborator> getAllCollaboratorsPerSprint(){
        return this.collaboratorDAO.getAllCollaboratorsPerSprint();
    }
}
