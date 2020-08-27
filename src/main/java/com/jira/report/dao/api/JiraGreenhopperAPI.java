package com.jira.report.dao.api;

import com.jira.report.config.JiraReportConfigApi;
import com.jira.report.config.JiraReportConfigGlobal;
import com.jira.report.model.SprintCommitment;
import com.jira.report.dto.greenhopper.ContentsDto;
import com.jira.report.dto.greenhopper.JiraGreenHopperDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class JiraGreenhopperAPI {
    private final WebClient jiraWebClient;
    private final String projectName;
    private final JiraAPI jiraAPI;
    private final String baseUrl;
    private final String greenhopperUrl;

    public JiraGreenhopperAPI(WebClient jiraWebClient,
                              JiraReportConfigApi jiraReportConfigApi,
                              JiraReportConfigGlobal jiraReportConfigGlobal,
                              JiraAPI jiraAPI) {
        this.jiraWebClient = jiraWebClient;
        this.baseUrl = jiraReportConfigApi.getBaseUrl();
        this.jiraAPI = jiraAPI;
        this.projectName = jiraReportConfigGlobal.getProjectName();
        this.greenhopperUrl = jiraReportConfigApi.getJiraGreenhopperApiUrl();
    }

    /**
     * Creates a list of strings. Each string corresponds to an issue that has been added in the specified input sprint
     * @param s SprintCommitment object from which we want to get added issues
     * @param boardId Board to which the sprint is linked
     * @return All added issues as list of strings
     */
    public List<String> getIssueKeys(SprintCommitment s, String boardId){
        String request = this.baseUrl + this.greenhopperUrl + "rapid/charts/sprintreport" + "?rapidViewId=" + boardId + "&sprintId=" + s.getId();
        JiraGreenHopperDto greenHopperDto = connectToJiraAPI(request);
        assert greenHopperDto != null;
        Set<String> addedIssues = greenHopperDto.getContents().getIssueKeysAddedDuringSprint().keySet();
        return new ArrayList<>(addedIssues);
    }

    /**
     * Creates an array of double by fetching data on initial commitment,...
     * @param s SprintCommitment object from which we want to get added issues
     * @param boardId Board to which the sprint is linked
     * @return An array of double, collecting SprintCommitment data (initial commitment,...)
     */
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

    /**
     * Creates a JiraGreenHopperDto object
     * @param request The request we want to GET data from
     * @return A JiraGreenHopperDto object containing parsed data from the GET request to the API
     */
    public JiraGreenHopperDto connectToJiraAPI(String request){
        return jiraWebClient
                .get()
                .uri(request)
                .retrieve()
                .bodyToMono(JiraGreenHopperDto.class)
                .block();
    }
}
