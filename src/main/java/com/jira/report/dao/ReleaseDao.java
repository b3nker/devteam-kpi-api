package com.jira.report.dao;

import com.jira.report.config.ReactiveServicesExchangesConfig;
import com.jira.report.config.WebClientInstancesConfig;
import com.jira.report.model.Release;
import com.jira.report.dao.api.API;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

@Repository
@Slf4j
public class ReleaseDao {
    private List<Release> releases;
    private WebClientInstancesConfig wcic = new WebClientInstancesConfig();
    private ReactiveServicesExchangesConfig rsec = new ReactiveServicesExchangesConfig();
    private API api = new API(wcic.jiraWebClient(rsec));


    public void loadReleases() {
        log.info("Start to load releases");
        try {
            releases = api.getReleases();
        } catch (IOException | ParseException e) {
            log.error("Failed to retrieve release file",e);
        }
    }

    public Collection<Release> getReleases(){
        return releases;
    }
}
