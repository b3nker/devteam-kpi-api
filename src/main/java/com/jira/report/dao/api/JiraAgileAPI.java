package com.jira.report.dao.api;

import com.jira.report.config.JiraReportConfigApi;
import com.jira.report.config.JiraReportConfigQuery;
import com.jira.report.model.Sprint;
import com.jira.report.model.SprintCommitment;
import com.jira.report.dto.agile.AgileDto;
import com.jira.report.dto.agile.SprintDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

    public List<SprintCommitment> getLastlyClosedSprints(int nbSprints, String teamName, String projectBoardId) {
        /*
        Variables
         */
        int nbSprintsFound = 0;
        boolean lastActiveFound = false;
        String teamNameLC = teamName.toLowerCase();
        List<SprintCommitment> sc = new ArrayList<>();
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
                        sc.add(SprintCommitment.builder()
                                .name(sprintsDto[i].getName())
                                .id(sprintsDto[i].getId())
                                .build());
                        nbSprintsFound++;
                        lastActiveFound = true;
                    }
                } else {
                    if (sprintsDto[i].getName().toLowerCase().contains(teamNameLC) && nbSprintsFound < nbSprints) {
                        sc.add(SprintCommitment.builder()
                                .name(sprintsDto[i].getName())
                                .id(sprintsDto[i].getId())
                                .build());
                        nbSprintsFound++;
                    }
                }
            }
        }
        while (nbSprintsFound < nbSprints) {
            sc.add(SprintCommitment.builder().build());
            nbSprintsFound++;
        }
        return sc;
    }

    /* Method that returns the lastly active sprint that contains teamName in its name
     *
     */
    public Sprint getLastlyActiveTeamSprint(String teamName, String projectBoardId) {
        String startDate = "";
        String endDate = "";
        String sprintName = "";
        int sprintId = 0;
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
                startDate = sprintsDto[i].getStartDate();
                endDate = sprintsDto[i].getEndDate();
                sprintId = sprintsDto[i].getId();
                break;
            }
        }
        return Sprint.builder()
                .id(sprintId)
                .name(sprintName)
                .startDate(Sprint.toLocalDateTime(startDate))
                .endDate(Sprint.toLocalDateTime(endDate))
                .build();
    }

    /* Method that connect to Jira Agile API
     * GET on the given request
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
