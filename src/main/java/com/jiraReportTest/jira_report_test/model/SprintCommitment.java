package com.jiraReportTest.jira_report_test.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SprintCommitment {
    private int id;
    private String name;
    private double initialCommitment;
    private double finalCommitment;
    private double addedWork;
    private double completedWork;
    private List<String> addedIssueKeys;

}
