package com.jiraReportTest.jiraReportTest.Dao;

import com.jiraReportTest.jiraReportTest.Model.Collaborator;
import org.springframework.stereotype.Repository;

import java.util.*;

import static org.apache.http.client.methods.RequestBuilder.put;

@Repository
public class CollaboratorDao {

    private static HashMap<String,Collaborator> collaboratorsSprint;
    /*
    private static HashMap<String,Collaborator> collaboratorsWeek;
     */
    static{
        collaboratorsSprint = JiraAPI.callJiraCollabSprintAPI();
        /*
        collaboratorsWeek = JiraAPI.callJiraCollabWeekAPI();

         */
    }

    public Collection<Collaborator> getAllCollaboratorsPerSprint(){
        return this.collaboratorsSprint.values();
    }
    /*
    public Collection<Collaborator> getAllCollaboratorsPerWeek(){
        return this.collaboratorsWeek.values();
    }

     */
}
