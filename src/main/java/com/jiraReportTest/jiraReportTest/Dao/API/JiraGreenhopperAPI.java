package com.jiraReportTest.jiraReportTest.Dao.API;

import com.jiraReportTest.jiraReportTest.Model.Sprint;
import com.jiraReportTest.jiraReportTest.Model.SprintCommitment;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.util.Iterator;

import static com.jiraReportTest.jiraReportTest.Dao.API.API.API_TOKEN;
import static com.jiraReportTest.jiraReportTest.Dao.API.API.USERNAME;
import static java.lang.Integer.parseInt;

public class JiraGreenhopperAPI {
    final static String JIRA_GREENHOPPER_URL = "https://apriltechnologies.atlassian.net/rest/greenhopper/1.0/";

    /* Returns 4 information on a sprint
     * 0: initialCommitment
     * 1: finalCommitment
     * 2: addedWork
     * 3: completedWork
     */
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
        HttpResponse<JsonNode> response = Unirest.get(request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        JSONObject myObj = response.getBody().getObject();
        JSONObject contents = myObj.getJSONObject("contents");
        JSONObject addedIssues = contents.getJSONObject("issueKeysAddedDuringSprint");
        // Completed Issues
        JSONObject completedIssuesEstimateSum = contents.getJSONObject("completedIssuesEstimateSum");
        JSONObject completedIssuesInitialEstimateSum = contents.getJSONObject("completedIssuesInitialEstimateSum");
        // Not Completed Issues
        JSONObject issuesNotCompletedEstimateSum = contents.getJSONObject("issuesNotCompletedEstimateSum");
        JSONObject issuesNotCompletedInitialEstimateSum = contents.getJSONObject("issuesNotCompletedInitialEstimateSum");
        //All issues
        JSONObject allIssuesEstimateSum = contents.getJSONObject("allIssuesEstimateSum");

        if (completedIssuesEstimateSum.has("value")) {
            completedWork += completedIssuesEstimateSum.getInt("value");
            initialCommitment += completedIssuesEstimateSum.getInt("value");
        }
        if (completedIssuesInitialEstimateSum.has("value")) {
            initialCommitment += completedIssuesInitialEstimateSum.getInt("value");
        }
        if (issuesNotCompletedEstimateSum.has("value")) {
            initialCommitment += issuesNotCompletedEstimateSum.getInt("value");
        }
        if (issuesNotCompletedInitialEstimateSum.has("value")) {
            initialCommitment += issuesNotCompletedInitialEstimateSum.getInt("value");
        }
        if (allIssuesEstimateSum.has("value")) {
            finalCommitment += allIssuesEstimateSum.getInt("value");
        }
        //Added issues
        Iterator<String> keys = addedIssues.keys();
        while (keys.hasNext()) {
            String issueID = keys.next();
            addedWork += JiraAPI.getStoryPoint(issueID);
        }
        commitment[0] = initialCommitment;
        commitment[1] = finalCommitment;
        commitment[2] = addedWork;
        commitment[3] = completedWork;
        return commitment;
    }
}
