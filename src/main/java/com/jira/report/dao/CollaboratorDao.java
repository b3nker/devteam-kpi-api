package com.jira.report.dao;

import com.jira.report.config.ReactiveServicesExchangesConfig;
import com.jira.report.config.WebClientInstancesConfig;
import com.jira.report.dao.api.API;
import com.jira.report.model.Collaborator;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class CollaboratorDao {
    private static Map<String,Collaborator> collaboratorsSprint;
    private WebClientInstancesConfig wcic = new WebClientInstancesConfig();
    private ReactiveServicesExchangesConfig rsec = new ReactiveServicesExchangesConfig();
    private API api = new API(wcic.jiraWebClient(rsec));

    public void loadCollaborators(){
        collaboratorsSprint = api.callJiraCollabSprintAPI();
    }

    public Collection<Collaborator> getAllCollaboratorsPerSprint(){
        return collaboratorsSprint.values();
    }
}
