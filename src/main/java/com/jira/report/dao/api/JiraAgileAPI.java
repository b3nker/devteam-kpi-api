package com.jira.report.dao.api;

import com.jira.report.model.Sprint;
import com.jira.report.model.SprintCommitment;
import com.jira.report.dto.jiraAgileApi.AgileDto;
import com.jira.report.dto.jiraAgileApi.SprintDto;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.jira.report.dao.api.API.*;

public class JiraAgileAPI {
    static final String JIRA_AGILE_API_URL = "https://apriltechnologies.atlassian.net/rest/agile/1.0/";
    static final String ACTIVE_SPRINT = "active";

    private JiraAgileAPI(){}

    public static List<SprintCommitment> getLastlyClosedSprints(int nbSprints, String teamName) {
        /*
        Variables
         */
        int nbSprintsFound = 0;
        boolean lastActiveFound = false;
        String teamNameLC = teamName.toLowerCase();
        List<SprintCommitment> sc = new ArrayList<>();
        String request = JIRA_AGILE_API_URL + "/board/" + PROJECT_BOARD_ID + "/sprint";
        /*
        Logic
         */
        AgileDto agileDto = JiraAgileAPI.connectToJiraAgileAPI(request);
        assert agileDto != null;
        SprintDto[] sprintsDto = agileDto.getValues();
        for (int i = sprintsDto.length-1; i > 0; i--) {
            String sprintName = sprintsDto[i].getName();
            if(!lastActiveFound){
                if (sprintName.toLowerCase().contains(teamNameLC) && ACTIVE_SPRINT.equals(sprintsDto[i].getState())){
                    sc.add(SprintCommitment.builder()
                            .name(sprintsDto[i].getName())
                            .id(sprintsDto[i].getId())
                            .build());
                    nbSprintsFound++;
                    lastActiveFound = true;
                }
            }else{
                if (sprintsDto[i].getName().toLowerCase().contains(teamNameLC)) {
                    sc.add(SprintCommitment.builder()
                            .name(sprintsDto[i].getName())
                            .id(sprintsDto[i].getId())
                            .build());
                    nbSprintsFound++;
                }
            }
        }
        while (nbSprintsFound < nbSprints){
            sc.add(SprintCommitment.builder().build());
            nbSprintsFound++;
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
