package com.jira.report.dto.jira;

import lombok.Data;

@Data
public class IssueDto {
    private String key;
    private FieldsDto fields;
}
