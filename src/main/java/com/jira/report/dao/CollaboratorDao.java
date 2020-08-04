package com.jira.report.dao;

import com.jira.report.dao.api.API;
import com.jira.report.model.Collaborator;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class CollaboratorDao {
    private static Map<String,Collaborator> collaboratorsSprint;
    private final API api;

    public CollaboratorDao(API api) {
        this.api = api;
    }

    public void loadCollaborators(){
        collaboratorsSprint = api.callJiraCollabSprintAPI();
    }

    public Collection<Collaborator> getAllCollaboratorsPerSprint(){
        return collaboratorsSprint.values();
    }
}
