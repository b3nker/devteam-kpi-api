package com.jira.report.dto.jira;

import lombok.Data;

@Data
public class JiraDto {
    private int startAt;
    private int maxResults;
    private int total;
    private IssueDto[] issues;
}
