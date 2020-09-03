package com.jira.report.service;

import com.jira.report.dao.ReleaseDao;
import com.jira.report.dao.api.API;
import com.jira.report.model.entity.ReleaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
public class ReleaseService {
    private static Logger LOGGER = LoggerFactory.getLogger(ReleaseService.class);

    private final API api;
    private final ReleaseDao releaseDao;

    public ReleaseService(ReleaseDao releaseDao,
                          API api) {
        this.releaseDao = releaseDao;
        this.api = api;
    }

    @Transactional(rollbackFor = {
            IOException.class,
            ParseException.class
    })
    public void loadReleases() throws IOException, ParseException {
        LOGGER.info("Removing data from the release collection");
        releaseDao.deleteAll();
        LOGGER.info("Starting to construct releases objects");
        try {
            List<ReleaseEntity> releases = api.getReleases();
            releaseDao.saveAll(releases);
            LOGGER.info("Finished constructing releases objects");
        } catch (IOException | ParseException e) {
            LOGGER.error("Failed to retrieve release file", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<ReleaseEntity> getRelease() {
        return releaseDao.findAll();
    }
}
