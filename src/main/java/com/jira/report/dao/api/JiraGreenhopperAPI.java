package com.jira.report.dao.api;

import com.jira.report.config.JiraReportConfigApi;
import com.jira.report.model.SprintCommitment;
import com.jira.report.dto.jiraGreenhopper.ContentsDto;
import com.jira.report.dto.jiraGreenhopper.JiraGreenHopperDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class JiraGreenhopperAPI {
    private final WebClient jiraWebClient;
    private final JiraReportConfigApi jiraReportConfigApi;
    private final JiraAPI jiraAPI;
    private final String baseUrl;
    private final String greenhopperUrl;

    public JiraGreenhopperAPI(WebClient jiraWebClient, JiraReportConfigApi jiraReportConfigApi, JiraAPI jiraAPI) {
        this.jiraWebClient = jiraWebClient;
        this.jiraReportConfigApi = jiraReportConfigApi;
        this.baseUrl = this.jiraReportConfigApi.getBaseUrl();
        this.jiraAPI = jiraAPI;
        this.greenhopperUrl = this.jiraReportConfigApi.getJiraGreenhopperApiUrl();
    }

    /* Returns 4 information on a sprint
     * 0: initialCommitment
     * 1: finalCommitment
     * 2: addedWork
     * 3: completedWork
     */
    public List<String> getIssueKeys(SprintCommitment s, String boardId){
        String request = this.baseUrl + this.greenhopperUrl + "rapid/charts/sprintreport" + "?rapidViewId=" + boardId + "&sprintId=" + s.getId();
        /*
        Logic
         */
        JiraGreenHopperDto greenHopperDto = connectToJiraAPI(request);
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
        String request = this.baseUrl + this.greenhopperUrl + "rapid/charts/sprintreport" + "?rapidViewId=" + boardId + "&sprintId=" + s.getId();
        /*
        Logic
         */
        JiraGreenHopperDto greenHopperDto = connectToJiraAPI(request);
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

    public JiraGreenHopperDto connectToJiraAPI(String request){
        return jiraWebClient
                .get()
                .uri(request)
                .retrieve()
                .bodyToMono(JiraGreenHopperDto.class)
                .block();
    }
}
