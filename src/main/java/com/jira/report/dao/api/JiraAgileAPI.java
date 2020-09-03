package com.jira.report.dao.api;

import com.jira.report.config.JiraReportConfigApi;
import com.jira.report.config.JiraReportConfigQuery;
import com.jira.report.dto.agile.AgileDto;
import com.jira.report.dto.agile.SprintDto;
import com.jira.report.model.entity.SprintCommitmentEntity;
import com.jira.report.model.entity.SprintEntity;
import com.jira.report.service.helper.SprintHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class JiraAgileAPI {
    private final WebClient jiraWebClient;
    private final String baseUrl;
    private final String jiraAgileUrl;
    private final String active;
    private static final String BOARD_URI = "/board/";
    private static final String SPRINT_URI = "/sprint";

    JiraAgileAPI(JiraReportConfigQuery jiraReportConfigQuery, WebClient jiraWebClient, JiraReportConfigApi jiraReportConfigApi) {
        this.jiraWebClient = jiraWebClient;
        this.baseUrl = jiraReportConfigApi.getBaseUrl();
        this.jiraAgileUrl = jiraReportConfigApi.getJiraAgileApiUrl();
        this.active = jiraReportConfigQuery.getActive();
    }

    /**
     * Creates a list of SprintCommitment objects
     * Returns a list containing exactly (nbSprints) SprintCommitment object, fills with empty object when not enough data are available.
     * Starts from the lastly active sprint containing team name.
     * @param nbSprints Desired number of SprintCommitment objects in the returned list
     * @param teamName  Desired team from which we want to collect sprints' data (sprint name must contain this label)
     * @param projectBoardId BoardId from which the sprints are retrieved
     * @return A list of SprintCommitment objects
     */
    public List<SprintCommitmentEntity> getLastlyClosedSprints(int nbSprints, String teamName, String projectBoardId) {
        /*
        Variables
         */
        int nbSprintsFound = 0;
        boolean lastActiveFound = false;
        String teamNameLC = teamName.toLowerCase();
        List<SprintCommitmentEntity> sc = new ArrayList<>();
        String request = baseUrl + jiraAgileUrl + BOARD_URI + projectBoardId + SPRINT_URI;
        /*
        Logic
         */
        AgileDto agileDto = connectToJiraAgileAPI(request);
        assert agileDto != null;
        assert nbSprints >= 0;
        SprintDto[] sprintsDto = agileDto.getValues();
        if (nbSprintsFound < nbSprints) {
            for (int i = sprintsDto.length - 1; i > 0; i--) {
                String sprintName = sprintsDto[i].getName();
                if (!lastActiveFound) {
                    if (sprintName.toLowerCase().contains(teamNameLC) && active.equals(sprintsDto[i].getState())) {
                        sc.add(SprintCommitmentEntity.builder()
                                .name(sprintsDto[i].getName())
                                .id(sprintsDto[i].getId())
                                .build());
                        nbSprintsFound++;
                        lastActiveFound = true;
                    }
                } else {
                    if (sprintsDto[i].getName().toLowerCase().contains(teamNameLC) && nbSprintsFound < nbSprints) {
                        sc.add(SprintCommitmentEntity.builder()
                                .name(sprintsDto[i].getName())
                                .id(sprintsDto[i].getId())
                                .build());
                        nbSprintsFound++;
                    }
                }
            }
        }
        while (nbSprintsFound < nbSprints) {
            sc.add(SprintCommitmentEntity.builder().build());
            nbSprintsFound++;
        }
        return sc;
    }

    /**
     * Creates a Sprint object
     * @param teamName Desired team from which we want to collect sprints' data (sprint name must contain this label)
     * @param projectBoardId BoardId from which the sprints are retrieved
     * @return A Sprint object corresponding to the lastly active in the API for the specified team
     */
    public SprintEntity getLastlyActiveTeamSprint(String teamName, String projectBoardId) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        String sprintName = "";
        Long sprintId = 0L;
        String name;
        String teamNameLC = teamName.toLowerCase();
        String request = baseUrl + jiraAgileUrl + BOARD_URI + projectBoardId + SPRINT_URI;
        AgileDto agileDto = connectToJiraAgileAPI(request);
        assert agileDto != null;
        SprintDto[] sprintsDto = agileDto.getValues();
        for (int i = (sprintsDto.length - 1); i > 0; i--) {
            name = sprintsDto[i].getName().toLowerCase();
            if (active.equals(sprintsDto[i].getState()) && name.contains(teamNameLC)) {
                sprintName = sprintsDto[i].getName();
                startDate = SprintHelper.toLocalDateTime(sprintsDto[i].getStartDate());
                endDate = SprintHelper.toLocalDateTime(sprintsDto[i].getEndDate());
                sprintId = sprintsDto[i].getId();
                break;
            }
        }
        return SprintEntity.builder()
                .id(sprintId)
                .name(sprintName)
                .startDate(startDate)
                .endDate(endDate)
                .timeLeft(SprintHelper.timeLeftOnSprint(endDate))
                .totalTime(SprintHelper.durationOfSprint(startDate, endDate))
                .build();
    }

    /**
     * Creates an AgileDto object
     * @param request The request we want to GET data from
     * @return A AgileDto object containing parsed data from the GET request to the API
     */
    public AgileDto connectToJiraAgileAPI(String request) {
        return jiraWebClient
                .get()
                .uri(request)
                .retrieve()
                .bodyToMono(AgileDto.class)
                .block();
    }
}
