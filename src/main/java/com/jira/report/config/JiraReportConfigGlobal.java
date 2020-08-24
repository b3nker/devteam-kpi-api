package com.jira.report.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jira.report.global")
@Data
public class JiraReportConfigGlobal {
    private String projectName;
    private String runProjectName;
    private String boardIdProject;
    private String boardIdOne;
    private String boardIdTwo;
    private String boardIdThree;
    private String boardIdFour;
    private int nbSprintsRetrospective;
    private int nbDaysBacklog;
}
