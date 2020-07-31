package com.jiraReportTest.jirareporttest.dto.jiraApi;

import lombok.Data;

@Data
public class IssueDto {
    private String key;
    private FieldsDto fields;
}
