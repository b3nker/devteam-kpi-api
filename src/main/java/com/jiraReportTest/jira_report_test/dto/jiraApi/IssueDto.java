package com.jiraReportTest.jira_report_test.dto.jiraApi;

import lombok.Data;

@Data
public class IssueDto {
    private String key;
    private FieldsDto fields;
}
