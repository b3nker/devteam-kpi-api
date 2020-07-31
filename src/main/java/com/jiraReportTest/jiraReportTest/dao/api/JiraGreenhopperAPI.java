package com.jiraReportTest.jiraReportTest.dao.api;

import com.jiraReportTest.jiraReportTest.dto.jiraGreenhopper.ContentsDto;
import com.jiraReportTest.jiraReportTest.dto.jiraGreenhopper.JiraGreenHopperDto;
import com.jiraReportTest.jiraReportTest.model.SprintCommitment;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.jiraReportTest.jiraReportTest.dao.api.API.API_TOKEN;
import static com.jiraReportTest.jiraReportTest.dao.api.API.USERNAME;

public class JiraGreenhopperAPI {
    final static String JIRA_GREENHOPPER_URL = "https://apriltechnologies.atlassian.net/rest/greenhopper/1.0/";

    private JiraGreenhopperAPI(){}
    /* Returns 4 information on a sprint
     * 0: initialCommitment
     * 1: finalCommitment
     * 2: addedWork
     * 3: completedWork
     */
    public static List<String> getIssueKeys(SprintCommitment s, String boardId){
        String request = JIRA_GREENHOPPER_URL + "rapid/charts/sprintreport" + "?rapidViewId=" + boardId + "&sprintId=" + s.getId();
        /*
        Logic
         */
        WebClient webClient = WebClient.builder()
                .filter(ExchangeFilterFunctions.basicAuthentication(USERNAME, API_TOKEN))
                .defaultHeader("Accept", "application/json")
                .build();
        JiraGreenHopperDto greenHopperDto = webClient
                .get()
                .uri(request)
                .retrieve()
                .bodyToMono(JiraGreenHopperDto.class)
                .block();
        assert greenHopperDto != null;
        Set<String> addedIssues = greenHopperDto.getContents().getIssueKeysAddedDuringSprint().keySet();
        return new ArrayList<>(addedIssues);
    }

    public static double[] getCommitment(SprintCommitment s, String boardId) {
        /*
        Variables
         */
        double[] commitment = new double[4];
        double initialCommitment = 0;
        double finalCommitment = 0;
        double addedWork = 0;
        double completedWork = 0;
        String request = JIRA_GREENHOPPER_URL + "rapid/charts/sprintreport" + "?rapidViewId=" + boardId + "&sprintId=" + s.getId();
        /*
        Logic
         */
        WebClient webClient = WebClient.builder()
                .filter(ExchangeFilterFunctions.basicAuthentication(USERNAME, API_TOKEN))
                .defaultHeader("Accept", "application/json")
                .build();
        JiraGreenHopperDto greenHopperDto = webClient
                .get()
                .uri(request)
                .retrieve()
                .bodyToMono(JiraGreenHopperDto.class)
                .block();
        ContentsDto contents = greenHopperDto.getContents();
        Set<String> addedIssues = contents.getIssueKeysAddedDuringSprint().keySet();
        int completedIssuesEstimateSum = contents.getCompletedIssuesEstimateSum().getValue();
        int completedIssuesInitialEstimateSum = contents.getCompletedIssuesInitialEstimateSum().getValue();
        int issuesNotCompletedEstimateSum = contents.getIssuesNotCompletedEstimateSum().getValue();
        int issuesNotCompletedInitialEstimateSum = contents.getIssuesNotCompletedInitialEstimateSum().getValue();
        int allIssuesEstimateSum = contents.getAllIssuesEstimateSum().getValue();
        completedWork += completedIssuesEstimateSum;
        initialCommitment += completedIssuesEstimateSum;
        initialCommitment += completedIssuesInitialEstimateSum;
        initialCommitment += issuesNotCompletedEstimateSum;
        initialCommitment += issuesNotCompletedInitialEstimateSum;
        finalCommitment += allIssuesEstimateSum;
        //Added issues
        for (String issueKey: addedIssues) {
            addedWork += JiraAPI.getStoryPoint(issueKey);
        }
        commitment[0] = initialCommitment;
        commitment[1] = finalCommitment;
        commitment[2] = addedWork;
        commitment[3] = completedWork;
        return commitment;
    }
}
