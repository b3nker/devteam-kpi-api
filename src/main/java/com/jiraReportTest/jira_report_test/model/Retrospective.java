package com.jiraReportTest.jira_report_test.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Retrospective{
    private String teamName;
    private List<SprintCommitment> sprints;
}
