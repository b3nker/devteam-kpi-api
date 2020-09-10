package com.jira.report.service;

import com.jira.report.dao.SprintDao;
import com.jira.report.dao.api.API;
import com.jira.report.model.entity.SprintEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class SprintService {
    private static Logger LOGGER = LoggerFactory.getLogger(SprintService.class);

    private final API api;
    private final SprintDao sprintDao;

    public SprintService(SprintDao sprintDao,
                         API api) {
        this.sprintDao = sprintDao;
        this.api = api;
    }

    @Transactional
    public void loadSprints() {
        LOGGER.info("Starting to construct Sprints objects");
        Collection<SprintEntity> sprints = api.callJiraSprintAPI().values();
        sprintDao.saveAll(sprints);
        LOGGER.info("Finished constructing Sprints objects");
    }

    @Transactional(readOnly = true)
    public List<SprintEntity> getSprint() {
        return sprintDao.findAll();
    }

    @Transactional(readOnly = true)
    public Collection<SprintEntity> getSprintsTeam(String teamName) {
        return sprintDao.findByTeamName(teamName);
    }

    @Transactional(readOnly = true)
    public SprintEntity getSprintTeam(String teamName) {
        return sprintDao.findByTeamName(teamName)
                .stream()
                .filter(sprintEntity -> sprintEntity.getEndDate().isAfter(LocalDateTime.now()))
                .findFirst()
                .get();
    }
}

