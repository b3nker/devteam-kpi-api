package com.jiraReportTest.jirareporttest.dao;

import com.jiraReportTest.jirareporttest.dao.api.API;
import com.jiraReportTest.jirareporttest.model.Collaborator;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class CollaboratorDao {

    private static Map<String,Collaborator> collaboratorsSprint;

    static{
        collaboratorsSprint = API.callJiraCollabSprintAPI();
    }

    public Collection<Collaborator> getAllCollaboratorsPerSprint(){
        return collaboratorsSprint.values();
    }
}
