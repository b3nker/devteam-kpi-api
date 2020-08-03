package com.jira.report.dao;

import com.jira.report.config.ReactiveServicesExchangesConfig;
import com.jira.report.config.WebClientInstancesConfig;
import com.jira.report.dao.api.API;
import com.jira.report.model.Backlog;
import org.springframework.stereotype.Repository;

@Repository
public class BacklogDao {
    private static Backlog backlog;
    private WebClientInstancesConfig wcic = new WebClientInstancesConfig();
    private ReactiveServicesExchangesConfig rsec = new ReactiveServicesExchangesConfig();
    private API api = new API(wcic.jiraWebClient(rsec));

    public void loadBacklog(){
        backlog = api.callJiraBacklogAPI();
    }

    public Backlog getBacklog(){
        return backlog;
    }
}
