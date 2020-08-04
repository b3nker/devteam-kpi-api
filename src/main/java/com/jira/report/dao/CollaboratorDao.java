package com.jira.report.dao;

import com.jira.report.dao.api.API;
import com.jira.report.model.Collaborator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.*;

@Slf4j
@Repository
public class CollaboratorDao {
    private static Map<String,Collaborator> collaboratorsSprint;
    private final API api;

    public CollaboratorDao(API api) {
        this.api = api;
    }

    public void loadCollaborators(){
        log.info("Starting to construct Collaborator objects");
        collaboratorsSprint = api.callJiraCollabSprintAPI();
        log.info("Finished constructing Collaborator objects");

    }

    public Collection<Collaborator> getAllCollaboratorsPerSprint(){
        return collaboratorsSprint.values();
    }
}
