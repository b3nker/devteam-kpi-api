package com.jira.report.dto.jira;

import lombok.Data;

@Data
public class IssueTypeDto {
    private String name;
    private Boolean subtask;
}
