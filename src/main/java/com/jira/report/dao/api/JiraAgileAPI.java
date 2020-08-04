package com.jira.report.dao.api;

import com.jira.report.config.JiraReportConfigApi;
import com.jira.report.config.JiraReportConfigQuery;
import com.jira.report.model.Sprint;
import com.jira.report.model.SprintCommitment;
import com.jira.report.dto.jiraAgileApi.AgileDto;
import com.jira.report.dto.jiraAgileApi.SprintDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.jira.report.dao.api.API.*;

@Service
public class JiraAgileAPI {
    private final WebClient jiraWebClient;
    private final String baseUrl;
    private final String jiraAgileUrl;
    private final String active;

    JiraAgileAPI(JiraReportConfigQuery jiraReportConfigQuery, WebClient jiraWebClient, JiraReportConfigApi jiraReportConfigApi){
        this.jiraWebClient = jiraWebClient;
        this.baseUrl = jiraReportConfigApi.getBaseUrl();
        this.jiraAgileUrl = jiraReportConfigApi.getJiraAgileApiUrl();
        this.active = jiraReportConfigQuery.getActive();
    }

    public List<SprintCommitment> getLastlyClosedSprints(int nbSprints, String teamName) {
        /*
        Variables
         */
        int nbSprintsFound = 0;
        boolean lastActiveFound = false;
        String teamNameLC = teamName.toLowerCase();
        List<SprintCommitment> sc = new ArrayList<>();
        String request = baseUrl + jiraAgileUrl + "/board/" + PROJECT_BOARD_ID + "/sprint";
        /*
        Logic
         */
        AgileDto agileDto = connectToJiraAgileAPI(request);
        assert agileDto != null;
        SprintDto[] sprintsDto = agileDto.getValues();
        for (int i = sprintsDto.length-1; i > 0; i--) {
            String sprintName = sprintsDto[i].getName();
            if(!lastActiveFound){
                if (sprintName.toLowerCase().contains(teamNameLC) && active.equals(sprintsDto[i].getState())){
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
    public Sprint getLastlyActiveSprint() {
        String startDate = "";
        String endDate = "";
        String sprintName = "";
        int sprintId = 0;
        String request = baseUrl + jiraAgileUrl + "board/" + PROJECT_BOARD_ID + "/sprint";
        AgileDto agileDto = connectToJiraAgileAPI(request);
        assert agileDto != null;
        SprintDto[] sprintsDto = agileDto.getValues();
        for (SprintDto s: sprintsDto) {
            if (s.getState().contains(active)) {
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
    public Sprint getLastlyActiveTeamSprint(String teamName){
        String startDate = "";
        String endDate = "";
        String sprintName = "";
        int sprintId = 0;
        String name;
        String teamNameLC = teamName.toLowerCase();
        String request = baseUrl + jiraAgileUrl + "board/" + PROJECT_BOARD_ID + "/sprint";
        AgileDto agileDto = connectToJiraAgileAPI(request);
        assert agileDto != null;
        SprintDto[] sprintsDto = agileDto.getValues();
        for (SprintDto s: sprintsDto) {
            name = s.getName().toLowerCase();
            if (active.equals(s.getState()) && name.contains(teamNameLC)) {
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
    public AgileDto connectToJiraAgileAPI(String request) {
        return jiraWebClient
                .get()
                .uri(request)
                .retrieve()
                .bodyToMono(AgileDto.class)
                .block();
    }
}
