package com.jiraReportTest.jiraReportTest.dto.jiraApi;

import lombok.Data;

import java.util.List;

@Data
public class CollaboratorDto {
    private int startAt;
    private int maxResults;
    private int total;
    private List<IssueDto> issues;
}
