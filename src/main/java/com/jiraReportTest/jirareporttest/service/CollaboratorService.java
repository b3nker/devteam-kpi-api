package com.jiraReportTest.jirareporttest.service;

import com.jiraReportTest.jirareporttest.dao.CollaboratorDao;
import com.jiraReportTest.jirareporttest.model.Collaborator;
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
