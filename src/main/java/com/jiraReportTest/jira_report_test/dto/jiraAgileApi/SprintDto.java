package com.jiraReportTest.jira_report_test.dto.jiraAgileApi;

import lombok.Data;

@Data
public class SprintDto {
    private int id;
    private String state;
    private String name;
    private String startDate;
    private String endDate;
}
