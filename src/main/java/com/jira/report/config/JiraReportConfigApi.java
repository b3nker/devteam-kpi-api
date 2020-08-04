package com.jira.report.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jira.report.api")
@Data
public class JiraReportConfigApi {
    private String baseUrl;
    private String jiraApiUrl;
    private String jiraGreenhopperApiUrl;
    private String tempoApiUrl;
    private String username;
    private String jiraToken;
    private String tempoToken;
    private String nbDaysBacklog;
    private String jiraAgileApiUrl;
}
