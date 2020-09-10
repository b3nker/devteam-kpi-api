package com.jira.report.service;

import com.jira.report.dao.CollaboratorDao;
import com.jira.report.dao.api.API;
import com.jira.report.model.entity.CollaboratorEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class CollaboratorService {
    private static Logger LOGGER = LoggerFactory.getLogger(CollaboratorService.class);

    private final API api;
    private final CollaboratorDao collaboratorDAO;

    public CollaboratorService(API api,
                               CollaboratorDao collaboratorDAO) {
        this.api = api;
        this.collaboratorDAO = collaboratorDAO;
    }

    @Transactional
    public void loadCollaborators() {
        LOGGER.info("Starting to construct Collaborator objects");
        Collection<CollaboratorEntity> collaboratorsSprint = api.callJiraCollabSprintAPI().values();
        collaboratorDAO.saveAll(collaboratorsSprint);
        LOGGER.info("Finished constructing Collaborator objects");
    }

    @Transactional(readOnly = true)
    public List<CollaboratorEntity> getAllCollaboratorsPerSprint() {
        return this.collaboratorDAO.findAll();
    }
}
