package com.jiraReportTest.jiraReportTest.Service;

import com.jiraReportTest.jiraReportTest.Dao.CollaboratorDao;
import com.jiraReportTest.jiraReportTest.Model.Collaborator;
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
    /*
    public Collection<Collaborator> getAllCollaboratorsPerWeek(){
        return this.collaboratorDAO.getAllCollaboratorsPerWeek();
    }

     */
}
