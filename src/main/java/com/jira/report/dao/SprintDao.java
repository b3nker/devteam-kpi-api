package com.jira.report.dao;

import com.jira.report.config.ReactiveServicesExchangesConfig;
import com.jira.report.config.WebClientInstancesConfig;
import com.jira.report.dao.api.API;
import com.jira.report.model.Sprint;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.Map;

@Repository
public class SprintDao {
    private WebClientInstancesConfig wcic = new WebClientInstancesConfig();
    private ReactiveServicesExchangesConfig rsec = new ReactiveServicesExchangesConfig();
    private API api = new API(wcic.jiraWebClient(rsec));

    private Map<String,Sprint> sprints;

    public void loadSprints(){
        sprints = api.callJiraSprintAPI();
    }

    public Collection<Sprint> getSprints(){
        return sprints.values();
    }

    public Sprint getSprintTeam(String teamName) { return sprints.get(teamName); }

}
