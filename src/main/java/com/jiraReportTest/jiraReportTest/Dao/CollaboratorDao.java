package com.jiraReportTest.jiraReportTest.Dao;

import com.jiraReportTest.jiraReportTest.Dao.API.API;
import com.jiraReportTest.jiraReportTest.Dao.API.JiraAPI;
import com.jiraReportTest.jiraReportTest.Model.Collaborator;
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
        return this.collaboratorsSprint.values();
    }
}
