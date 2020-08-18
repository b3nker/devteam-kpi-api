package com.jira.report;

import com.jira.report.dao.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;

@Component
@AllArgsConstructor
@Slf4j
public class JiraReportInitializer {

    private final ReleaseDao releaseDao;
    private final SprintDao sprintDao;
    private final BacklogDao backlogDao;
    private final CollaboratorDao collaboratorDao;
    private final RetrospectiveDao retrospectiveDao;


    @PostConstruct
    public void loadDataAtStartup() {
        StopWatch loadingDataStopWatch = new StopWatch("loadingDataStopWatch");

        loadingDataStopWatch.start("loadReleases");
        releaseDao.loadReleases();
        loadingDataStopWatch.stop();

        loadingDataStopWatch.start("loadSprints");
        sprintDao.loadSprints();
        loadingDataStopWatch.stop();

        loadingDataStopWatch.start("loadBacklog");
        backlogDao.loadBacklog();
        loadingDataStopWatch.stop();

        loadingDataStopWatch.start("loadCollaborators");
        collaboratorDao.loadCollaborators();
        loadingDataStopWatch.stop();


        loadingDataStopWatch.start("loadRetrospectives");
        retrospectiveDao.loadRetrospectives();
        loadingDataStopWatch.stop();



        log.info("Finished data loading : {}", loadingDataStopWatch.prettyPrint());
    }


}
