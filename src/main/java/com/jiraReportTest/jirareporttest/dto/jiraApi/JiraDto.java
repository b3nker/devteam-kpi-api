package com.jiraReportTest.jirareporttest.dto.jiraApi;

import lombok.Data;

@Data
public class JiraDto {
    private int startAt;
    private int maxResults;
    private int total;
    private IssueDto[] issues;
}
