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

    /* Returns an array of size (nbSprints) of the lastly closed sprints including the lastly active sprint
     */
    public static ArrayList<SprintCommitment> getLastlyClosedSprints(int nbSprints) {
        /*
        Variables
         */
        ArrayList<SprintCommitment> sprints = new ArrayList<>(nbSprints);
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
        int i = 0;
        while (i < nbSprints) {
            value = values.getJSONObject(lastlyActiveSprintIndex - i);
            sprintName = value.getString("name");
            sprintId = parseInt(value.getString("id"));
            SprintCommitment s = SprintCommitment.builder()
                    .name(sprintName)
                    .id(sprintId)
                    .build();
            sprints.add(s);
            i++;
        }
        return sprints;
    }

    /* Method that returns the lastly active sprint in JIRA Agile API
     *
     */
    public static Sprint getLastlyActiveSprint() {
        String startDate = "";
        String endDate = "";
        String sprintName = "";
        int sprintId = 0;
        HttpResponse<JsonNode> response = Unirest.get(JIRA_AGILE_API_URL + "board/" + BOARD_ID + "/sprint")
                .basicAuth(USERNAME, API_TOKEN)
                .header("Accept", "application/json")
                .asJson();
        JSONObject myObj = response.getBody().getObject();
        JSONArray values = myObj.getJSONArray("values");
        mainLoop:
        for (int i = 0; i < values.length(); i++) {
            JSONObject value = values.getJSONObject(i);
            //Condition à modifier quand il n'y aura qu'un seul sprint actif
            if (value.getString("state").equals("active")) {
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
