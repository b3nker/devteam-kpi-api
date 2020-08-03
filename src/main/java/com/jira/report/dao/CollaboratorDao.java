package com.jira.report.dao;

import com.jira.report.dao.api.API;
import com.jira.report.model.Collaborator;
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
