package com.jira.report.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("sprint.data.mongodb")
@Data
public class JiraReportConfigMongoDB {
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
}
