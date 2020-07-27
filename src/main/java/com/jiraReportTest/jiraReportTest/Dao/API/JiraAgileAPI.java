package com.jiraReportTest.jiraReportTest.Dao.API;

import com.jiraReportTest.jiraReportTest.Model.Sprint;
import com.jiraReportTest.jiraReportTest.Model.SprintCommitment;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.util.ArrayList;

import static com.jiraReportTest.jiraReportTest.Dao.API.API.*;
import static java.lang.Integer.parseInt;

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
        JSONObject myObj;
        JSONArray values;
        JSONObject value;
        String request = JIRA_AGILE_API_URL + "/board/" + BOARD_ID + "/sprint";
        int lastlyActiveSprintIndex = -1;

        /*
        Logic
         */
        HttpResponse<JsonNode> response = Unirest.get(request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        myObj = response.getBody().getObject();
        values = myObj.getJSONArray("values");
        for (int i = 0; i < values.length(); i++) {
            value = values.getJSONObject(i);
            if (value.getString("state").equals("active")) {
                lastlyActiveSprintIndex = i;
            }
        }
        for (int i = 0; i < nbSprints; i++){
            value = values.getJSONObject(lastlyActiveSprintIndex - i);
            sprintName = value.getString("name");
            sprintId = parseInt(value.getString("id"));
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
        String request = JIRA_AGILE_API_URL + "board/" + BOARD_ID + "/sprint";
        HttpResponse<JsonNode> response = Unirest.get(request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        JSONObject myObj = response.getBody().getObject();
        JSONArray values = myObj.getJSONArray("values");
        for (int i = 0; i < values.length(); i++) {
            JSONObject value = values.getJSONObject(i);
            if (value.getString("state").equals("active")) {
                sprintName = value.getString("name");
                startDate = value.getString("startDate");
                endDate = value.getString("endDate");
                sprintId = parseInt(value.getString("id"));
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

    /* Method that returns the lastly active sprint that contains teamName in its name
     *
     */
    public static Sprint getLastlyActiveTeamSprint(String teamName){
        String startDate = "";
        String endDate = "";
        String sprintName = "";
        int sprintId = 0;
        String teamNameLC = teamName.toLowerCase();
        String request = JIRA_AGILE_API_URL + "board/" + BOARD_ID + "/sprint";
        HttpResponse<JsonNode> response = Unirest.get(request)
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        JSONObject myObj = response.getBody().getObject();
        JSONArray values = myObj.getJSONArray("values");
        mainLoop:
        for (int i = 0; i < values.length(); i++) {
            JSONObject value = values.getJSONObject(i);
            String name = value.getString("name").toLowerCase();
            if (ACTIVE_SPRINT.equals(value.getString("state")) && name.contains(teamNameLC)) {
                sprintName = value.getString("name");
                startDate = value.getString("startDate");
                endDate = value.getString("endDate");
                sprintId = parseInt(value.getString("id"));
                break mainLoop;
            }
        }
        return Sprint.builder()
                .id(sprintId)
                .name(sprintName)
                .startDate(Sprint.toLocalDateTime(startDate))
                .endDate(Sprint.toLocalDateTime(endDate))
                .build();
    }
}
