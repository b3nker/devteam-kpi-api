package com.jiraReportTest.jiraReportTest.dao.api;

import com.jiraReportTest.jiraReportTest.dto.jiraApi.CollaboratorDto;
import com.jiraReportTest.jiraReportTest.dto.tempo.ResultsDto;
import com.jiraReportTest.jiraReportTest.dto.tempo.TempoDto;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import static com.jiraReportTest.jiraReportTest.dao.api.API.*;

public class JiraTempoAPI {
    final static String JIRA_TEMPO_API_URL = "https://api.tempo.io/core/3";

    /* Method that retrieves the worklog (loggedTime) from a worklogid
     * String dates should have the following format 'yyyy-dd-mm'
     */

    public static int getWorklogByAccountID(String accountID, String startDate, String endDate){
        int loggedTime = 0;
        String request = JIRA_TEMPO_API_URL + "/worklogs/user/" + accountID + "?&from=" + startDate + "&to=" + endDate;
        WebClient webClient = WebClient.builder()
                .defaultHeader("Accept", "application/json")
                .defaultHeaders(h -> h.setBearerAuth(API_TOKEN_TEMPO))
                .build();
        TempoDto t = webClient
                .get()
                .uri(request)
                .retrieve()
                .bodyToMono(TempoDto.class)
                .block();
        for(ResultsDto r: t.getResults()){
            loggedTime += r.getTimeSpentSeconds();
        }
        return loggedTime;
    }
}
