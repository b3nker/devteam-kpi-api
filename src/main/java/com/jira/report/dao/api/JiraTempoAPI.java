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
    /* Method that retrieves the worklog (loggedTime) from a worklogid
     * String dates should have the following format 'yyyy-dd-mm'
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

    public TempoDto connectToJiraAPI(String request) {
        return tempoWebClient
                .get()
                .uri(request)
                .retrieve()
                .bodyToMono(TempoDto.class)
                .block();
    }
}
