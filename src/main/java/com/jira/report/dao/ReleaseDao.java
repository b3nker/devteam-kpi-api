package com.jira.report.dao;

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
    private final API api;

    public ReleaseDao(API api) {
        this.api = api;
    }

    public void loadReleases() {
        log.info("Starting to construct releases objects");
        try {
            releases = api.getReleases();
            log.info("Finished constructing releases objects");
        } catch (IOException | ParseException e) {
            log.error("Failed to retrieve release file",e);
        }
    }

    public Collection<Release> getReleases(){
        return releases;
    }
}
