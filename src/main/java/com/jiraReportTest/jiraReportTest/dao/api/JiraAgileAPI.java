package com.jiraReportTest.jiraReportTest.dao.api;

import com.jiraReportTest.jiraReportTest.dto.jiraAgileApi.AgileDto;
import com.jiraReportTest.jiraReportTest.dto.jiraAgileApi.SprintDto;
import com.jiraReportTest.jiraReportTest.model.Sprint;
import com.jiraReportTest.jiraReportTest.model.SprintCommitment;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDateTime;
import static com.jiraReportTest.jiraReportTest.dao.api.API.*;

public class JiraAgileAPI {
    final static String JIRA_AGILE_API_URL = "https://apriltechnologies.atlassian.net/rest/agile/1.0/";
    final static String ACTIVE_SPRINT = "active";

    /* Returns an array of size (nbSprints) of the lastly closed sprints including the lastly active sprint
     */
    public static SprintCommitment[] getLastlyClosedSprints(int nbSprints) {
        /*
        Variables
         */
        SprintCommitment[] sc = new SprintCommitment[nbSprints];
        String sprintName;
        int sprintId;
        String request = JIRA_AGILE_API_URL + "/board/" + PROJECT_BOARD_ID + "/sprint";
        int lastlyActiveSprintIndex = -1;
        /*
        Logic
         */
        AgileDto agileDto = JiraAgileAPI.connectToJiraAgileAPI(request);
        assert agileDto != null;
        SprintDto[] sprintsDto = agileDto.getValues();
        for (int i = 0; i < sprintsDto.length; i++) {
            if (ACTIVE_SPRINT.equals(sprintsDto[i].getState())) {
                lastlyActiveSprintIndex = i;
            }
        }
        for (int i = 0; i < nbSprints; i++){
            SprintDto s = sprintsDto[lastlyActiveSprintIndex-i];
            sprintName = s.getName();
            sprintId = s.getId();
            sc[i] = SprintCommitment.builder()
                    .name(sprintName)
                    .id(sprintId)
                    .build();
        }
        return sc;
    }

    /* Method that returns the lastly active sprint in JIRA Agile API
     * A sprint is active if
     */
    public static Sprint getLastlyActiveSprint() {
        String startDate = "";
        String endDate = "";
        String sprintName = "";
        int sprintId = 0;
        String request = JIRA_AGILE_API_URL + "board/" + PROJECT_BOARD_ID + "/sprint";
        AgileDto agileDto = JiraAgileAPI.connectToJiraAgileAPI(request);
        assert agileDto != null;
        SprintDto[] sprintsDto = agileDto.getValues();
        for (SprintDto s: sprintsDto) {
            if (s.getState().contains(ACTIVE_SPRINT)) {
                sprintName = s.getName();
                startDate = s.getStartDate();
                endDate = s.getEndDate();
                sprintId = s.getId();
                break;
            }
        }
        LocalDateTime ldStart = Sprint.toLocalDateTime(startDate);
        LocalDateTime ldEnd = Sprint.toLocalDateTime(endDate);
        return Sprint.builder()
                .id(sprintId)
                .name(sprintName)
                .startDate(ldStart)
                .endDate(ldEnd)
                .timeLeft(Sprint.timeLeftOnSprint(ldEnd))
                .totalTime(Sprint.durationOfSprint(ldStart, ldEnd))
                .build();
    }

    /* Method that returns the lastly active sprint that contains teamName in its name
     *
     */
    public static Sprint getLastlyActiveTeamSprint(String teamName){
        String startDate = "";
        String endDate = "";
        String sprintName = "";
        int sprintId = 0;
        String name;
        String teamNameLC = teamName.toLowerCase();
        String request = JIRA_AGILE_API_URL + "board/" + PROJECT_BOARD_ID + "/sprint";
        AgileDto agileDto = JiraAgileAPI.connectToJiraAgileAPI(request);
        assert agileDto != null;
        SprintDto[] sprintsDto = agileDto.getValues();
        for (SprintDto s: sprintsDto) {
            name = s.getName().toLowerCase();
            if (ACTIVE_SPRINT.equals(s.getState()) && name.contains(teamNameLC)) {
                sprintName = s.getName();
                startDate = s.getStartDate();
                endDate = s.getEndDate();
                sprintId = s.getId();
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
    public static AgileDto connectToJiraAgileAPI(String request){
        WebClient webClient = WebClient.builder()
                .filter(ExchangeFilterFunctions.basicAuthentication(USERNAME, API_TOKEN))
                .defaultHeader("Accept", "application/json")
                .build();
        return webClient
                .get()
                .uri(request)
                .retrieve()
                .bodyToMono(AgileDto.class)
                .block();
    }
}
