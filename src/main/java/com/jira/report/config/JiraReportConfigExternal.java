package com.jira.report.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Component
@ConfigurationProperties("jira.external")
@Data
public class JiraReportConfigExternal {
    private String planning;
    private String release;
    private Character separator;
    private int indexAccountId;
    private int firstRow;
}
