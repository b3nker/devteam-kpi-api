package com.jiraReportTest.jirareporttest.dto.jiraGreenhopper;

import lombok.Data;

import java.util.Map;

@Data

public class ContentsDto {
    private CompletedIssuesInitialDto completedIssuesInitialEstimateSum;
    private CompletedIssuesDto completedIssuesEstimateSum;
    private IssuesNotCompletedInitialDto issuesNotCompletedInitialEstimateSum;
    private IssuesNotCompletedDto issuesNotCompletedEstimateSum;
    private AllIssuesDto allIssuesEstimateSum;
    private Map<String,Boolean> issueKeysAddedDuringSprint;
}
