package com.jira.report.service;

import com.jira.report.dao.CollaboratorDao;
import com.jira.report.model.Collaborator;
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
