package com.jiraReportTest.jiraReportTest.dao;

import com.jiraReportTest.jiraReportTest.dao.api.API;
import com.jiraReportTest.jiraReportTest.model.Collaborator;
import org.springframework.stereotype.Repository;

import java.util.*;

import static org.apache.http.client.methods.RequestBuilder.put;

@Repository
public class CollaboratorDao {

    private static HashMap<String,Collaborator> collaboratorsSprint;

    static{
        collaboratorsSprint = API.callJiraCollabSprintAPI();
    }

    public Collection<Collaborator> getAllCollaboratorsPerSprint(){
        return collaboratorsSprint.values();
    }
}
