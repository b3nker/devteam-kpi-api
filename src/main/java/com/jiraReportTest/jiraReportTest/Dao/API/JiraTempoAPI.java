package com.jiraReportTest.jiraReportTest.Dao.API;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import static com.jiraReportTest.jiraReportTest.Dao.API.API.*;

public class JiraTempoAPI {

    final static String JIRA_TEMPO_API_URL = "https://apriltechnologies.atlassian.net/rest/tempo-timesheets/5/";
    /* Method that retrieves the worklog from a worklogid
     * String dates should have the following format 'yyyy-dd-mm'
     */
    public static int getWorklogByAccountID(String accountID, String startDate, String endDate){
        int worklog = 0;
        String request = JIRA_TEMPO_API_URL;
        HttpResponse<JsonNode> response = Unirest.get(request)
                .basicAuth(USERNAME, API_TOKEN_TEMPO)
                .header("Accept", "application/json")
                .asJson();
        JSONObject myObj = response.getBody().getObject();
        return worklog;
    }
}
