package com.jira.report;

import com.jira.report.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;

@Component
@AllArgsConstructor
@Slf4j
public class JiraReportInitializer {

    private final ReleaseService releaseService;
    private final SprintService sprintService;
    private final BacklogService backlogService;
    private final CollaboratorService collaboratorService;
    private final RetrospectiveService retrospectiveService;
    private final CommentService commentService;

    @PostConstruct
    public void loadDataAtStartup() throws IOException, ParseException {
        StopWatch loadingDataStopWatch = new StopWatch("loadingDataStopWatch");


        loadingDataStopWatch.start("loadSprints");
        sprintService.loadSprints();
        loadingDataStopWatch.stop();

        loadingDataStopWatch.start("loadReleases");
        releaseService.loadReleases();
        loadingDataStopWatch.stop();

        loadingDataStopWatch.start("loadRetrospectives");
        retrospectiveService.loadRetrospectives();
        loadingDataStopWatch.stop();

        loadingDataStopWatch.start("loadCollaborators");
        collaboratorService.loadCollaborators();
        loadingDataStopWatch.stop();

        loadingDataStopWatch.start("loadBacklog");
        backlogService.loadBacklog();
        loadingDataStopWatch.stop();

        log.info("Finished data loading : {}", loadingDataStopWatch.prettyPrint());
    }
}
