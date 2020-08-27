package com.jira.report.dao.api;

import com.jira.report.config.JiraReportConfigApi;
import com.jira.report.dto.tempo.ResultsDto;
import com.jira.report.dto.tempo.TempoDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class JiraTempoAPI {
    private final WebClient tempoWebClient;
    private final String tempoUrl;

    public JiraTempoAPI(JiraReportConfigApi jiraReportConfigApi,
                        WebClient tempoWebClient){
        this.tempoWebClient = tempoWebClient;
        this.tempoUrl = jiraReportConfigApi.getTempoApiUrl();
    }

    /**
     * Creates a double from fetching worklokgs hours in Tempo API.
     * @param accountID Collaborator's accountId from which we wish to collect worklogs
     * @param startDate Date from which we want to start gathering worklogs data
     * @param endDate Date from which we want to stop gathering worklogs data
     * @return A double representing the number of hours logged on Tempo by a collaborator
     */
    public double getWorklogByAccountID(String accountID, String startDate, String endDate){
        double loggedTime = 0;
        String request = this.tempoUrl + "/worklogs/user/" + accountID + "?&from=" + startDate + "&to=" + endDate;
        TempoDto t = connectToJiraAPI(request);
        assert t != null;
        for(ResultsDto r: t.getResults()){
            loggedTime += r.getTimeSpentSeconds() / (double)3600;
        }
        return loggedTime;
    }

    public double getWorklogByIssue(String accountID, String issueKey, String startDate, String endDate){
        double loggedTime = 0;
        String request = this.tempoUrl + "/worklogs/user/" + accountID + "?&from=" + startDate + "&to=" + endDate;
        TempoDto t = connectToJiraAPI(request);
        assert t != null;
        for(ResultsDto r: t.getResults()){
            if(issueKey.equals(r.getIssue().getKey())){
                loggedTime += r.getTimeSpentSeconds() / (double)3600;
            }
        }
        return loggedTime;
    }

    /**
     * Creates a TempoDto object
     * @param request The request we want to GET data from
     * @return A TempoDto object containing parsed data from the GET request to the API
     */
    public TempoDto connectToJiraAPI(String request) {
        return tempoWebClient
                .get()
                .uri(request)
                .retrieve()
                .bodyToMono(TempoDto.class)
                .block();
    }
}
