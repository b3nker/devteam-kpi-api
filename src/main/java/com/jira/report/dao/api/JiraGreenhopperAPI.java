package com.jira.report.dao.api;

import com.jira.report.model.SprintCommitment;
import com.jira.report.dto.jiraGreenhopper.ContentsDto;
import com.jira.report.dto.jiraGreenhopper.JiraGreenHopperDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.jira.report.dao.api.API.*;

@Service
public class JiraGreenhopperAPI {
    static final String JIRA_GREENHOPPER_URL = "rest/greenhopper/1.0/";
    private JiraAPI jiraAPI;

    public JiraGreenhopperAPI(WebClient jiraWebClient){
        this.jiraAPI = new JiraAPI(jiraWebClient);
    }
    /* Returns 4 information on a sprint
     * 0: initialCommitment
     * 1: finalCommitment
     * 2: addedWork
     * 3: completedWork
     */
    public List<String> getIssueKeys(SprintCommitment s, String boardId){
        String request = BASE_URL + JIRA_GREENHOPPER_URL + "rapid/charts/sprintreport" + "?rapidViewId=" + boardId + "&sprintId=" + s.getId();
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

    public double[] getCommitment(SprintCommitment s, String boardId) {
        /*
        Variables
         */
        double[] commitment = new double[4];
        double initialCommitment = 0;
        double finalCommitment = 0;
        double addedWork = 0;
        double completedWork = 0;
        String request = BASE_URL + JIRA_GREENHOPPER_URL + "rapid/charts/sprintreport" + "?rapidViewId=" + boardId + "&sprintId=" + s.getId();
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
            addedWork += jiraAPI.getStoryPoint(issueKey);
        }
        commitment[0] = initialCommitment;
        commitment[1] = finalCommitment;
        commitment[2] = addedWork;
        commitment[3] = completedWork;
        return commitment;
    }
}
